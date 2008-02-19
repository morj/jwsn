package ru.amse.nikitin.centralized;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.impl.Packet;

public class CommonNet extends MotModule {
	protected int pred = 0;
	
	public CommonNet(Mot m) {
		super(m);
	}
	public boolean recieveMessage(IPacket m) {
		if (m.getID() == mot.getID()) {
			if (m.isEncapsulating()) { // data
				return upper.recieveMessage(m.decapsulate());
			} else {
				pred = m.getData()[0];
				// System.out.println("pred of " + p.getID() + " = " + pred);
				return true;
			}
		} else {
			return false;
		}
	}
	public boolean sendMessage(IPacket m) {
		IPacket msg = new Packet(pred);
		msg.encapsulate(m);
		return lower.sendMessage(msg);
	}
}
