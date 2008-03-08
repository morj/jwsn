package ru.amse.nikitin.activeobj.impl;

public final class MessageInitData {
	private final Time t;
	private final int i;
	/* package-private */ MessageInitData(Time t, int i) {
		this.t = t;
		this.i = i;
	}
	/* package-private */ int getI() {
		return i;
	}
	/* package-private */ Time getT() {
		return t;
	}
	
}
