package com.vineet.ss.test.prime;

public class PrimeApplicationRun {
	private static final int SERVER_PORT = 15679;

	public static void main(String[] args) {
		new PrimeApplication(SERVER_PORT).run();
	}
}
