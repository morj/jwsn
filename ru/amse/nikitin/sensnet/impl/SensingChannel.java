package ru.amse.nikitin.sensnet.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import ru.amse.nikitin.sensnet.IMovingObject;
import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IDisplayListener;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.IMessageFilter;

public class SensingChannel implements IMessageFilter {
	private static SensingChannel instance = null;
	
	public static SensingChannel getInstance () {
		if (instance == null) {
			instance = new SensingChannel();
		}
		return instance;
	}
	
	protected void recieve(IActiveObject obj, IMessage m, List<IDisplayListener> displisteners) {
		for (IDisplayListener i: displisteners) {
			i.messageRecieved(m.getSource(), obj.getID(), m.getData());
			if (obj.recieveMessage(m)) {
				i.messageAccepted(obj.getID());
			}
		}
	}

	public void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			List<IDisplayListener> dispListeners) {
		Iterator<IMessage> j = messages.iterator();
		while (j.hasNext()) {
			IMessage msg = j.next(); // next in iterator
			Object peek = msg.getData();
			if(peek != null) {
				if(peek instanceof MonitoredPacket) {
					j.remove(); // removing from iterator
				}
			}
			for (IActiveObject obj: objs) {
				IMovingObject t = (IMovingObject)obj;
				if (t.hasInputGate(MonitoredPacket.class)) {
					if (t.squaredDistanceTo((IMovingObject)objs.get(msg.getSource()))
							< 2500) {
						recieve(obj, msg, dispListeners);
					}
				}
			}
		}
	}

}
