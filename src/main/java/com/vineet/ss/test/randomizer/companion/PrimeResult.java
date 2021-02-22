package com.vineet.ss.test.randomizer.companion;

public class PrimeResult {
	private final int number;
	private final boolean isPrime;
	
	public PrimeResult(int number, boolean isPrime) {
		this.number = number;
		this.isPrime = isPrime;
	}

	public int getNumber() {
		return number;
	}

	public boolean isPrime() {
		return isPrime;
	}
}
