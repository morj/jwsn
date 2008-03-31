package ru.amse.nikitin.protocols.app;

import ru.amse.nikitin.sensnet.impl.MonitoredObject;
import ru.amse.nikitin.sensnet.impl.MonitoredObjectModule;

public class TemperatureObject extends MonitoredObjectModule {
	protected int helloCount = 1;

	public TemperatureObject(MonitoredObject sensing) {
		super(sensing);
	}

	public Object getReading() {
		return new BsData(Const.hello, ++helloCount);
	}

}
