package ru.amse.nikitin.sensnet.impl;

import java.util.HashMap;
import java.util.Map;

import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.sensnet.IMotModule;
import ru.amse.nikitin.net.impl.NetModule;

public class MotModule extends NetModule implements IMotModule {
	protected Mot mot;
	protected Map<Integer, Runnable> events = new HashMap<Integer, Runnable>();
	
	public MotModule(Mot m) {
		mot = m;
	}
	
	protected void scheduleEvent(Runnable r, int t) {
		IMessage msg = mot.newMessage();
		Integer id = msg.getID();
		// System.err.println(id + " allocated");
		assert events.containsKey(id);
		mot.scheduleMessage(msg, new Time(t));
		events.put(id, r);
	}

	protected void scheduleEvent(Runnable r, Time t) {
		IMessage msg = mot.newMessage();
		Integer id = msg.getID();
		// System.out.println(id + " allocated");
		assert events.containsKey(id);
		mot.scheduleMessage(msg, t);
		events.put(id, r);
	}

	protected boolean lowerMessage(IWirelessPacket m) { return false; }
	protected boolean upperMessage(IWirelessPacket m) { return false; }
	
	/* package-private */ void fireEvent(int id) {
		if (events.containsKey(id)) {
			Runnable r = events.remove(id);
			r.run();
		}
	}
	
	public boolean recieveMessage(IPacket m) {
		if (arrivedOn.equals("upper")) {
			return upperMessage((IWirelessPacket)m);
		}
		if (arrivedOn.equals("lower")) {
			return lowerMessage((IWirelessPacket)m);
		}
		System.err.println("bad gate");
		return false;
	}
	
	public void init(IGraph<Integer> topology) {
	}

}
