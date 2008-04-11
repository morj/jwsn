package ru.amse.nikitin.protocols.app;

import java.util.Random;

import ru.amse.nikitin.sensnet.impl.MonitoredPacket;
import ru.amse.nikitin.sensnet.impl.MonitoredObject;
import ru.amse.nikitin.sensnet.impl.MonitoredObjectModule;
import ru.amse.nikitin.simulator.impl.Time;

public class CarObject extends MonitoredObjectModule {
	protected static final int maxX = 600;
	protected static final int maxY = 800;
	protected static final int speed = 10;
	protected static final Random randomizer = new Random();
	
	protected int helloCount = 1;
	protected int stepsMax;
	protected int stepsTotal;
	protected int oldX;
	protected int oldY;
	protected int newX;
	protected int newY;
	protected int offsetX;
	protected int offsetY;
	protected final static Time oneUnitTime = new Time(0);
	
	Runnable r = new Runnable() {
		public void run() {
			if (stepsTotal <= stepsMax) {
				stepsTotal++;
				sensing.setX(oldX + (offsetX * stepsTotal) / stepsMax);
				sensing.setY(oldY + (offsetY * stepsTotal) / stepsMax);
			} else {
				newWaypoint();
			}
			// System.out.println("SETTING");
			scheduleEvent(this, oneUnitTime); // resend
			MonitoredPacket p = new MonitoredPacket(sensing, 1);
			CarData d = new CarData(sensing.getX(), sensing.getY());
			p.setData(d);
			sendMsgToLower(p);
		}
	};
	
	protected void newWaypoint() {
		oldX = newX;
		oldY = newY;
		newX = randomizer.nextInt(maxX);
		newY = randomizer.nextInt(maxY);
		int dx = newX - oldX;
		int dy = newY - oldY;
		int len = (int)Math.sqrt(dx*dx + dy*dy);
		stepsMax = len / speed;
		stepsTotal = 0;
		offsetX = newX - oldX;
		offsetY = newY - oldY;
	}

	public CarObject(MonitoredObject sensing) {
		super(sensing);
	}

	public Object getReading() {
		return null;
	}

	public void init() {
		newX = sensing.getX();
		newY = sensing.getY();
		newWaypoint();
		scheduleEvent(r, oneUnitTime);
	}
	
	protected boolean sendMsgToLower(MonitoredPacket packet) {
		return getGate("output").recieveMessage(packet, this);
	}

}
