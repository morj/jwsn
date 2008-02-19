package ru.amse.nikitin.tests;

import junit.framework.TestCase;
import ru.amse.nikitin.activeobj.impl.Time;

public class TestTime extends TestCase {
	public void testTime() {
		Time T1 = new Time();
		Time T2 = new Time();
		T2.tick();
		if (T2.compareTo(T1) != 1) {
			fail();
		}
		T1.tick();
		if (T2.compareTo(T1) != 0) {
			fail();
		}
	}
}
