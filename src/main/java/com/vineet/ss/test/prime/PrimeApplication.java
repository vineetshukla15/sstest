package com.vineet.ss.test.prime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vineet.ss.test.dtq.ClientApplication;

public class PrimeApplication extends ClientApplication {

	public PrimeApplication(int serverPortNumber) {
		super(serverPortNumber);
	}

	@Override
	public void process(DataInputStream in, DataOutputStream out) throws InterruptedException {
		try {
			int testedNumber = in.readInt();
			boolean isPrime = NumberUtils.isPrime(testedNumber);
			out.writeInt(testedNumber);
			out.writeBoolean(isPrime);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
