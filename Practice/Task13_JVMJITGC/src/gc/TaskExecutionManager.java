package gc;

public class TaskExecutionManager implements ExecutionManager {

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {

        GeneralThreadPool pool = new GeneralThreadPool(3, 3){
            @Override
            protected void terminated(){
                callback.run();
            }
        };

        for(int i = 0; i < tasks.length; i++) {
            pool.execute(tasks[i]);
        }
        pool.shutdown();

        return pool.getContext();
    }

}
