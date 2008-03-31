package ru.amse.nikitin.protocols.app;

import ru.amse.nikitin.sensnet.impl.MonitoredPacket;
import ru.amse.nikitin.sensnet.impl.MonitoredObject;
import ru.amse.nikitin.sensnet.impl.MonitoredObjectModule;
import ru.amse.nikitin.simulator.impl.Time;

public class CarObject extends MonitoredObjectModule {
	protected int helloCount = 1;
	protected final static Time oneUnitTime = new Time(0);
	
	Runnable r = new Runnable() {
		public void run() {
			sensing.setX(sensing.getX() + 10);
			sensing.setY(sensing.getY() + 5);
			// System.out.println("SETTING");
			scheduleEvent(this, oneUnitTime); // resend
			MonitoredPacket p = new MonitoredPacket(sensing, 1);
			CarData d = new CarData(sensing.getX(), sensing.getY());
			p.setData(d);
			sendMsgToLower(p);
		}
	};

	public CarObject(MonitoredObject sensing) {
		super(sensing);
	}

	public Object getReading() {
		return null;
	}

	public void init() {
		scheduleEvent(r, oneUnitTime);
	}
	
	protected boolean sendMsgToLower(MonitoredPacket packet) {
		return getGate("output").recieveMessage(packet, this);
	}

}
