package ca.mcgill.ecse420.a2;
import java.util.*;


public class FilterLock {
    List<Integer> level;
    List<Integer> victim;
    int n;

    public FilterLock(int n) {
        this.level =  Collections.synchronizedList(new ArrayList<Integer>());
        this.victim = Collections.synchronizedList(new ArrayList<Integer>());
 
        this.n = n;
        for (int i = 0; i < n; i++) {
            level.add(0);
            victim.add(0);
        }
    }

    public void lock(int threadId) {
        int i = threadId;
        for (int L = 1; L < this.n; L++) {
            level.set(i, L);
            victim.set(L, i);
            boolean conflict;
            do {
                conflict = false;
                for (int k = 0; k < n; k++) {
                    if (k != i && level.get(k) >= L && victim.get(L) == i) {
                        conflict = true;
                        break;
                    }
                }
            } while (conflict);
            
        }
    }

    public void unlock(int threadId) {
        int i = threadId;
        level.set(i, 0);
    }


    public static void main(String[] args) {
        int n = 5;
        FilterLock lock = new FilterLock(n);
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


