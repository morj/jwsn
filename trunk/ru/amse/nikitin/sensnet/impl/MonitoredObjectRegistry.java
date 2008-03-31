package ru.amse.nikitin.sensnet.impl;

import java.util.*;

import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.impl.Logger;

public class MonitoredObjectRegistry {
	protected static Map<String, MonitoredObject> objects
		= new HashMap<String, MonitoredObject>();
	
	public static Object getReading(String name) {
		Object reading = objects.get(name).getReading();
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
				"reading from " + name + ": " + reading);
		return reading;
	}
	
	/* package-private */ static void registerSensingObject(String name, MonitoredObject obj) {
		objects.put(name, obj);
	}
}
