package ca.mcgill.ecse420.a2;

import java.util.*;

public class Bakerylock {

    List<Boolean> flag;
    List<Integer> label;
    int n;
    private static int counter = 0;
    private static int ITERATIONS = 10;

    public Bakerylock(int n) {
        this.n = n;
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

        boolean conflict;
        do{
            conflict = false;
            for (int k = 0; k < n; k++) {
                if (k != threadId && flag.get(k) && label.get(k) < label.get(threadId)){
                    conflict = true;
                    break;
                }
            }
        }while(conflict);
    }

    public void unlock(int threadId) {
        flag.set(threadId, false);
    }

    public static void main(String[] args) {
       int n = 5;
       Bakerylock lock = new Bakerylock(n);
       Thread[] threads = new Thread[n];

       for (int i = 0; i < n; i++) {
           System.out.println("Starting thread " + i);

           final int threadId = i;
           Thread t = new Thread(() -> {
               for (int j = 0; j < ITERATIONS; j++) {
               lock.lock(threadId);
               try {
                   System.out.println("Thread " + threadId + " is in critical section");
                   counter++;
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }finally{
                   lock.unlock(threadId);
               }
               } 
           });
           threads[i] = t;
           t.start();
       }

       for (Thread thread : threads) {
           try {
               thread.join();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       
       System.out.println("Expected counter value: " + (n * ITERATIONS) + ", Actual counter value: " + counter);
       if (counter != n * ITERATIONS) {
           throw new AssertionError("The lock did not provide mutual exclusion.");
       }
    }
    
}
