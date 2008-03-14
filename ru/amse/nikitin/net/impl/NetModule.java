package ru.amse.nikitin.net.impl;

import java.util.HashMap;
import java.util.Map;

import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.IModule;

public class NetModule implements IModule {
	protected Map<String, IGate> gates = new HashMap<String, IGate>();
	protected String arrivedOn;
	
	public boolean recieveMessage(IPacket m) {
		return false;
	}
	
	public IGate declareGate(String name) {
		IGate newGate = null;
		if (!gates.containsKey(name)) {
			newGate = new Gate(this, name);
			gates.put(name, newGate);
		}
		return newGate;
	}

	public IGate getGate(String name) {
		return gates.get(name);
	}

	public void setArrivedOn(String arrivedOn) {
		this.arrivedOn = arrivedOn;
	}
}
