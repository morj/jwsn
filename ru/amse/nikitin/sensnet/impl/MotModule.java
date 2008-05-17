package ru.amse.nikitin.sensnet.impl;

import java.util.HashMap;
import java.util.Map;

import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.impl.NetModule;
import ru.amse.nikitin.sensnet.Const;
import ru.amse.nikitin.sensnet.IMotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.IMonitoredPacket;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Time;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class MotModule extends NetModule implements IMotModule {
	protected Mot mot;
	protected Map<Integer, Runnable> events = new HashMap<Integer, Runnable>();
	
	public MotModule(Mot mot) {
		this.mot = mot;
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
	
	/* package-private */ void fireEvent(int id) {
		if (events.containsKey(id)) {
			Runnable r = events.remove(id);
			r.run();
		}
	}

	protected boolean lowerMessage(IWirelessPacket m) { return false; }
	protected boolean upperMessage(IWirelessPacket m) { return false; }
	protected boolean sensingMessage(IMonitoredPacket m) { return false; }
	
	public boolean recieveMessage(IPacket m) {
		if (arrivedOn.equals(Const.upperGateName)) {
			return upperMessage((IWirelessPacket)m);
		}
		if (arrivedOn.equals(Const.lowerGateName)) {
			return lowerMessage((IWirelessPacket)m);
		}
		if (arrivedOn.equals(Const.sensingGateName)) {
			return sensingMessage(((IMonitoredPacket)m));
		}
		//System.err.println("bad gate");
		return false;
	}
	
	public void init(IGraph<Integer> topology) {
	}

}
