package com.vineet.ss.test;

import com.vineet.ss.test.prime.PrimeApplicationRun;
import com.vineet.ss.test.randomizer.RandomizerApplicationRun;

public class RunTestMultiJvmSetup {
	
	public static void main(String[] args) {
		
		//Run Server
		runJava(RandomizerApplicationRun.class);

		// spawn few java client
		runJava(PrimeApplicationRun.class);
		runJava(PrimeApplicationRun.class);
	}
	
	private static void runJava(Class<?> classToRun) {
		new Thread(() -> {
			runJava_(classToRun);
		}).start();
	}

	private static void runJava_(Class<?> classToRun) {
		String classpath = System.getProperty("java.class.path");
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(
				"java",
				"-cp", classpath,
				classToRun.getName())
			.inheritIO();
			Process process = processBuilder.start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
