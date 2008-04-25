package ru.amse.nikitin.protocols.routing.distributed;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.impl.Time;

public class CommonNet extends MotModule {
	public static final int BAD_PRED = -201;
	protected int pred = BAD_PRED;
	private final CommonNet instance;
	
	class ResendMsg implements Runnable {
		private IWirelessPacket m;
		
		public ResendMsg(IWirelessPacket m) {
			this.m = m;
		}

		public void run () {
			DistributedNetData data = (DistributedNetData)m.getData();
			IWirelessPacket p = new WirelessPacket(-1, mot);
			m.setData(data);
			getGate("lower").recieveMessage(p, instance);
		}
	}
	
	public CommonNet(Mot m) {
		super(m);
		instance = this;
	}
	public void init(IGraph<Integer> topology) {
		/* Collection<IVertex<Integer>> vertices = topology.getVertices();
		for (IVertex<Integer> v: vertices) {
			int i = v.getData();
			if (i == mot.getID()) {
				IVertex<Integer> w = v.getPredecessor();
				if (w != null) {
					pred = w.getData();
				}
			}
		} */
	}
	public boolean lowerMessage(IWirelessPacket m) {
		int lastdest = mot.getLastMessageDest();
		if ((lastdest == mot.getID()) || (lastdest == -1)) {
			if (m.isEncapsulating()) { // data
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				if (pred == BAD_PRED) {
					pred = mot.getLastMessageSource();
					System.err.println("pred of " + mot.getID() + " = " + pred);
					scheduleEvent(new ResendMsg(m), Time.randTime(4));
					return true;
				} else {
					return false;
				}
			}
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
