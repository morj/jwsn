package ru.amse.nikitin.sensnet.impl;

import java.util.HashMap;
import java.util.Map;
import ru.amse.nikitin.sensnet.IMonitoredObjectModule;
import ru.amse.nikitin.net.impl.NetModule;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Time;

public class MonitoredObjectModule extends NetModule implements IMonitoredObjectModule {
	protected MonitoredObject sensing;
	protected Map<Integer, Runnable> events = new HashMap<Integer, Runnable>();
	
	public MonitoredObjectModule(MonitoredObject sensing) {
		this.sensing = sensing;
	}
	
	protected void scheduleEvent(Runnable r, int t) {
		IMessage msg = sensing.newMessage();
		Integer id = msg.getID();
		// System.err.println(id + " allocated");
		assert events.containsKey(id);
		sensing.scheduleMessage(msg, new Time(t));
		events.put(id, r);
	}

	protected void scheduleEvent(Runnable r, Time t) {
		IMessage msg = sensing.newMessage();
		Integer id = msg.getID();
		// System.out.println(id + " allocated");
		assert events.containsKey(id);
		sensing.scheduleMessage(msg, t);
		events.put(id, r);
	}

	/* package-private */ void fireEvent(int id) {
		if (events.containsKey(id)) {
			Runnable r = events.remove(id);
			r.run();
		}
	}

	public void init() { }

	public Object getReading() {
		return null;
	}

}
