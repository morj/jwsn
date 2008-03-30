package ru.amse.nikitin.sensnet.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IDisplayListener;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.IMessageFilter;

public class SensingChannel implements IMessageFilter {

	public void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			List<IDisplayListener> dispListeners) {
		Iterator<IMessage> j = messages.iterator();
		while (j.hasNext()) {
			IMessage msg = j.next(); // next in iterator
			Object peek = msg.getData();
			if(peek != null) {
				if(peek instanceof SensingPacket) {
					j.remove(); // removing from iterator
				}
			}
			for (IActiveObject obj: objs) {
				obj.recieveMessage(msg);
			}
		}
	}

}
