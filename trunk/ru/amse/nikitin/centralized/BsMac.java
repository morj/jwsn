package ru.amse.nikitin.centralized;

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
		int[] data = new int[2 * count + 1];
		data[0] = topology.getColorsCount();
		for (IVertex<Integer> v: vertices) {
			int i = v.getData();
			data [2 * i + 1] = v.getColor();
			IVertex<Integer> u = v.getPredecessor();
			if (u == null) {
				data [2 * i + 2] = -1;
			} else {
				data [2 * i + 2] = v.getPredecessor().getData();
			}
		}
		IWirelessPacket msg = new WirelessPacket(-1);
		msg.setData(data);
		pending.add(msg);
		step.run();
	}
	private boolean sendNextMessage() {
		IWirelessPacket mmsg = pending.remove();
		return getGate("lower").recieveMessage(mmsg, this);
	}
}
