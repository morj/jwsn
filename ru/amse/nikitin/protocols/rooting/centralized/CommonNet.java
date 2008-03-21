package ru.amse.nikitin.protocols.rooting.centralized;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;

public class CommonNet extends MotModule {
	public static final int BAD_PRED = -201;
	protected int pred = BAD_PRED;
	
	public CommonNet(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IWirelessPacket m) {
		int lastdest = mot.getLastMessageDest();
		if ((lastdest == mot.getID()) || (lastdest == -1)) {
			if (m.isEncapsulating()) { // data
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				NetData data = (NetData)m.getData();
				pred = data.getPredecessor(mot.getID());
				// System.out.println("pred of " + p.getID() + " = " + pred);
				return true;
			}
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		if (pred == BAD_PRED) {
			return false;
		} else {
			IWirelessPacket msg = new WirelessPacket(pred);
			msg.encapsulate(m);
			return getGate("lower").recieveMessage(msg, this);
		}
	}
}
