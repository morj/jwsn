package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.IMotGenerator;

public abstract class MotGenerator implements IMotGenerator {
	
	protected void connectModules(Mot m, MotModule mac, MotModule net, MotModule app) {
		m.addModule("mac", mac);
		m.addModule("net", net);
		m.addModule("app", app);
		
		m.createTopology();
	}
	
	public Mot generateMot(int x, int y, double power, double threshold) {
		return null;
	}
	
}
