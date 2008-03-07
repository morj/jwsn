package ru.amse.nikitin.sensnet.impl;

import java.util.HashMap;
import java.util.Map;

import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.IGate;
import ru.amse.nikitin.sensnet.IMotModule;
import ru.amse.nikitin.sensnet.IPacket;

public class MotModule implements IMotModule {
	// protected IMotModule upper; protected IMotModule lower;
	protected Mot mot;
	
	protected Map<Integer, Runnable> events = new HashMap<Integer, Runnable>();
	protected Map<String, IGate> gates = new HashMap<String, IGate>();
	
	/* package-private */ String arrivedOn;
	
	public MotModule(Mot m) {
		mot = m;
	}
	
	/* public void setNeghbours(IMotModule u, IMotModule l) {
		upper = u; // Reciever
		lower = l; // Sender
	} */
	
	public boolean lowerMessage(IPacket m) { return false; }
	public boolean upperMessage(IPacket m) { return false; }
	
	public boolean recieveMessage(IPacket m) {
		if (arrivedOn.equals("upper")) {
			return upperMessage(m);
		}
		if (arrivedOn.equals("lower")) {
			return lowerMessage(m);
		}
		System.err.println("bad gate");
		return false;
	}
	
	protected void scheduleEvent(Runnable r, int t) {
		IMessage msg = mot.allocateMessage(mot);
		Integer id = msg.getID();
		// System.out.println(id + " allocated");
		assert events.containsKey(id);
		mot.scheduleMessage(msg, new Time(t));
		events.put(id, r);
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

	protected void scheduleEvent(Runnable r, Time t) {
		IMessage msg = mot.allocateMessage(mot);
		Integer id = msg.getID();
		// System.out.println(id + " allocated");
		assert events.containsKey(id);
		mot.scheduleMessage(msg, t);
		events.put(id, r);
	}
	
	/* package-private */ public void fireEvent(int id) {
		if (events.containsKey(id)) {
			Runnable r = events.remove(id);
			r.run();
		}
	}
	
	/* public int getID() {
		return p.getID();
	}
	public void setID(int newID) {
	} */
	
	public void init(IGraph<Integer> topology) {
	}
}
