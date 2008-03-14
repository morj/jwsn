package ru.amse.nikitin.centralized;

import java.util.*;

import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;

public class SimpleMac extends MotModule {
	protected Queue<IWirelessPacket> pending = new LinkedList<IWirelessPacket>();
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
	public boolean lowerMessage(IWirelessPacket m) {
		int lastdest = mot.getLastMessageDest();
		if ((lastdest == mot.getID()) || (lastdest == -1)) {
			if (m.isEncapsulating()) { // data
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else { // hop
				int[] timings = (int[])m.getData();
				int id = mot.getID();
				waitTime = new Time(timings[0]);
				slotTime = new Time(timings[2 * id + 1]);
				
				IWirelessPacket routingInfo = new WirelessPacket(id);
				// int[]data = new int[1];
				// data[0] = timings[2 * id + 2]; // rooting pred.
				NetData data = new NetData(timings[2 * id + 2]);
				// System.out.println("pred for " + p.getID() + " = " + data[0]);
				routingInfo.setData(data);
				
				scheduleEvent(step, slotTime);
				return getGate("upper").recieveMessage(routingInfo, this);
			}
		} else {
			return false;
		}
	}
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(m.getID());
		msg.encapsulate(m);
		return pending.add(msg);
	}
	public void init(IGraph<Integer> topology) {
	}
	private boolean sendNextMessage() {
		IWirelessPacket mmsg = pending.remove();
		return getGate("lower").recieveMessage(mmsg, this);
	}
}
