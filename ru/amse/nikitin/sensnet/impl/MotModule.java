package ru.amse.nikitin.sensnet.impl;

import java.util.HashMap;
import java.util.Map;

import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.IMotModule;
import ru.amse.nikitin.sensnet.IPacket;

public class MotModule implements IMotModule {
	protected IMotModule upper;
	protected IMotModule lower;
	protected Mot mot;
	
	protected Map<Integer, Runnable> events = new HashMap<Integer, Runnable>();
	
	public MotModule(Mot m) {
		mot = m;
	}
	
	public void setNeghbours(IMotModule u, IMotModule l) {
		upper = u; // Reciever
		lower = l; // Sender
	}
	
	public boolean recieveMessage(IPacket m) {
		return false;
	}
	
	public boolean sendMessage(IPacket m) {
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
