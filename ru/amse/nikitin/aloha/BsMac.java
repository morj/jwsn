package ru.amse.nikitin.aloha;

import java.util.*;

import ru.amse.nikitin.activeobj.EMessageType;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.impl.Packet;

public class BsMac extends MotModule {
	protected Queue<IPacket> pending = new LinkedList<IPacket>();
	
	final Runnable step = new Runnable() {
		public void run () {
			if (!pending.isEmpty()) sendNextMessage(); // sending one next message
			scheduleEvent(this, 0);
		}
	};
	public BsMac(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IPacket m) {
		if (mot.getLastMessageDest() == mot.getID()) {
			return getGate("upper").recieveMessage(m.decapsulate(), this);
		} else {
			return false;
		}
	}
	public boolean upperMessage(IPacket m) {
		IPacket msg = new Packet(m.getID());
		msg.encapsulate(m);
		return pending.add(msg);
	}
	public void init(IGraph<Integer> topology) {
		step.run();
	}
	private boolean sendNextMessage() {
		IPacket mmsg = pending.remove();
		IMessage msg = mot.allocateMessage(mot);
		if (msg.getID() >= 0) {
			msg.setType(EMessageType.DATA);
		} else {
			msg.setType(EMessageType.BROADCAST);
		}
		msg.setDest(mmsg.getID());
		int[] data = new int[mmsg.getLength()];
		mmsg.toIntArr(data, 0);
		msg.setData(data);
		return mot.sendMessage(msg);
	}
}
