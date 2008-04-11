package ru.amse.nikitin.net.impl;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.net.INetObject;
import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Dispatcher;

public class NetObject implements INetObject {
	protected Dispatcher s;
	protected int id, lastMessageID, lastMessageSource, lastMessageDest;

	// private List<MotModule> modules = new LinkedList<MotModule>();
	protected Map<String, IModule> modules = new HashMap<String, IModule>();

	protected Map<Class<? extends IPacket>, IGate> gates
		= new HashMap<Class<? extends IPacket>, IGate>();
	protected IGate outputGate;
	
	public void createTopology() {
	}

	public NetObject() {
		s = Dispatcher.getInstance();
		outputGate = new Gate(null, "net object output gate");
	}

	public IGate getOutputGate() {
		return outputGate;
	}
	
	public boolean recieveMessage(IMessage m) {
		lastMessageID = m.getID();
		lastMessageSource = m.getSource();
		lastMessageDest = m.getDest();
		
		IGate in = gates.get(m.getClass());
		if (in != null) {
			// System.err.println("recv on " + in.getName());
			IPacket p = (IPacket)m.getData();
			return in.recieveMessage(p, null);
		} else {
			// System.err.println("no apropriate input gate for " + m.getClass());
			return false;
		}
	}
	
	/* public int getLastMessageID() {
		return lastMessageID;
	} */
	
	public int getLastMessageSource() {
		return lastMessageSource;
	}
	
	public int getLastMessageDest() {
		return lastMessageDest;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int newID) {
		id = newID;
	}
	
	public IGate declareInputGate(Class<? extends IPacket> msgClass) {
		IGate newGate = null;
		if (!gates.containsKey(msgClass)) {
			String name = "mot input gate for " + msgClass.getName();
			// System.err.println(name);
			newGate = new Gate(null, name);
			gates.put(msgClass, newGate);
		}
		return newGate;
	}
	
	public IGate getInputGate(Class<? extends IPacket> msgClass) {
		return gates.get(msgClass);
	}
	
	public boolean hasInputGate(Class<? extends IPacket> msgClass) {
		return gates.containsKey(msgClass);
	}

	public IActiveObjectDesc getDesc() {
		return null;
	}

	public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
		return null;
	}

	public IModule addModule(String name, IModule module) {
		return modules.put(name, module);
	}

	public IModule getModule(String name) {
		return modules.get(name);
	}

	public void notification(String text) {
		s.notification(this, text);
	}
	
}
