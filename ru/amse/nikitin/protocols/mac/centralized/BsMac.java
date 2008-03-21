package ru.amse.nikitin.protocols.mac.centralized;

import java.util.*;

import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.graph.IVertex;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;

public class BsMac extends MotModule {
	protected Queue<IWirelessPacket> pending = new LinkedList<IWirelessPacket>();
	protected final static Time oneUnitTime = new Time(0);
	
	final Runnable step = new Runnable() {
		public void run () {
			if (!pending.isEmpty()) sendNextMessage(); // sending one next message
			scheduleEvent(this, oneUnitTime);
		}
	};
	public BsMac(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IWirelessPacket m) {
		if (mot.getLastMessageDest() == mot.getID()) {
			return getGate("upper").recieveMessage(m.decapsulate(), this);
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(m.getID());
		msg.encapsulate(m);
		return pending.add(msg);
	}
	public void init(IGraph<Integer> topology) {
		// topology.solvePaths(p.getID());
		topology.solveColors();
		Collection<IVertex<Integer>> vertices = topology.getVertices();
		int count = vertices.size();
		int[] data = new int[count]; 
		for (IVertex<Integer> v: vertices) {
			int i = v.getData();
			data [i] = v.getColor() + 1; // wait one more tick for net init
		}
		IWirelessPacket msg = new WirelessPacket(-1);
		msg.setData(new MacData(topology.getColorsCount(), data));
		pending.add(msg);
		step.run();
	}
	private boolean sendNextMessage() {
		IWirelessPacket mmsg = pending.remove();
		return getGate("lower").recieveMessage(mmsg, this);
	}
}
