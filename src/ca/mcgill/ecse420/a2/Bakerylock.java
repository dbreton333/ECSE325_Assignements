package ca.mcgill.ecse420.a2;

import java.util.*;

public class Bakerylock {

    List<Boolean> flag;
    List<Integer> label;

    public Bakerylock(int n) {
        this.flag = Collections.synchronizedList(new ArrayList<Boolean>(n));
        this.label = Collections.synchronizedList(new ArrayList<Integer>(n));
        for (int i = 0; i < n; i++) {
            flag.add(false);
            label.add(0);
        }
    }
    
    public void lock(int threadId) {
        flag.set(threadId, true);
        label.set(threadId, Collections.max(label) + 1);
        flag.set(threadId, false);
        for (int k = 0; k < label.size(); k++) {
            while (k != threadId && flag.get(k) && (label.get(k) < label.get(threadId) || (label.get(k) == label.get(threadId) && k < threadId))) {
                // busy wait
            }
        }
    }

    public void unlock(int threadId) {
        label.set(threadId, 0);
    }

    public static void main(String[] args) {
        int n = 5;
        Bakerylock lock = new Bakerylock(n);
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Starting thread " + i);

            final int threadId = i;
            Thread t = new Thread(() -> {
                while(true){
                lock.lock(threadId);
                System.out.println("Thread " + threadId + " is in critical section");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock(threadId);
                }
            });
            threads[i] = t;
            t.start();
        }
    }
    
}
