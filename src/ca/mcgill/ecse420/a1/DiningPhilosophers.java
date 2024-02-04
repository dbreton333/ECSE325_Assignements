package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
	
	public static void main(String[] args) {
		int numberOfPhilosophers = 20;
		Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
		Object[] chopsticks = new Object[numberOfPhilosophers];

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				for (Philosopher philosopher : philosophers) {
					System.out.println("\nPhilosopher " + philosopher.id + " has eaten for " + philosopher.totalEatingTime + "ms, " + philosopher.eatingCount + " times.");
				}
			}
		});
		

		for (int i = 0; i < numberOfPhilosophers; i++) {
			chopsticks[i] = new Object();
		}

		ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

		for (int i = 0; i < numberOfPhilosophers; i++) {
			Object rightChopstick = chopsticks[i];
			Object leftChopstick = chopsticks[(i + 1) % numberOfPhilosophers];


			//the last philosopher picks up the right chopstick first to break the circle of deadlock
			//deadlock happens when all philosophers pick up the left chopstick first
			if (i == numberOfPhilosophers - 1) {
				philosophers[i] = new Philosopher(i, rightChopstick, leftChopstick);
			} else {
				philosophers[i] = new Philosopher(i, leftChopstick, rightChopstick);
			}
			executor.execute(philosophers[i]);
		}
	}

	public static class Philosopher implements Runnable {
		private final int id;
		private final Object leftChopstick;
		private final Object rightChopstick;
		private long totalEatingTime = 0;
		private int eatingCount = 0;

		public Philosopher(int id, Object leftChopstick, Object rightChopstick) {
			this.id = id;
			this.leftChopstick = leftChopstick;
			this.rightChopstick = rightChopstick;
		}


		//by setting random sleep time for thinking and eating, we ensure that all philosophers will have a chance to eat
		//we therefore ensure that no philosopher will starve
		private void think() throws InterruptedException {
			System.out.println("Philosopher " + id + " is thinking");
			Thread.sleep((long) (Math.random() * 1000));
		}

		private void eat() throws InterruptedException {
			System.out.println("Philosopher " + id + " is eating");
			long eatingTime = (long)(Math.random() * 1000);
			this.totalEatingTime += eatingTime;
			this.eatingCount++;
			Thread.sleep((long) ( eatingTime));
		}
		

		@Override
		public void run() {
			try{
				while(true){
					think();
					synchronized(leftChopstick){
						System.out.println("Philosopher " + id + " picked up left chopstick");
						synchronized(rightChopstick){
							System.out.println("Philosopher " + id + " picked up right chopstick");
							eat();
						}
						System.out.println("Philosopher " + id + " put down right chopstick");
					}
					System.out.println("Philosopher " + id + " put down left chopstick");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

}
