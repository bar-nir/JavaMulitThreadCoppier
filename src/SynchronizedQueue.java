//ID:319024303

/**
 * A synchronized bounded-size queue for multithreaded producer-consumer applications.
 *
 * @param <T> Type of data items
 */
public class SynchronizedQueue<T> {

    private T[] buffer;
    private int producers;
    // TODO: Add more private members here as necessary
    //Size of the queue
    private int size;
    //First item in queue
    private int head;
    //Capacity of the queue
    private int capacity;

    /**
     * Constructor. Allocates a buffer (an array) with the given capacity and
     * resets pointers and counters.
     *
     * @param capacity Buffer capacity
     */
    @SuppressWarnings("unchecked")
    public SynchronizedQueue(int capacity) {
        this.buffer = (T[]) (new Object[capacity]);
        this.producers = 0;
        // TODO: Add more logic here as necessary
        this.size = 0;
        this.capacity = capacity;
        this.head = 0;
    }

    /**
     * Dequeues the first item from the queue and returns it.
     * If the queue is empty but producers are still registered to this queue,
     * this method blocks until some item is available.
     * If the queue is empty and no more items are planned to be added to this
     * queue (because no producers are registered), this method returns null.
     *
     * @return The first item, or null if there are no more items
     * @see #registerProducer()
     * @see #unregisterProducer()
     */
    public synchronized T dequeue() {
        //until the queue is not empty sleeping the thread.
        while (this.size == 0) {
            //Checks if there are no producers on the queue
            if (this.producers == 0) {
                return null;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        //Popping the item from the queue
        T item = buffer[head];
        this.head = (this.head + 1) % this.capacity;
        this.size--;
        //notify another thread that item is popped out from the queue
        notify();
        return item;
    }

    /**
     * Enqueues an item to the end of this queue. If the queue is full, this
     * method blocks until some space becomes available.
     *
     * @param item Item to enqueue
     */
    public synchronized void enqueue(T item) {
        //Until the queue is not full sleeping the thread.
        while (this.getSize() == this.getCapacity()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        //Inserting item into the queue
        this.buffer[(this.head + this.size) % this.capacity] = item;
        this.size++;
        //notifying other thread that item inserted to the queue.
        notify();
    }

    /**
     * Returns the capacity of this queue
     *
     * @return queue capacity
     */
    public synchronized int getCapacity() {
        return this.capacity;
    }

    /**
     * Returns the current size of the queue (number of elements in it)
     *
     * @return queue size
     */
    public synchronized int getSize() {
        return this.size;
    }

    /**
     * Registers a producer to this queue. This method actually increases the
     * internal producers counter of this queue by 1. This counter is used to
     * determine whether the queue is still active and to avoid blocking of
     * consumer threads that try to dequeue elements from an empty queue, when
     * no producer is expected to add any more items.
     * Every producer of this queue must call this method before starting to
     * enqueue items, and must also call <see>{@link #unregisterProducer()}</see> when
     * finishes to enqueue all items.
     *
     * @see #dequeue()
     * @see #unregisterProducer()
     */
    public synchronized void registerProducer() {
        this.producers++;

    }

    /**
     * Unregisters a producer from this queue. See <see>{@link #registerProducer()}</see>.
     *
     * @see #dequeue()
     * @see #registerProducer()
     */
    public synchronized void unregisterProducer() {
        this.producers--;
        //notify all threads that producer got decreased.
        notifyAll();
    }
}
