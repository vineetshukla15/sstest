package com.vineet.ss.test.prime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class PrimeNumberCheckSimpleTest {

	private int[] samplePrime = new int[] { 2, 3, 5, 7, 11, 13, 17 };
	private Set<Integer> primeSet = new TreeSet<Integer>();
	{
		for (int i = 0; i < samplePrime.length; i++) {
			primeSet.add(samplePrime[i]);
		}
	}

	@Test
	public void testAllUnder10Primes() {
		assertFalse("0 is not a prime number", NumberUtils.isPrime(0));
		assertFalse("1 is not a prime number", NumberUtils.isPrime(1));
		assertTrue("2 is a prime number", NumberUtils.isPrime(2));
		assertTrue("3 is a prime number", NumberUtils.isPrime(3));
		assertFalse("4 is not a prime number", NumberUtils.isPrime(4));
	}

}
