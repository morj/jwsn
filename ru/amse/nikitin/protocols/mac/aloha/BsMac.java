package ru.amse.nikitin.protocols.mac.aloha;

import java.util.LinkedList;
import java.util.Queue;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class BsMac extends MotModule {
	protected Queue<IWirelessPacket> pending = new LinkedList<IWirelessPacket>();
	
	final Runnable step = new Runnable() {
		public void run () {
			if (!pending.isEmpty()) sendNextMessage(); // sending one next message
			scheduleEvent(this, 0);
		}
	};
	public BsMac(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IWirelessPacket m) {
		if (mot.getLastMessageDest() == mot.getID()) {
			if (m.isEncapsulating()) {
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(m.getID(), mot);
		msg.encapsulate(m);
		return pending.add(msg);
	}
	public void init(IGraph<Integer> topology) {
		step.run();
	}
	private boolean sendNextMessage() {
		IWirelessPacket mmsg = pending.remove();
		return getGate("lower").recieveMessage(mmsg, this);
	}
}
