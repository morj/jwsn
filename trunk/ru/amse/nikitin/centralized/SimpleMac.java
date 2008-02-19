package ru.amse.nikitin.centralized;

import java.util.*;

import ru.amse.nikitin.activeobj.EMessageType;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.impl.Packet;


public class SimpleMac extends MotModule {
	protected Queue<IPacket> pending = new LinkedList<IPacket>();
	protected Time slotTime = null;
	protected Time waitTime = null;
	
	final Runnable step = new Runnable() {
		public void run () {
			if (!pending.isEmpty()) sendNextMessage(); // sending one next message
			scheduleEvent(this, waitTime);
		}
	};
	public SimpleMac(Mot m) {
		super(m);
	}
	public boolean recieveMessage(IPacket m) {
		int lastdest = mot.getLastMessageDest();
		if ((lastdest == mot.getID()) || (lastdest == -1)) {
			if (m.isEncapsulating()) { // data
				return upper.recieveMessage(m.decapsulate());
			} else { // hop
				int[] timings = m.getData();
				int id = mot.getID();
				waitTime = new Time(timings[0]);
				slotTime = new Time(timings[2 * id + 1]);
				
				IPacket routingInfo = new Packet(id);
				int[]data = new int[1];
				data[0] = timings[2 * id + 2]; // rooting pred.
				// System.out.println("pred for " + p.getID() + " = " + data[0]);
				routingInfo.setData(data);
				
				scheduleEvent(step, slotTime);
				return upper.recieveMessage(routingInfo);
			}
		} else {
			return false;
		}
	}
	public boolean sendMessage(IPacket m) {
		IPacket msg = new Packet(m.getID());
		msg.encapsulate(m);
		return pending.add(msg);
	}
	public void init(IGraph<Integer> topology) {
	}
	private boolean sendNextMessage() {
		IPacket mmsg = pending.remove();
		IMessage msg = mot.allocateMessage(mot);
		msg.setType(EMessageType.DATA);
		msg.setDest(mmsg.getID());
		int[] data = new int[mmsg.getLength()];
		mmsg.toIntArr(data, 0);
		msg.setData(data);
		return mot.sendMessage(msg);
	}
}
