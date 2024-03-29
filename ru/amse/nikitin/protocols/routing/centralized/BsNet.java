package ru.amse.nikitin.protocols.routing.centralized;

import java.util.Collection;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.util.graph.IVertex;

public class BsNet extends MotModule {
	protected int pred = 0;
	
	public BsNet(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IWirelessPacket m) {
		if (m.getDest() == mot.getID()) {
			if (m.isEncapsulating()) { // data
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(pred, mot);
		msg.encapsulate(m);
		return getGate("lower").recieveMessage(msg, this);
	}
	public void init(IGraph<Integer> topology) {
		// topology.solvePaths(p.getID());
		topology.solveColors();
		Collection<IVertex<Integer>> vertices = topology.getVertices();
		int count = vertices.size();
		int[] data = new int[count];
		for (IVertex<Integer> v: vertices) {
			int i = v.getData();
			IVertex<Integer> u = v.getPredecessor();
			if (u == null) {
				data [i] = -1;
			} else {
				data [i] = v.getPredecessor().getData();
			}
		}
		pred = data[mot.getID()];
		IWirelessPacket m = new WirelessPacket(-1, mot);
		m.setData(new CentralizedNetData(data));
		getGate("lower").recieveMessage(m, this);
	}
}
