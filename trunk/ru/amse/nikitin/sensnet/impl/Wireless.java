package ru.amse.nikitin.sensnet.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import ru.amse.nikitin.activeobj.IActiveObject;
import ru.amse.nikitin.activeobj.IDisplayListener;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.IMessageFilter;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.activeobj.ELogMsgType;

public class Wireless implements IMessageFilter {
	private static Wireless instance = null;
	
	private class PoweredMessage implements Comparable<PoweredMessage> {
		IMessage m; // encapsulates SensnetMessage
		double p = 0;
		PoweredMessage(IMessage msg) { m = msg; }
		public int compareTo(PoweredMessage rv) {
			double rvp = rv.p;
			return (p > rvp ? -1 : (p == rvp ? 0 : 1));
		}
	}
	
	public static Wireless getInstance () {
		return (instance == null ? new Wireless() : instance);
	}
	
	protected void recieve(Mot mot, IMessage m, List<IDisplayListener> displisteners) {
		for (IDisplayListener i: displisteners) {
			i.messageRecieved(m.getSource(), mot.getID(), m.getData());
			if (mot.recieveMessage(m)) {
				i.messageAccepted(mot.getID());
			}
		}
	}
	
	public void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			// Queue<IMessage> dropped,
			List<IDisplayListener> displisteners) {
		Logger l = Logger.getInstance();
		int size = messages.size();
		List<PoweredMessage> msg = new ArrayList<PoweredMessage>();
		// draining messages to this list
		while (!messages.isEmpty()) {
			Object peek = messages.peek().getData();
			if(peek != null) {
				if(peek instanceof WirelessPacket) {
					PoweredMessage pw = new PoweredMessage(messages.poll());
					msg.add (pw);
				}
			}
		}
		Iterator i = objs.iterator();
		if (size > 0) while (i.hasNext()) { // for each mot
			Mot currmot = (Mot)i.next();
			if (currmot != null) { // filling messages power
				for (PoweredMessage m: msg) {
					Mot tmp = (Mot)objs.get(m.m.getSource());
					m.p = tmp.getTransmitterPower() / 
						(currmot.squaredDistanceTo(tmp));
				}
				Collections.sort(msg);
				PoweredMessage currmsg = msg.get(0); // looking at the first message
				int id = currmot.getID();
				if (currmsg.p >= currmot.getThreshold()) { // checking threshold
					if (size > 1) {
						if ((msg.get(1)).p
								/ currmsg.p > 0.5) { // messages interfere
							// l.logMessage("d " + currmsg.m + " - i, not recieved by mot " + id);
							l.logMessage(
								ELogMsgType.DROPPED,
								"not recieved by mot " + id,
								currmsg.m
							);
							for (IDisplayListener listener: displisteners) {
								listener.messageConflicted(currmsg.m.getSource(), id);
							}
							currmsg = msg.get(1);
							l.logMessage(
								ELogMsgType.DROPPED,
								"not recieved by mot " + id,
								currmsg.m
							);
							for (IDisplayListener listener: displisteners) {
								listener.messageConflicted(currmsg.m.getSource(), id);
							}
						} else {
							if (currmsg.m.getSource() != id) {
								// l.logMessage("s " + currmsg.m + " - recieved by mot " + id + ", p = " + currmsg.p);
								l.logMessage(
									ELogMsgType.SENT,
									"recieved by mot " + id,
									currmsg.m
								);
								recieve(currmot, currmsg.m, displisteners);
							} else {
								// l.logMessage("d all messages to Mot " + id + " (transmitter was active)");
								l.logMessage(
									ELogMsgType.DROPPED,
									"all messages to mot " + id + " trans. active"
								);
							}
						}
					} else {
						if (currmsg.m.getSource() != id) {
							// l.logMessage("s " + currmsg.m + " was the only msg to mot " + id + ", p = " + currmsg.p);
							l.logMessage(
								ELogMsgType.SENT,
								"the only msg to mot " + id,
								currmsg.m
							);
							recieve(currmot, currmsg.m, displisteners);
						}
					}
				} else {
					/* l.logMessage(
						ELogMsgType.DROPPED,
						"all messages to mot " + id + " threshold"
					); */
				}
			}
		}
		/* Iterator<PoweredMessage> j = msg.iterator();
		while(j.hasNext()) {
			dropped.add(j.next().m);
			j.remove();
		} */ msg.clear();
	}
}
