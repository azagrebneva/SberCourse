package stream;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Streams<T> {

    /**
     * Backlink to the head of the pipeline chain (self if this is the source stage)
     */
    private final Streams sourceStage;


    /**
     * The "upstream" pipeline, or null if this is the source stage.
     */
    private final Streams previousStage;

    /**
     * The next stage in the pipeline, or null if this is the last stage. Effectively final at the point of linking to the next pipeline.
     */
    private Streams<?> nextStage;

    /**
     * The number of intermediate operations between this pipeline object
     * and the stream source if sequential, or the previous stateful if parallel.
     * Valid at the point of pipeline preparation for evaluation.
     */
    private int depth;

    /**
     * The source spliterator. Only valid for the head pipeline.
     * Before the pipeline is consumed if non-null then {@code sourceSupplier}
     * must be null. After the pipeline is consumed if non-null then is set to
     * null.
     */
    private Spliterator<?> sourceSpliterator;

    /**
     * True if this pipeline has been linked or consumed
     */
    private boolean linkedOrConsumed;

    /**
     * Predicate for filtration
     */
    private final Predicate<? super T> predicate;

    /**
     * Function for transformation
     */
    private final Function<T, ?> mapper;

    /**
     * Constructor for the head of a stream pipeline.
     *
     * @param source {@code Spliterator} describing the stream source
     */
    private Streams(Spliterator<T> source) {
        this.sourceStage = this;
        this.previousStage = null;
        this.depth = 0;
        this.sourceSpliterator = source;
        this.nextStage = null;
        this.linkedOrConsumed = false;
        this.predicate = null;
        this.mapper = null;
    }

    /**
     * Constructor for appending an intermediate operation stage onto an
     * existing pipeline.
     *
     * @param previousStage the upstream pipeline stage
     */
    private Streams(Streams<T> previousStage, Predicate<? super T> predicate) {
        if (previousStage.linkedOrConsumed)
            throw new IllegalStateException("Streams is already used.");
        previousStage.linkedOrConsumed = true;
        previousStage.nextStage = this;

        this.previousStage = previousStage;
        this.sourceStage = previousStage.sourceStage;
        this.depth = previousStage.depth + 1;
        this.predicate = predicate;
        this.mapper = null;
    }

    /**
     * Constructor for appending an intermediate operation stage onto an
     * existing pipeline.
     *
     * @param previousStage the upstream pipeline stage
     */
    private Streams(Streams<?> previousStage, Function<T, Object> mapper) {
        if (previousStage.linkedOrConsumed)
            throw new IllegalStateException("Streams is already used.");
        previousStage.linkedOrConsumed = true;
        previousStage.nextStage = this;

        this.previousStage = previousStage;
        this.sourceStage = previousStage.sourceStage;
        this.depth = previousStage.depth + 1;
        this.predicate = null;
        this.mapper = mapper;
    }

    /**
     * Returns a sequential {@link Streams} with the specified List as its
     * source.
     *
     * @param <T> The type of the list elements
     * @return a {@code Streams} for the collection
     */
    public static <T> Streams<T> of(List<T> list) {
        return new Streams<>(list.spliterator());
    }

    /**
     * Returns a stream consisting of the elements of this stream that match
     * the given predicate.
     *
     * <p>This is an intermediate operation</a>.
     *
     * @param predicate a predicate to apply to each element to determine if it
     *                  should be included
     * @return the new streams
     */
    public Streams<T> filter(Predicate<? super T> predicate){
        return new Streams<>(this, predicate);
    }

    /**
     * Returns a stream consisting of the results of applying the given
     * function to the elements of this stream.
     *
     * <p>This is an intermediate operation</a>.
     *
     * @param <R>    The element type of the new stream
     * @param mapper function to apply to each element
     * @return the new streams
     */
    public <R> Streams<R> transform(Function<? super T, ? extends R> mapper) {
        return new Streams(this, mapper);
    }


    /**
     * Returns a {@code Map} whose keys and values are the result of applying the provided
     * mapping functions to the input elements.
     *
     * <p>If the mapped keys contain duplicates (according to
     * {@link Object#equals(Object)}), an {@code IllegalStateException} is
     * thrown when the collection operation is performed.
     *
     * <p>There are no guarantees on the type, mutability, serializability,
     * or thread-safety of the {@code Map} returned.
     *
     * @param <K> the output type of the key mapping function
     * @param <V> the output type of the value mapping function
     * @param keyMapper a mapping function to produce keys
     * @param valueMapper a mapping function to produce values
     * @return a {@code Collector} which collects elements into a {@code Map}
     * whose keys and values are the result of applying mapping functions to
     * the input elements
     */
     public <K,V> Map<K,V> toMap( Function<? super T, ? extends K> keyMapper,
                      Function<? super T, ? extends V> valueMapper) {
        if (linkedOrConsumed)
            throw new IllegalStateException("The stream is linked or consumed.");
        linkedOrConsumed = true;

        Spliterator spliterator = sourceStage.sourceSpliterator;

        // moving along the pipe
        for (@SuppressWarnings("rawtypes") Streams u = sourceStage, p = sourceStage.nextStage, e = this;
             u != e;
             u = p, p = p.nextStage) {
            spliterator = doAction(u,  spliterator);
        }

        // last element in the pipe
        spliterator = doAction(this,  spliterator);
        final Spliterator<T> spl  = (Spliterator<T>) spliterator;

        Map<K,V> result = new HashMap<>();
        spl.forEachRemaining(element -> {
            K k = keyMapper.apply(element);
            V v = Objects.requireNonNull(valueMapper.apply(element));
            V u = result.putIfAbsent(k, v);
            if (u != null) throw new RuntimeException("Key is associated with several values");
        });

        return result;
    }

    <R> Spliterator<?> doAction(Streams<T> s, Spliterator<T> spliterator) {
        if (s.predicate != null) {
            List<T> list = new ArrayList<>();
            spliterator.forEachRemaining(element -> {
                if (s.predicate.test(element)) {
                    list.add(element);
                }
            });
            return list.spliterator();
        } else if (s.mapper != null) {
            List<R> list = new ArrayList<>();
            spliterator.forEachRemaining(element -> {
                R r = (R) s.mapper.apply(element);
                list.add(r);
            });
            return list.spliterator();
        }

        return spliterator;
    }
}