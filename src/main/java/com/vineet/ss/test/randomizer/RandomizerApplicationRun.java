package com.vineet.ss.test.randomizer;

public class RandomizerApplicationRun {
	private static final int SERVER_PORT = 15678; // can be configured

	public static void main(String[] args) {
		new RandomizerApplication(SERVER_PORT).run();
	}
}
