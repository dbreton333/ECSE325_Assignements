package ca.mcgill.ecse420.a3;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedLockBasedQueue<T> {
    private final T[] queue;
    private final int capacity;
    private int head = 0;
    private int tail = 0;
    private final Lock headLock = new ReentrantLock();
    private final Lock tailLock = new ReentrantLock();
    private final AtomicInteger size = new AtomicInteger(0);
    private final Condition notEmpty = headLock.newCondition();
    private final Condition notFull = tailLock.newCondition();

    @SuppressWarnings("unchecked")
    public BoundedLockBasedQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) new Object[capacity];
    }

    public void enqueue(T item) throws InterruptedException {
        boolean mustWakeDequeuers = false;
        tailLock.lock();
        try {
            while (size.get() == capacity) {
                // Wait for space to become available
                notFull.await();
            }
            queue[tail] = item;
            tail = (tail + 1) % capacity;
            if (size.getAndIncrement() == 0) {
                mustWakeDequeuers = true;
            }
        } finally {
            tailLock.unlock();
        }
        if (mustWakeDequeuers) {
            notEmpty.signalAll();
        }
    }

    public T dequeue() throws InterruptedException {
        T item;
        boolean mustWakeEnqueuers = false;
        headLock.lock();
        try {
            while (size.get() == 0) {
                // Wait for items to become available
                notEmpty.await();
            }
            item = queue[head];
            queue[head] = null; // Help GC
            head = (head + 1) % capacity;
            if (size.getAndDecrement() == capacity) {
                mustWakeEnqueuers = true;
            }
        } finally {
            headLock.unlock();
        }
        if (mustWakeEnqueuers) {
            notFull.signalAll();
        }
        return item;
    }
}