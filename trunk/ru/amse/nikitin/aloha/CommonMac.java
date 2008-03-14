package ru.amse.nikitin.aloha;

import java.util.*;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.ISendCallback;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.sensnet.util.MacData;


public class CommonMac extends MotModule {
	protected Queue<IWirelessPacket> pending = new LinkedList<IWirelessPacket>();
	protected Map<Integer, IWirelessPacket> waiting = new HashMap<Integer, IWirelessPacket>();
	
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
	
	class ConfirmMsg implements ISendCallback {
		private IWirelessPacket packet;
		
		public ConfirmMsg(IWirelessPacket packet) {
			this.packet = packet;
		}

		public void run (IMessage msg) {
			// data message: wait for response
			// System.err.println("callback");
			int id = msg.getID();
			CheckMsg checkMsg = new CheckMsg(id);
			waiting.put(id, packet);
			scheduleEvent(checkMsg, 0);
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
				MacData reciever = new MacData(mot.getLastMessageID());
				WirelessPacket confirmMsg = new WirelessPacket(mot.getLastMessageSource());
				confirmMsg.setData(reciever);
				pending.add(confirmMsg); // sending confirmation
				return getGate("upper").recieveMessage(m.decapsulate(), this);
			} else { // confirm
				MacData reciever = ((MacData)m.getData()); 
				waiting.remove(reciever.getMessageId());
				Logger.getInstance().logMessage(
					ELogMsgType.INFORMATION, 
					"rem " + reciever.getMessageId()
				);
				return true;
			}
		} else {
			return false;
		}
	}
	
	public boolean upperMessage(IWirelessPacket m) {
		IWirelessPacket msg = new WirelessPacket(m.getID());
		msg.encapsulate(m);
		msg.setOnSendAction(new ConfirmMsg(msg));
		if (wasSent) {
			return pending.add(msg);
		} else {
			if (pending.add(msg)) {
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
		IWirelessPacket mmsg = pending.remove();
		return getGate("lower").recieveMessage(mmsg, this);
	}
}
