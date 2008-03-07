package ru.amse.nikitin.aloha;

import java.util.Collection;

import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.graph.IVertex;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.impl.Packet;

public class CommonNet extends MotModule {
	protected int pred = 0;
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
	public boolean lowerMessage(IPacket m) {
		if (m.getID() == mot.getID()) {
			return getGate("upper").recieveMessage(m.decapsulate(), this);
		} else {
			return false;
		}
	}
	public boolean upperMessage(IPacket m) {
		IPacket msg = new Packet(pred);
		msg.encapsulate(m);
		return getGate("lower").recieveMessage(msg, this);
	}
}
