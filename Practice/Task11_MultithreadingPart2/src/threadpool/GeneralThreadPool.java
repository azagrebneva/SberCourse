package threadpool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class GeneralThreadPool {

    private final ReentrantLock mainLock = new ReentrantLock();

    private volatile AtomicInteger poolSize;
    final private int minimumPoolSize;
    final private int maximumPoolSize;

    /**
     * The queue used for holding tasks and handing off to worker
     * threads.
     */
    private final BlockingQueue<Runnable> workQueue;

    /** The state of the thread pool */
    volatile int runState = RUNNING;

    /** Different states of threads in thread pool */
    static final int RUNNING = 0;
    static final int SHUTDOWN = 1;
    static final int STOP = 2;
    static final int TERMINATED = 3;

    /**
     * Set containing all worker threads in pool.
     * Accessed only when holding mainLock.
     */
    private final HashSet<Worker> workers = new HashSet<>();

    /** count amount of tasks */
    private long completedTaskCount;

    /** For naming the thread */
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String namePrefix;

    public GeneralThreadPool(int fixSize) {
        if (fixSize <= 0)
            throw new IllegalArgumentException();
        this.poolSize = new AtomicInteger(0);
        this.workQueue = new LinkedBlockingQueue<Runnable>();
        this.minimumPoolSize = fixSize;
        this.maximumPoolSize = fixSize;

        // for thread name
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    public GeneralThreadPool(int minSize, int maxSize) {
        if ((minSize > maxSize) || ( minSize < 0))
            throw new IllegalArgumentException();
        this.poolSize = new AtomicInteger(0);
        this.workQueue = new LinkedBlockingQueue<Runnable>();
        this.minimumPoolSize = minSize;
        this.maximumPoolSize = maxSize;

        // for thread name
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    /* Executes the thread pool.
     * Proceed in 3 steps:
     *
     * 1. If fewer than corePoolSize threads are running, try to
     * start a new thread with the given command as its first
     * task.  The call to addWorker atomically checks runState and
     * workerCount, and so prevents false alarms that would add
     * threads when it shouldn't, by returning false.
     *
     * 2. If a task can be successfully queued, then we still need
     * to double-check whether we should have added a thread
     * (because existing ones died since last checking) or that
     * the pool shut down since entry into this method. So we
     * recheck state and if necessary roll back the enqueuing if
     * stopped, or start a new thread if there are none.
     *
     * 3. If we cannot queue task, then we try to add a new
     * thread.  If it fails, we know we are shut down or saturated
     * and so reject the task.
     */
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();

        // add minimal number of threads into thread pool
        if ((poolSize.get() < minimumPoolSize) ||
            (!workQueue.isEmpty() && (poolSize.get() < maximumPoolSize)))
            if (addWorkerInTheThreadPool(command, maximumPoolSize)){
                return;
            }

        if (runState == RUNNING && workQueue.offer(command)) {
            // recheck pool status
            if (!(runState == RUNNING) && remove(command))
                reject(command);
            else if (0 == poolSize.get()) {
                addWorkerInTheThreadPool(null, minimumPoolSize);
            }
        } else if (!addWorkerInTheThreadPool(command, maximumPoolSize)) {
            reject(command);
        }
    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     *
     * <p>This method does not wait for previously submitted tasks to
     * complete execution.
     *
     * @throws SecurityException {@inheritDoc}
     */
    public void shutdown() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            runState = SHUTDOWN;

            interruptIdleWorkers();

        } finally {
            mainLock.unlock();
        }
        tryTerminate();
    }

    public boolean remove(Runnable task) {
        boolean removed = workQueue.remove(task);
        tryTerminate(); // In case SHUTDOWN and now empty
        return removed;
    }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks, and returns a list of the tasks
     * that were awaiting execution. These tasks are drained (removed)
     * from the task queue upon return from this method.
     */
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            runState = STOP;
            interruptWorkers();
            tasks = drainQueue();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
        return tasks;
    }

    /**
     * Transitions to TERMINATED state if either (SHUTDOWN and pool
     * and queue empty) or (STOP and pool empty).  If otherwise
     * eligible to terminate but workerCount is nonzero, interrupts an
     * idle worker to ensure that shutdown signals propagate. This
     * method must be called following any action that might make
     * termination possible -- reducing worker count or removing tasks
     * from the queue during shutdown. The method is non-private to
     * allow access from ScheduledThreadPoolExecutor.
     */
    final void tryTerminate() {
        for (;;) {
            if (runState == RUNNING ||
                    runState == TERMINATED ||
                    (runState < STOP && ! workQueue.isEmpty()))
                return;

            // SHUTDOWN and workQueue is empty or
            // STOP
            if (poolSize.get() != 0) { // Eligible to terminate
                //interruptIdleWorkers(ONLY_ONE);
                return;
            }

            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                runState = TERMINATED;
                return;
            } finally {
                mainLock.unlock();
            }
        }
    }

    private void reject(Runnable command){
        throw new RejectedExecutionException("Impossible to proceed the command " + command.getClass());
    }

    private boolean addWorkerInTheThreadPool(Runnable firstTask, int minimumPoolSize) {
        Worker w = null;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (poolSize.get() < minimumPoolSize &&
                    runState == RUNNING) {
                w = addWorker(firstTask);
            }
        } finally {
            mainLock.unlock();
        }
        if (w == null) {
            return false;
        }
        w.thread.start();
        return true;
    }

    private Worker addWorker(Runnable task){
        Worker w = new Worker(task); // Runnable
        if (w.thread == null) {
            return null;
        }
        // ???
        workers.add(w);
        poolSize.addAndGet(1);
        return w;
    }

    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {

        /** Thread this worker is running in.  Null if factory fails. */
        final Thread thread;
        /** Initial task to run.  Possibly null. */
        private Runnable firstTask;
        /** Per-thread task counter. */
        volatile AtomicInteger completedTasks = new AtomicInteger(0);
        /** Lock for worker. */
        //private final ReentrantLock runLock = new ReentrantLock();

        public boolean isLocked() { return isHeldExclusively(); }

        // ----------------------------------
        // For safe running
        // The value 0 represents the unlocked state.
        // The value 1 represents the locked state.
        protected boolean isHeldExclusively() {
            return getState() != 0;
        }
        protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        protected boolean tryRelease(int unused) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
        public void lock()        { acquire(1); }
        public boolean tryLock()  { return tryAcquire(1); }
        public void unlock()      { release(1); }
        //--------------------------------

        /**
         * Creates with given first task and thread from ThreadFactory.
         * @param firstTask the first task (null if none)
         */
        Worker(Runnable firstTask) {
            setState(-1); // inhibit interrupts until runWorker
            this.firstTask = firstTask;
            this.thread = newThread(this);
        }

        @Override
        public void run() {
            Runnable task = firstTask;
            firstTask = null;
            this.unlock(); // allow interrupts
            try {
                while (task != null || (task = getTask()) != null) {
                    runWorker(task);
                    task = null;
                }
            } finally {
                processWorkerExit(this);
            }
        }

        /** perform a task if status is not (STOP and TERMINATED) */
        private void runWorker(Runnable task){
            //final ReentrantLock runLock = this.runLock;
            //runLock.lock();
            this.lock();
            try {
                if (runState >= STOP)
                    Thread.currentThread().interrupt();

                try {
                    task.run();
                } catch (Throwable ex) {
                    throw ex;
                }
            }
            finally {
                completedTasks.getAndIncrement();
                this.unlock();
                //runLock.unlock();
            }
        }

        /** interrupts the idle thread */
        void interruptIfIdle(){
            //final ReentrantLock runLock = this.runLock;
            //if (runLock.tryLock()) { // we obtain the lock
            if (this.tryLock()) { // we obtain the lock
                try {
                    if (!thread.isInterrupted() &&
                            thread != Thread.currentThread()) //???
                        thread.interrupt();
                } finally {
                    //runLock.unlock();
                    this.unlock();
                }
            }
        }

        void interruptIfStarted() {
            Thread t;

            // The value 0 represents the unlocked state.
            // The value 1 represents the locked state.
            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
                //if ((t = thread) != null && !t.isInterrupted()) {
                //try {
                t.interrupt();
                //} catch (SecurityException ignore) {
                // }
            }
        }
    }

    /** Makes actions when the worker finished his work */
    private void processWorkerExit(Worker w){

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            poolSize.addAndGet(-1);
            completedTaskCount += w.completedTasks.get();
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }

        tryTerminate();

        // add a new worker if necessary
        if (runState < STOP){

            int minimumWorkerCount = minimumPoolSize;
            if (runState == SHUTDOWN) {
                if (workQueue.isEmpty()) return;
                // we have work, but may haven't workers
                if (minimumPoolSize == 0)
                    minimumWorkerCount = 1;
            }

            if (poolSize.get() >= minimumWorkerCount) return;
            addWorkerInTheThreadPool(null, minimumPoolSize);
        }
    }

    /** Creates a new thread with specified name */
    private Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }

    /**
     * Performs blocking, depending on current configuration
     * settings, or returns null if this worker must exit
     * @return task, or null if the worker must exit
     */
    private Runnable getTask() {

        for (; ; ) {
            try {
                int state = runState;

                if (runState >= SHUTDOWN) {
                    if ((runState >= STOP) || (workQueue.isEmpty()))
                        return null;
                }

                Runnable r;
                if ((state == SHUTDOWN) ||
                   (poolSize.get() > minimumPoolSize))
                    r = workQueue.poll(100, TimeUnit.MILLISECONDS); // drain the queue
                else
                    r = workQueue.take();

                if (r != null) {
                    return r;
                }

                // worker can exit
                if (poolSize.get() > maximumPoolSize)
                    return null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void interruptIdleWorkers(){
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w: workers){
                w.interruptIfIdle();
            }
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Interrupts all threads, even if active. Ignores SecurityExceptions
     * (in which case some threads may remain uninterrupted).
     */
    private void interruptWorkers() {
        // assert mainLock.isHeldByCurrentThread();
        for (Worker w : workers)
            w.interruptIfStarted();
    }

    /**
     * Drains the task queue into a new list, normally using
     * drainTo. But if the queue is a DelayQueue or any other kind of
     * queue for which poll or drainTo may fail to remove some
     * elements, it deletes them one by one.
     */
    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> q = workQueue;
        ArrayList<Runnable> taskList = new ArrayList<>();
        q.drainTo(taskList);
        if (!q.isEmpty()) {
            for (Runnable r : q.toArray(new Runnable[0])) {
                if (q.remove(r))
                    taskList.add(r);
            }
        }
        return taskList;
    }


    /**
     * Returns a string identifying this pool, as well as its state,
     * including indications of run state and estimated worker and
     * task counts.
     *
     * @return a string identifying this pool, as well as its state
     */
    public String toString() {
        long ncompleted;
        int nworkers, nactive;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            ncompleted = completedTaskCount;
            nactive = 0;
            nworkers = workers.size();
            for (Worker w : workers) {
                ncompleted += w.completedTasks.get();
                if (w.isLocked())
                    ++nactive;
            }
        } finally {
            mainLock.unlock();
        }

        String strRunState =
                runState == RUNNING ? "Running" :
                        runState >= TERMINATED ? "Terminated" :
                                "Shutting down";
        return  super.toString() +
                "[" + strRunState +
                ", pool size = " + nworkers +
                ", active threads = " + nactive +
                ", queued tasks = " + workQueue.size() +
                ", completed tasks = " + ncompleted +
                "]";
    }
}
