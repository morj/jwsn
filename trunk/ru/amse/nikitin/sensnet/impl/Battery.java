package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.IBattery;

public class Battery implements IBattery {
	private double capacity;
	
	public Battery (double cap) {
		capacity = cap;
	}
	
	public double getCapacity() {
		return capacity;
	}
	
	public boolean drain() {
		capacity -= 1;
		return capacity > 0;
	}
}
