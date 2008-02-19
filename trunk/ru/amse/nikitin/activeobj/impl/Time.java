package ru.amse.nikitin.activeobj.impl;

import java.util.Random;

/**
 * @author Pavel A. Nikitin
 * Time
 *
 */
public class Time implements Comparable {
	protected long steps = 0;
	protected static Random randomizer = new Random();
	protected static Time randtime = new Time();
	public Time() {
		steps = 0;
	}
	public Time(long i) {
		steps = i;
	}
	public Time(Time t) {
		steps = t.steps + 1;
	}
	public long getTime() {
		return steps;
	}
	public void copyFrom(Time t) {
		steps = t.steps + 1;
	}
	public void addFrom(Time t) {
		steps += t.steps;
	}
	public int compareTo(Object t) {
		long tvs = ((Time)t).steps;
		return (steps > tvs ? 1 : (steps == tvs ? 0 : -1));
	}
	public void tick() {
		steps++;
	}
	public static Time randTime (long max) {
		long r = randomizer.nextInt() % max;
		if (r < 0) r = -r;
		randtime.steps = r;
		return randtime;
	}
	public String toString() {
		return Long.valueOf(steps).toString();
	}
}
