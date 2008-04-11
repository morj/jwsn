package ru.amse.nikitin.protocols.routing.direct;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;

public class Net extends MotModule {

	public Net(Mot mot) {
		super(mot);
	}

	protected boolean lowerMessage(IWirelessPacket m) {
		int lastdest = mot.getLastMessageDest();
		if ((lastdest == mot.getID()) || (lastdest == -1)) {
			if (m.isEncapsulating()) { // data
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(m.getDest(), mot);
		msg.encapsulate(m);
		return getGate("lower").recieveMessage(msg, this);
	}	

}
