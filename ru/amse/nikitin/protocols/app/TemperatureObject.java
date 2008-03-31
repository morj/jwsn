package ru.amse.nikitin.protocols.app;

import ru.amse.nikitin.sensnet.impl.MonitoredObject;
import ru.amse.nikitin.sensnet.impl.MonitoredObjectModule;
import ru.amse.nikitin.simulator.impl.Time;

public class TemperatureObject extends MonitoredObjectModule {
	protected int helloCount = 1;
	protected final static Time oneUnitTime = new Time(0);
	
	Runnable r = new Runnable() {
		public void run() {
			System.out.println("SETTING");
			sensing.setX(sensing.getX() + 10);
			sensing.setY(sensing.getY() + 5);
			scheduleEvent(this, oneUnitTime); // resend
		}
	};

	public TemperatureObject(MonitoredObject sensing) {
		super(sensing);
	}

	public Object getReading() {
		return new BsData(Const.hello, ++helloCount);
	}

	public void init() {
		scheduleEvent(r, oneUnitTime);
	}

}
