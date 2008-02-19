package ru.amse.nikitin.activeobj.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import ru.amse.nikitin.activeobj.IActiveObject;
import ru.amse.nikitin.activeobj.IDisplayListener;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.IMessageFilter;

/**
 * @author Pavel A. Nikitin
 * void message filter implementation
 *
 */
public class VoidMessageFilter implements IMessageFilter {
	/** void filtering method */
	public void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			Queue<IMessage> dropped, List<IDisplayListener> displisteners) {
		Iterator<IMessage> i = messages.iterator();
		Iterator<IActiveObject> j;
		IMessage m;
		while(i.hasNext()) {
			m = i.next();
			j = objs.iterator();
			while(j.hasNext()) {
				j.next().recieveMessage(m);
			}
			dropped.add(m);
			i.remove();
		}
	}
}
