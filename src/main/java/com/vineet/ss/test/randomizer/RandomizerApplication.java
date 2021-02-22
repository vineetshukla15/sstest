package com.vineet.ss.test.randomizer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vineet.ss.test.dtq.ClientProxyProcess;
import com.vineet.ss.test.dtq.DistributedQueue;
import com.vineet.ss.test.randomizer.companion.Generator;
import com.vineet.ss.test.randomizer.companion.PrimeResult;
import com.vineet.ss.test.randomizer.companion.Receiver;

public class RandomizerApplication {
	private DistributedQueue<Integer, PrimeResult> queue;

	public RandomizerApplication() {
		this(15678);
	}

	public RandomizerApplication(int randomizerPortNumber) {
		queue = new DistributedQueue<>(new RandomizerProxy(), randomizerPortNumber);
	}

	public void run() {
		new Thread(new Generator(queue)).start();
		new Thread(new Receiver(queue)).start();
		queue.start();
	}

	
	public static class RandomizerProxy implements ClientProxyProcess<Integer, PrimeResult> {
		private int origInput = 0;

		@Override
		public void write(DataOutputStream out, Integer number) {
			this.origInput = number;
			try {
				out.writeInt(number);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public PrimeResult read(DataInputStream in) {
			try {
				int number = in.readInt();
				boolean isPrime = in.readBoolean();

				if (number != origInput) {
					throw new RuntimeException("verification check failed");
				}
				return new PrimeResult(number, isPrime);

			} catch (IOException e) {
				throw new RuntimeException("Unexpected exception occured" +e);
			}
		}
	}
}
