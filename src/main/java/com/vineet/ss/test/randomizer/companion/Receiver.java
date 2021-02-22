package com.vineet.ss.test.randomizer.companion;

import com.vineet.ss.test.dtq.DistributedQueue;

/**
 * Receive results from Prime checkers and print them
 */
public class Receiver implements Runnable {

	private DistributedQueue<Integer, PrimeResult> queue;

	public Receiver(DistributedQueue<Integer, PrimeResult> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				PrimeResult r = queue.take();
				if (r.isPrime()) {
					System.out.println(String.format("Number: %d is a prime number", r.getNumber()));
				} else {
					System.out.println(String.format("Number:  %d is NOT prime number", r.getNumber()));
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}