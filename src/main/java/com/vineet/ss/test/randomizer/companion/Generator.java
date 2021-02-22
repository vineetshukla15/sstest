package com.vineet.ss.test.randomizer.companion;

import java.util.Random;

import com.vineet.ss.test.dtq.DistributedQueue;

/**
 * Generate inputs for distributed Prime checkers
 */
public class Generator implements Runnable {
	private final Random randomGenerator = new Random();
	private DistributedQueue<Integer, PrimeResult> queue;

	public Generator(DistributedQueue<Integer, PrimeResult> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			int randomInt = Math.abs(randomGenerator.nextInt(Integer.MAX_VALUE));
			try {
				queue.put(randomInt);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}