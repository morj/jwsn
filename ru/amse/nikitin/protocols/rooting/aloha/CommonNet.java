package ru.amse.nikitin.protocols.rooting.aloha;

import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.graph.IVertex;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import java.util.Collection;

public class CommonNet extends MotModule {
	public static final int BAD_PRED = -201;
	protected int pred = BAD_PRED;
	
	public CommonNet(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		Collection<IVertex<Integer>> vertices = topology.getVertices();
		for (IVertex<Integer> v: vertices) {
			int i = v.getData();
			if (i == mot.getID()) {
				IVertex<Integer> w = v.getPredecessor();
				if (w != null) {
					pred = w.getData();
				}
			}
		}
	}
	public boolean lowerMessage(IWirelessPacket m) {
		if (m.getID() == mot.getID()) {
			return getGate("upper").recieveMessage(m.decapsulate(), this);
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		if (pred == BAD_PRED) {
			System.err.println("bad pred");
			return false;
		} else {
			IWirelessPacket msg = new WirelessPacket(pred, mot);
			msg.encapsulate(m);
			return getGate("lower").recieveMessage(msg, this);
		}
	}
}
