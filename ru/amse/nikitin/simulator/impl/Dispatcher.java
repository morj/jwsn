package ru.amse.nikitin.simulator.impl;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.EMessageType;
import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.IDisplayListener;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.IMessageFilter;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class Dispatcher implements IDispatcher {
	private static Dispatcher instance;
	protected List<IMessageFilter> messageFilters = new LinkedList<IMessageFilter>();
	protected int listenersCount = 0;
	protected int messageCount = 0;
	protected List<IActiveObject> listeners = new LinkedList<IActiveObject>();
	protected List<IDisplayListener> dispListeners = new LinkedList<IDisplayListener>();
	protected Queue<IMessage> messages = new PriorityQueue<IMessage>();
	// protected Queue<IMessage> dropped = new ArrayDeque<IMessage>();
	protected Queue<IMessage> timered = new ArrayDeque<IMessage>();
	protected Queue<IMessage> pending = new ArrayDeque<IMessage>();
	protected Time time;
	protected IGraph<Integer> graph;
	
	protected Dispatcher() {
		time = new Time();
	}
	
	/* package-private */ Time getTime() {
		return time;
	}
	
	public static Dispatcher getInstance() {
		if (instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}
	
	public void removeAllListeners() {
		listeners.clear();
		listenersCount = 0;
	}
	
	public void addMessageFilter(IMessageFilter m) {
		messageFilters.add(m);
	}
	
	public void init() {
		for (IDisplayListener i: dispListeners) {
			i.stepStarted();
		}
		for (IActiveObject obj: listeners) {
			IMessage m = new Message(getMessageInitData());
			m.setType(EMessageType.INIT);
			obj.recieveMessage(m);
			// dropped.add(m);
		}
		for (IDisplayListener i: dispListeners) {
			i.stepPerformed();
		}
	}
	
	/* public IMessage allocateMessage(IActiveObject obj) {
		if (!dropped.isEmpty()) {
			Message m = (Message)dropped.peek();
			m.setID(++messageCount);
			m.setSource(obj.getID());
			m.setTimer(time);
			return dropped.remove();
		} else {
			Message res = (Message)allocateMessage(new Time(time), ++messageCount);
			res.setSource(obj.getID());
			return res;
		}
	} */
	
	/* public void notification(IActiveObject obj, String message) {
		for (IDisplayListener i: dispListeners) {
			i.notificationArrived(obj.getID(), message);
		}
	} */
	
	public void addActiveObjectListener(IActiveObject obj) {
		obj.setID(listenersCount);
		for (IDisplayListener i: dispListeners) {
			i.objectAdded(listenersCount, obj.getDesc());
		}
		listenersCount++;
		listeners.add(obj);
	}
	
	public void addDisplayListener(IDisplayListener l) {
		dispListeners.add(l);
	}
	
	public boolean sendMessage(IMessage m) {
		switch (m.getType()) {
			case TIMER:
				/* ActiveObject is not allowed to send timer messages
				 * directly (use shedulemessage).
				 */
				return false;
			// break;
			// case BROADCAST: messages.add(m); break;
			default:
				messages.add(m);
		}
		return true;
	}
	
	public void scheduleMessage(IMessage m, Time t) {
		((Message)m).delay(t);
		m.setDest(m.getSource());
		m.setType(EMessageType.TIMER);
		// System.out.println("time = " + t.getTime());
		messages.add(m);
	}
	
	public void step() {
		for (IDisplayListener i: dispListeners) {
			i.stepStarted();
		}
		IMessage m;
		time.tick();
		while(!messages.isEmpty()) {
			m = messages.peek();
			if (((Message)m).isOnTime(time)) {
				switch (m.getType()) {
					case TIMER:
						timered.add(messages.remove());
					break;
					/* case BROADCAST:
						for (IActiveObject obj: listeners) {
							obj.recieveMessage(m);
						}
						dropped.add(messages.remove());
					break; */
					default:
						pending.add(messages.remove());
				}
			} else {
				break;
			}
		}
		
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION, "notify");
		
		for (IMessageFilter filter: messageFilters) {
			filter.Filter(listeners, pending, dispListeners);
		}
		assert pending.isEmpty();
		/* for (IMessage message: pending) {
			listeners.get(message.getDest()).recieveMessage(message);
		} */
		
		while(!timered.isEmpty()) {
			m = timered.remove();
			listeners.get(m.getDest()).recieveMessage(m);
		}
		
		for (IDisplayListener i: dispListeners) {
			i.stepPerformed();
		}
	}
	
	public void setTopology(IGraph<Integer> graph) {
		this.graph = graph;
	}
	
	public IGraph<Integer> getTopology() {
		return graph;
	}

	public int getListenersCount() {
		return listenersCount;
	}

	public MessageInitData getMessageInitData() {
		return new MessageInitData(new Time(time), ++messageCount);
	}

	public void assignMessage(IActiveObject owner, IMessage m) {
		((Message)m).setSource(owner.getID());
	}

	public void changeDesc(IActiveObject owner) {
		for (IDisplayListener i: dispListeners) {
			i.descChanged(owner.getID());
		}
	}
}
