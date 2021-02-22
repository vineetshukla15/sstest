package com.vineet.ss.test;

import com.vineet.ss.test.prime.PrimeApplicationRun;
import com.vineet.ss.test.randomizer.RandomizerApplicationRun;


public class RunTestSetup {
	public static void main(String[] args) {
		// run server (in-process)
		RandomizerApplicationRun.main(args);
		
		// run 5 clients (all also in-process)
		PrimeApplicationRun.main(args);
		PrimeApplicationRun.main(args);
		
	}
}
