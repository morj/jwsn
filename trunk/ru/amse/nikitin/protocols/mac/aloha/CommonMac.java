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
	protected static final Time oneUnitTime = new Time(0);
	protected Deque<IWirelessPacket> pending = new LinkedList<IWirelessPacket>();
	protected IWirelessPacket lastMsg = null;
	protected int tries = 0;
	protected boolean isBlocked = false;
	protected boolean wasSent = false;
	
	final Runnable step = new Runnable() {
		public void run () {
			// System.out.println("mac step " + mot.getID());
			if (pending.isEmpty() || wasSent) {
				mot.notification("msg queue size = " + pending.size());
				wasSent = false;
			} else {
				isBlocked = false;
				wasSent = true;
				sendNextMessage(); // sending one next message
			}
		}
	};
	
	/* class ResendMsg implements Runnable {
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
	} */
	
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
					if (lastMsg != null) {
						if (lastMsg.hashCode() == id) {
							Logger.getInstance().logMessage(
								ELogMsgType.INFORMATION, 
								"rem " + id
							);
							if (!lastMsg.releaseLock(mot)) {
								System.err.println("not a lock owner");
							}
							lastMsg = null;
						} else {
							Logger.getInstance().logMessage(
								ELogMsgType.INFORMATION, 
								"unknown confirm id " + id
							);
						}
					}
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
		pending.addLast(msg);
		if(!isBlocked) {
			if (wasSent) {
				wasSent = false;
				return true;
			} else {
				wasSent = true;
				return sendNextMessage();
			}
		} else {
			return false;
		}
	}
	
	public void init(IGraph<Integer> topology) {
		step.run();
	}
	
	private boolean sendNextMessage() {
		System.err.println("sendNextMessage on " + mot.getID());
		if (!isBlocked) {
			IWirelessPacket mmsg = pending.getFirst();

			if(mmsg.isEncapsulating() && (mmsg.getDest() != -1)) { // msg needs confirmation
				// send and care about resend
				
				if(mmsg == lastMsg) { // resend
					if(tries > 0) { // do resend
						Logger.getInstance().logMessage(
							ELogMsgType.INFORMATION, 
							"resubmit scheduled for WP " + mmsg.hashCode()
						);
						isBlocked = true;
						scheduleEvent(step, Time.randTime(4));
						return false;
					} else { // discard
						pending.removeFirst();
						mmsg = null;
						if(!pending.isEmpty()) { // get next newcomer
							mmsg = pending.removeFirst();
							lastMsg = mmsg;
							tries = 3;
						}
					}
					tries--;
				} else { // newcomer in line
					lastMsg = mmsg;
					tries = 3;
				}
				
				// isBlocked = true;
			} else { // confirm msg
				// just send
				pending.removeFirst();
			}
			
			scheduleEvent(step, oneUnitTime);
			if (mmsg != null) return getGate("lower").recieveMessage(mmsg, this);
		}
		return false;
	}
}
