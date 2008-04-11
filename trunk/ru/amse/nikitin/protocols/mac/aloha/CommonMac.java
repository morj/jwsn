package ru.amse.nikitin.protocols.mac.aloha;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Deque;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.simulator.impl.Time;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class CommonMac extends MotModule {
	protected static final Time oneUnitTime = new Time(1);
	protected Deque<IWirelessPacket> pending = new LinkedList<IWirelessPacket>();
	protected Map<Integer, IWirelessPacket> waiting = new HashMap<Integer, IWirelessPacket>();
	protected Map<Integer, Integer> tries = new HashMap<Integer, Integer>();
	
	protected boolean wasSent = false;
	
	final Runnable step = new Runnable() {
		public void run () {
			// System.out.println("mac step " + mot.getID());
			
			if (pending.isEmpty() || wasSent) {
				wasSent = false;
			} else {
				wasSent = true;
				sendNextMessage(); // sending one next message
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
			if(waiting.containsKey(id)) {
				pending.addLast(waiting.get(id));
				Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
						"- msg " + id + ": resubmitting");
			} else {
				Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
						"- msg " + id + " sent successfully!");
			}
		}
	}
	
	class CheckMsg implements Runnable {
		protected final int id;
		
		public CheckMsg(int id) {
			this.id = id;
		}

		public void run() {
			if (!waiting.containsKey(id)) {
				Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
					"- msg " + id + " sent successfully!");
			} else { // resend
				if(tries.get(id) > 0) {
					Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
							"- msg " + id + ": resubmit scheduled");
					scheduleEvent(new ResendMsg(id), Time.randTime(4));
				} else {
					Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
							"- msg " + id + ": resubmit cancelled");
					waiting.remove(id);
					tries.remove(id);
				}
			}
		}
	}
	
	public CommonMac(Mot m) {
		super(m);
	}
	
	public boolean lowerMessage(IWirelessPacket m) {
		if (mot.getLastMessageDest() == mot.getID()) {
			if (m.isEncapsulating()) { // data
				// int[] reciever = new int [1];
				// reciever[0] = mot.getLastMessageID();
				// if (m.getDest() != -1) {
				MacData reciever = new MacData(m.hashCode());
				IWirelessPacket confirmMsg = new WirelessPacket(mot.getLastMessageSource(), mot);
				confirmMsg.setData(reciever);
				pending.addFirst(confirmMsg); // sending confirmation	
				// }
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else { // confirm
				if (m.getData() == null) {
					/* Logger.getInstance().logMessage(
						ELogMsgType.INFORMATION, 
						"bad confirm data in msg " + m.getID()
					); */
					System.err.println("bad confirm data in msg " + m.getDest());
					return false;
				} else {
					MacData reciever = ((MacData)m.getData());
					int id = reciever.getMessageId();
					Logger.getInstance().logMessage(
						ELogMsgType.INFORMATION, 
						"rem " + id
					);
					if(!waiting.remove(id).releaseLock(mot)) {
						System.err.println("not a lock owner");
					}
					tries.remove(id);
					return true;
				}
			}
		} else { // not our message, but what if net level wants to track it...
			if (m.isEncapsulating()) {
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else {
				return false;
			}
		}
	}
	
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(m.getDest(), mot);
		msg.encapsulate(m);
		if(!msg.setLock(mot)) {
			System.err.println("not a lock owner");
		}
		// msg.setOnSendAction(new ConfirmMsg(msg));
		if (wasSent) {
			pending.addLast(msg);
			return true;
		} else {
			if (pending.add(msg)) {
				wasSent = true;
				if (msg == null) {
					System.err.println("putting null msg in queue");
					return false;
				}
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
		IWirelessPacket mmsg = pending.remove();
		if (mmsg == null) {
			System.err.println("null msg in queue");
			return false;
		} else {
			if(mmsg.isEncapsulating()) { // not a confirm msg
				int code = mmsg.hashCode();
				if (!waiting.containsKey(code)) {
					if(mmsg.getDest() != -1) {
						waiting.put(code, mmsg);
						tries.put(code, 3);
					}
				} else {
					Integer t = tries.remove(code);
					tries.put(code, t - 1);
					System.err.println("tries left: " + t);
				}
				scheduleEvent(new CheckMsg(code), oneUnitTime);
			}
		}
		return getGate("lower").recieveMessage(mmsg, this);
	}
}
