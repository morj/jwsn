package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;

import ru.amse.nikitin.activeobj.EMessageType;
import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.activeobj.impl.Message;
import ru.amse.nikitin.activeobj.impl.Dispatcher;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.impl.Gate;
import ru.amse.nikitin.net.impl.NetObject;
import ru.amse.nikitin.sensnet.*;

public class Mot extends NetObject {
	private Dispatcher s;
	private int x, y;
	private double transmitterPower;
	private double threshold;
	// private double ratioX; private double ratioY;
	private IMotModule transmitterModule = new TransmitterModule(this);
	private IBattery b = new Battery (100000000);
	private MotDescription description;
	
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
		IGate inputGate = declareInputGate(WirelessPacket.class);
		
		MotModule module = f.createModule(this, 0);
		addModule("m0", module);
		
		IGate gate = module.declareGate(Const.lowerGateName);
		// inputGate -> gate
		inputGate.setTo(gate);
		gate.setFrom(inputGate);
		// gate -> outputGate
		outputGate = new Gate(transmitterModule, "mot linear output gate");
		gate.setTo(outputGate);
		outputGate.setFrom(gate);
		
		gate = module.declareGate(Const.upperGateName);
		for (int i = 1; i < f.getModuleCount(); i++) { 
			module = f.createModule(this, i);
			addModule("m" + i, module);
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
				for (IModule module: modules.values()) {
					((MotModule)module).fireEvent(id);
				}
				return true;
			case INIT:
				for (IModule module: modules.values()) {
					((MotModule)module).init(s.getTopology());
				}
				return true;
			default:
				if (b.drain()) {
					// return mac.recieveMessage(new Packet(m.getData(), 0));
					
					if (m.getData() != null) {
						IGate in = gates.get(m.getData().getClass());
						if (in != null) {
							// System.err.println("recv on " + in.getName());
							IWirelessPacket p = (IWirelessPacket)m.getData();
							return in.recieveMessage(p, null);
						} // else
					} // else
					return false;
				} else {
					return false;
				}
		}
	}
	
	/* package-private */ void scheduleMessage(IMessage msg, Time t) {
		s.scheduleMessage(msg, t);
	}
	
	/* package-private */ IMessage newMessage() {
		IMessage msg = new Message(s.getMessageInitData());
		s.assignMessage(this, msg);
		return msg;
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
		private Gate input;
		private Mot mot;
		public TransmitterModule(Mot m) {
			input = new Gate(this, "input");
			this.mot = m;
		}
		/** message recieve wrapper */
		public boolean recieveMessage(IPacket m1) {
			// System.err.println("babax");
			IWirelessPacket m = (IWirelessPacket)m1;
			IMessage msg = new Message(s.getMessageInitData());
			s.assignMessage(mot, msg);
			msg.setType(EMessageType.DATA);
			msg.setDest(m.getID());
			ISendCallback action = m.getOnSendAction();
			if (action != null) {
				action.run(msg);
			}
			msg.setData(m);
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
