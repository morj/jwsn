package ru.amse.nikitin.protocols.mac.aloha;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

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
	protected int isBlocked = 0;
	protected boolean wasSent = false;
	protected boolean resubmit = false;
	protected static Random randomizer = new Random();
	
	final Runnable step = new Runnable() {
		public void run () {
			/* if(mot.getID() == 1) {
				System.out.println("step for 1, tries = " + tries +
						"; isBlocked = " + isBlocked + ", lastMsg = " + lastMsg);
			} */
			// System.out.println("mac step " + mot.getID());
			if(isBlocked > 0) isBlocked--;
			if (wasSent) {
				mot.notification("msg queue size = " + pending.size());
				wasSent = false;
			} else {
				wasSent = true;
				sendNextMessage(); // sending one next message
			}
			scheduleEvent(this, oneUnitTime);
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
		/* if(mot.getID() == 1) {
			System.out.println("upperMessage for 1, tries = " + tries +
					"; isBlocked = " + isBlocked + ", lastMsg = " + lastMsg);
		} */
		IWirelessPacket msg = new WirelessPacket(m.getDest(), mot);
		msg.encapsulate(m);
		if(!msg.setLock(mot)) {
			System.err.println("not a lock owner");
		}
		// msg.setOnSendAction(new ConfirmMsg(msg));
		pending.addLast(msg);
		if(isBlocked == 0) {
			if (wasSent) {
				wasSent = false;
				return true;
			} else {
				mot.notification("msg queue size = " + pending.size());
				wasSent = true;
				return sendNextMessage();
			}
		} else {
			mot.notification("blocked, msg queue size = " + pending.size());
			return false;
		}
	}
	
	public void init(IGraph<Integer> topology) {
		step.run();
	}
	
	private boolean sendNextMessage() {
		/* if(mot.getID() == 1) {
			System.out.println("sendNextMessage for 1, tries = " + tries +
					"; isBlocked = " + isBlocked + ", lastMsg = " + lastMsg);
		} */
		if(pending.isEmpty()) {
			return false;
		} else {
			if (isBlocked == 0) {
				IWirelessPacket mmsg = pending.getFirst();
	
				if(mmsg.isEncapsulating() && (mmsg.getDest() != -1)) { // msg needs confirmation
					// send and care about resend
					
					if(mmsg == lastMsg) { // resend
						if(tries > 0) { // do resubmit
							if (resubmit) {
								Logger.getInstance().logMessage(
									ELogMsgType.INFORMATION,
									"resubmitting WP " + mmsg.hashCode() + " tries = " + tries +
									"; isBlocked = " + isBlocked + ", lastMsg = " + lastMsg
								);
								resubmit = false;
								return getGate("lower").recieveMessage(mmsg, this);
							} else {
								Logger.getInstance().logMessage(
									ELogMsgType.INFORMATION,
									"resubmit scheduled for WP " + mmsg.hashCode() + " tries = " + tries +
									"; isBlocked = " + isBlocked + ", lastMsg = " + lastMsg
								);
								isBlocked = Math.abs(randomizer.nextInt() % 4);
								tries--;
								resubmit = true;
								return true; 
							}
						} else { // discard
							pending.removeFirst();
							mmsg = null;
							if(!pending.isEmpty()) { // get next newcomer
								mmsg = pending.removeFirst();
								lastMsg = mmsg;
								tries = 3;
								resubmit = false;
							}
						}
					} else { // newcomer in line
						resubmit = false;
						lastMsg = mmsg;
						tries = 3;
					}
					
					// isBlocked = true;
				} else { // confirm msg
					// just send
					pending.removeFirst();
				}
				Logger.getInstance().logMessage(
					ELogMsgType.INFORMATION,
					"regular step scheduled for mot " + mot.getID() +
					"; isBlocked = " + isBlocked + ", lastMsg = " + lastMsg
				);
				if (mmsg != null) return getGate("lower").recieveMessage(mmsg, this);
			}
			return false;
		}
	}
}
