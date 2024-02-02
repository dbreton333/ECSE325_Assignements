package ca.mcgill.ecse420.a1;

import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {

    public static ReentrantLock lock1 = new ReentrantLock(true);
    public static ReentrantLock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        Thread thread1 = new Thread(()-> {
            lock1.lock();
            System.out.println("Thread 1: Locked Resource 1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock2.lock();
            //should never reach here
            System.out.println("Thread 1: Locked Resource 2");


        }
        );
        Thread thread2 = new Thread(()-> {
            lock2.lock();
            System.out.println("Thread 2: Locked Resource 2");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock1.lock();
            //should never reach here
            System.out.println("Thread 2: Locked Resource 1");
        });

        thread1.start();
        thread2.start();
    }
    
}

