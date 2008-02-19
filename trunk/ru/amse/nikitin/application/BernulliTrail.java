package ru.amse.nikitin.application;

import java.util.Random;

public class BernulliTrail {
	protected int numerator;
	protected int denominator;
	
	protected static final Random randomizer = new Random();
	
	public BernulliTrail(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public boolean toss() {
		int t = randomizer.nextInt() % denominator;
		return t > numerator;
	}

}
