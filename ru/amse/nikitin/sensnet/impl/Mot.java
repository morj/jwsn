package ru.amse.nikitin.sensnet.impl;

import java.util.*;
import javax.swing.ImageIcon;

import ru.amse.nikitin.activeobj.EMessageType;
import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.IActiveObject;
import ru.amse.nikitin.activeobj.impl.*;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.*;

public class Mot implements IActiveObject {
	private Dispatcher s;
	private int id, x, y, lastMessageID, lastMessageSource, lastMessageDest;
	private double transmitterPower;
	private double threshold;
	// private double ratioX; private double ratioY;
	private IMotModule sendModule = new TransmitterModule();
	private List<MotModule> modules = new LinkedList<MotModule>();
	private IBattery b = new Battery (100000000);
	private MotDescription description;
	private Map<Class<? extends IMessage>, IGate> gates
		= new HashMap<Class<? extends IMessage>, IGate>();
	private IGate outputGate;
	
	/**
	 * All gate links here are 1-1 links without channels.
	 * This is the original internal topology of each mot,
	 * hence this constructor supports such connection.
	 */
	public Mot(int x_, int y_,
			double power, double threshold, IMotModuleFactory f) {
		x = x_; y = y_;
		s = Dispatcher.getInstance();
		transmitterPower = power;
		this.threshold = threshold;
		createLinearTopology(f);
		description = new MotDescription(new ImageIcon("noicon.png"), "Mot", x, y);
	}
	
	public Mot(int x_, int y_, double power, double threshold) {
		x = x_; y = y_;
		s = Dispatcher.getInstance();
		transmitterPower = power;
		this.threshold = threshold;
		outputGate = new Gate(null, "mot output gate");
		description = new MotDescription(new ImageIcon("noicon.png"), "Mot", x, y);
	}

	public IGate getOutputGate() {
		return outputGate;
	}

	private void createLinearTopology(IMotModuleFactory f) {
		IGate inputGate = declareInputGate(Message.class);
		
		MotModule module = f.createModule(this, 0);
		modules.add(module);
		
		IGate gate = module.declareGate(Const.lowerGateName);
		// inputGate -> gate
		inputGate.setTo(gate);
		gate.setFrom(inputGate);
		// gate -> outputGate
		outputGate = new Gate(sendModule, "mot linear output gate");
		gate.setTo(outputGate);
		outputGate.setFrom(gate);
		
		gate = module.declareGate(Const.upperGateName);
		for (int i = 1; i < f.getModuleCount(); i++) { 
			module = f.createModule(this, i);
			modules.add(module);
			IGate dest = module.declareGate(Const.lowerGateName);
			// gate <-> dest
			// if (dest == null) System.err.print("null dest");
			gate.setTo(dest);
			dest.setFrom(gate);
			gate.setFrom(dest);
			dest.setTo(gate);
			gate = module.declareGate(Const.upperGateName);
		}	
	}
	
	public double squaredDistanceTo(Mot m) {
		return((x-m.x)*(x-m.x) + (y-m.y)*(y-m.y));
	}
	
	public boolean recieveMessage(IMessage m) {
		lastMessageID = m.getID();
		lastMessageSource = m.getSource();
		lastMessageDest = m.getDest();
		switch (m.getType()) {
			case TIMER:
				int id = m.getID();
				// System.out.println(id + " recieved");
				for (MotModule module: modules) {
					module.fireEvent(id);
				}
				return true;
			case INIT:
				/* sheduleEvent(new Runnable() {
					public void run() {
						System.out.println("PREVED");
					}
				}, Time.randTime(5));
				int[] t = new int [2];
				t[1] = 0;
				app.sendMessage(t); */
				for (MotModule module: modules) {
					module.init(s.getTopology());
				}
				return true;
			default:
				if (b.drain()) {
					// return mac.recieveMessage(new Packet(m.getData(), 0));
					
					IGate in = gates.get(m.getClass());
					if (in != null) {
						// System.err.println("recv on " + in.getName());
						return in.recieveMessage(new Packet((int[])m.getData(), 0), null);
					} else {
						// System.err.println("no apropriate input gate for " + m.getClass());
						return false;
					}
				} else {
					return false;
				}
		}
	}
	
	/* package-private */ void scheduleMessage(IMessage msg, Time t) {
		s.scheduleMessage(msg, t);
	}
	
	public int getLastMessageID() {
		return lastMessageID;
	}
	
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
	
	public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
		description = new MotDescription(image, name, x, y);
		description.setOwner(this);
		return description; 
	}
	
	public IActiveObjectDesc getDesc() {
		return description;
	}
	
	public double getTransmitterPower() {
		return transmitterPower;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public IGate declareInputGate(Class<? extends IMessage> msgClass) {
		IGate newGate = null;
		if (!gates.containsKey(msgClass)) {
			String name = "mot input gate for " + msgClass.getName();
			// System.err.println(name);
			newGate = new Gate(null, name);
			gates.put(msgClass, newGate);
		}
		return newGate;
	}
	
	public IGate getInputGate(Class<? extends IMessage> msgClass) {
		return gates.get(msgClass);
	}
	
	public IMessage allocateMessage() {
		return s.allocateMessage(this);
	}
	
	private boolean sendMessage(IMessage m) {
		if (b.drain()) {
			// m.setDest(0);
			// m.setData(null);
			// Logger.getInstance().logMessage(m.toString());
			return s.sendMessage(m);
		} else {
			return false;
		}
	}
	
	private class TransmitterModule implements IMotModule {
		Gate input;
		public TransmitterModule() {
			input = new Gate(this, "input");
		}
		/** message recieve wrapper */
		public boolean recieveMessage(IPacket m) {
			// System.err.println("babax");
			IMessage msg = allocateMessage();
			msg.setType(EMessageType.DATA);
			msg.setDest(m.getID());
			ISendCallback action = m.getOnSendAction();
			if (action != null) {
				action.run(msg);
			}
			int[] data = new int[m.getLength()];
			m.toIntArr(data, 0);
			msg.setData(data);
			return sendMessage(msg);
		}
		public IGate declareGate(String name) {
			if (name.equals("input")) {
				return input;
			} else {
				return null;
			}
		}
		public IGate getGate(String name) {
			return input;
		}
		public void init(IGraph<Integer> topology) { /* :-) */ }
		public void setArrivedOn(String arrivedOn) { /* ;-] */ }
	}
}
