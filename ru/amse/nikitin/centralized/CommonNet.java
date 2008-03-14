package ru.amse.nikitin.centralized;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;

public class CommonNet extends MotModule {
	protected int pred = 0;
	
	public CommonNet(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IWirelessPacket m) {
		if (m.getID() == mot.getID()) {
			if (m.isEncapsulating()) { // data
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				NetData data = (NetData)m.getData();
				pred = data.getPredecessor();
				// System.out.println("pred of " + p.getID() + " = " + pred);
				return true;
			}
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(pred);
		msg.encapsulate(m);
		return getGate("lower").recieveMessage(msg, this);
	}
}
