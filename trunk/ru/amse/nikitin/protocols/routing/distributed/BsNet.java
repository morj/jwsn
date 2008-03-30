package ru.amse.nikitin.protocols.routing.distributed;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class BsNet extends MotModule {
	public static final int BAD_PRED = -201;
	protected int pred = BAD_PRED;
	
	public BsNet(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		IWirelessPacket m = new WirelessPacket(-1, mot);
		m.setData(new DistributedNetData());
		getGate("lower").recieveMessage(m, this);
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
