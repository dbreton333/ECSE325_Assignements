
package ca.mcgill.ecse420.a3;
import java.util.concurrent.locks.ReentrantLock;

class Node<T> {
    public int key;
    public T item;
    public volatile Node<T> next;
    ReentrantLock lock;

    Node(T item) {

        this.item = item;
        this.next = null;
        this.lock = new ReentrantLock();
    }

    void lock() {
        lock.lock();
    }

    void unlock() {
        lock.unlock();
    }
}

public class SynchronizedLinkedList<T> {

    private Node<T> head;

    public SynchronizedLinkedList() {
        head = new Node<>(null);
        head.key = Integer.MIN_VALUE;
        
        // Tail node with maximum value key to keep list sorted by key
        Node<T> tail = new Node<>(null);
        tail.key = Integer.MAX_VALUE;
        
        head.next = tail;
    }

    public boolean remove(String item){
        Node<T> pred, curr;
        int key = item.hashCode();
        pred = head;
        curr = pred.next;
       
        try {
            pred.lock();
            curr.lock();

            //find the node with the key
            while (curr.key < key) {
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
            }

            if (curr.key == key && curr.item.equals(item)) {
                pred.next = curr.next;  
                return true;
            }
            return false;

        } finally {
            curr.unlock();
            pred.unlock();
        }
        
    }

    public boolean add(T item) {
        Node<T> pred, curr;
        int key = item.hashCode();
        pred = head;
        curr = pred.next;
        
        try {
            pred.lock();
            curr.lock();

            //find the position where the key should be inserted
            while (curr.key < key) {
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
            }

            //check if the key is already in the list
            if (curr.key == key) {
                return false;
            }
            Node<T> newNode = new Node<>(item);
            newNode.key = key;
            newNode.next = curr;
            pred.next = newNode;
            return true;

        } finally {
            pred.unlock();
            curr.unlock();
        }
    }

    public boolean contains(T item) {
        Node<T> pred, curr;
        int key = item.hashCode();
        pred = head;
        curr = pred.next;
        try {
            pred.lock();
            curr.lock();
            
            //find the node with the key
            while (curr.key < key) {
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
            }
            
            if (curr.key == key && curr.item.equals(item)){
                return true;
            }
            return false;
           
        } finally {
            pred.unlock();
            curr.unlock();
        }
    }

    public static void main(String[] args) {
      
      
    }
}