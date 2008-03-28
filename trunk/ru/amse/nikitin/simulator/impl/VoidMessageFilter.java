package ru.amse.nikitin.simulator.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IDisplayListener;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.IMessageFilter;

/**
 * @author Pavel A. Nikitin
 * void message filter implementation
 *
 */
public class VoidMessageFilter implements IMessageFilter {
	/** void filtering method */
	public void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			// Queue<IMessage> dropped,
			List<IDisplayListener> displisteners) {
		Iterator<IMessage> i = messages.iterator();
		Iterator<IActiveObject> j;
		IMessage m;
		while(i.hasNext()) {
			m = i.next();
			j = objs.iterator();
			while(j.hasNext()) {
				j.next().recieveMessage(m);
			}
			// dropped.add(m);
			i.remove();
		}
	}
}
