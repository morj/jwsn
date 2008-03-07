package ru.amse.nikitin.aloha;

import java.util.*;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.EMessageType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.impl.Packet;


public class CommonMac extends MotModule {
	protected Queue<IPacket> pending = new LinkedList<IPacket>();
	protected Map<Integer, IPacket> waiting = new HashMap<Integer, IPacket>();
	
	protected boolean wasSent = false;
	
	final Runnable step = new Runnable() {
		public void run () {
			// System.out.println("mac step " + mot.getID());
			
			if (pending.isEmpty() || wasSent) {
				wasSent = false;
			} else {
				sendNextMessage(); // sending one next message
				wasSent = true;
			}
			
			scheduleEvent(this, 0);
		}
	};
	
	class ResendMsg implements Runnable {
		protected final int id;
		
		public ResendMsg(int id) {
			this.id = id;
		}

		public void run () {
			assert waiting.containsKey(id);
			pending.add(waiting.get(id));
			Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
				"- msg " + id + ": resubmitting");
		}
	}
	
	class CheckMsg implements Runnable {
		protected final int id;
		protected int tries = 0;
		
		public CheckMsg(int id) {
			this.id = id;
		}

		public void run() {
			tries++;
			if (!waiting.containsKey(id)) {
				Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
					"- msg " + id + " sent successfully!");
			} else { // resend
				if (tries == 3) {
					ResendMsg resendMsg = new ResendMsg(id);
					scheduleEvent(resendMsg, Time.randTime(4));
				} else {
					Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
						"- msg " + id + ": waiting for response");
					scheduleEvent(this, 0);
				}
			}
		}
	}
	
	public CommonMac(Mot m) {
		super(m);
	}
	
	public boolean lowerMessage(IPacket m) {
		if (mot.getLastMessageDest() == mot.getID()) {
			if (m.isEncapsulating()) { // data
				int[] reciever = new int [1];
				reciever[0] = mot.getLastMessageID();
				Packet confirmMsg = new Packet(mot.getLastMessageSource());
				confirmMsg.setData(reciever);
				pending.add(confirmMsg); // sending confirmation
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else { // confirm
				waiting.remove(m.getData()[0]);
				Logger.getInstance().logMessage(ELogMsgType.INFORMATION, "rem " + m.getData()[0]);
				return true;
			}
		} else {
			return false;
		}
	}
	
	public boolean upperMessage(IPacket m) {
		IPacket msg = new Packet(m.getID());
		msg.encapsulate(m);
		if (wasSent) {
			return pending.add(msg);
		} else {
			if (pending.add(msg) ) {
				wasSent = true;
				return sendNextMessage();
			} else {
				return false;
			}
		}
	}
	
	public void init(IGraph<Integer> topology) {
		step.run();
	}
	
	private boolean sendNextMessage() {
		IPacket mmsg = pending.remove();
		IMessage msg = mot.allocateMessage(mot);
		msg.setType(EMessageType.DATA);
		msg.setDest(mmsg.getID());
		
		if (mmsg.isEncapsulating()) { // data
			final int id = msg.getID();
			CheckMsg checkMsg = new CheckMsg(id);
			waiting.put(id, mmsg);
			scheduleEvent(checkMsg, 0);
		}
		
		int[] data = new int[mmsg.getLength()];
		mmsg.toIntArr(data, 0);
		msg.setData(data);
		return mot.sendMessage(msg);
	}
}