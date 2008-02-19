package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.IActiveObject;
import ru.amse.nikitin.activeobj.impl.*;
import ru.amse.nikitin.sensnet.IBattery;
import ru.amse.nikitin.sensnet.IMotModuleFactory;

public class Mot implements IActiveObject {
	protected Dispatcher s;
	protected int id, x, y, lastMessageID, lastMessageSource, lastMessageDest;
	protected double transmitterpower;
	protected double threshold;

	protected MotModule mac;
	private MotModule net;
	protected MotModule app;
	
	protected IBattery b = new Battery (100000000);
	
	protected MotDescription description;
	
	public Mot(int x_, int y_,
			double power, double threshold, IMotModuleFactory f) {
		x = x_; y = y_;
		s = Dispatcher.getInstance();
		transmitterpower = power;
		this.threshold = threshold; 
		app = f.App(this);
		net = f.Net(this);
		mac = f.Mac(this);
		app.SetNeghbours(null, net);
		net.SetNeghbours(app, mac);
		mac.SetNeghbours(net, null);
		description = new MotDescription(new ImageIcon("noicon.png"), "Mot", x, y);
	}
	
	public double squaredDistanceTo(Mot m) {
		return((x-m.x)*(x-m.x) + (y-m.y)*(y-m.y));
	}
	
	public boolean recieveMessage(IMessage m) {
		lastMessageID = m.getID();
		lastMessageSource = m.getSource();
		lastMessageDest = m.getDest();
		switch (m.getType()) {
			case TIMER:
				int id = m.getID();
				// System.out.println(id + " recieved");
				app.fireEvent(id);
				net.fireEvent(id);
				mac.fireEvent(id);
				return true;
			case INIT:
				/* sheduleEvent(new Runnable() {
					public void run() {
						System.out.println("PREVED");
					}
				}, Time.randTime(5));
				int[] t = new int [2];
				t[1] = 0;
				app.sendMessage(t); */
				mac.init(s.getTopology());
				net.init(s.getTopology());
				app.init(s.getTopology());
				return true;
			default:
				if (b.drain()) {
					return mac.recieveMessage(new Packet(m.getData(), 0));
				} else {
					return false;
				}
		}
	}
	
	/* package-private */ void scheduleMessage(IMessage msg, Time t) {
		s.scheduleMessage(msg, t);
	}
	
	public boolean sendMessage(IMessage m) {
		if (b.drain()) {
			// m.setDest(0);
			// m.setData(null);
			// Logger.getInstance().logMessage(m.toString());
			return s.sendMessage(m);
		} else {
			return false;
		}
	}
	
	public IMessage allocateMessage(IActiveObject obj) {
		return s.allocateMessage(obj);
	}
	
	public int getLastMessageID() {
		return lastMessageID;
	}
	
	public int getLastMessageSource() {
		return lastMessageSource;
	}
	
	public int getLastMessageDest() {
		return lastMessageDest;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int newID) {
		id = newID;
	}
	
	public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
		description = new MotDescription(image, name, x, y);
		return description; 
	}
	
	public IActiveObjectDesc getDesc() {
		return description;
	}
	
	public double getTransmitterPower() {
		return transmitterpower;
	}
	
	public double getThreshold() {
		return threshold;
	}
}
