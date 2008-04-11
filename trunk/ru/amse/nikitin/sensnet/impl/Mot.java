package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.impl.Gate;
import ru.amse.nikitin.sensnet.Const;
import ru.amse.nikitin.sensnet.IBattery;
import ru.amse.nikitin.sensnet.IMot;
import ru.amse.nikitin.sensnet.IMotModule;
import ru.amse.nikitin.sensnet.IMotModuleFactory;
// import ru.amse.nikitin.sensnet.ISendCallback;
import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.simulator.EMessageType;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.impl.Message;
import ru.amse.nikitin.simulator.impl.Time;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class Mot extends MovingObject implements IMot {
	protected Dispatcher s;
	
	protected double transmitterPower;
	protected double threshold;
	// private double ratioX; private double ratioY;
	protected IMotModule transmitterModule = new TransmitterModule(this);
	protected IBattery b = new Battery (100000000);
	// protected int linearModuleCount;
	
	/**
	 * All gate links here are 1-1 links without channels.
	 * This is the original internal topology of each mot,
	 * hence this constructor supports such connection.
	 */
	public Mot(int x, int y,
			double power, double threshold, IMotModuleFactory f) {
		super(x, y);
		s = Dispatcher.getInstance();
		transmitterPower = power;
		this.threshold = threshold;
		int count = f.getModuleCount();
		for (int i = 0; i < count; i++) {
			addModule(Util.moduleName(i), f.createModule(this, i));
		}
		outputGate = transmitterModule.declareGate("phy");
		createLinearTopology(count);
		newDesc(null, "Mot", x, y);
	}
	
	public Mot(int x, int y, double power, double threshold) {
		super(x, y);
		s = Dispatcher.getInstance();
		transmitterPower = power;
		this.threshold = threshold;
		outputGate = transmitterModule.declareGate("phy");
		newDesc(null, "Mot", x, y);
	}
	
	public void createTopology() {
		createLinearTopology(3);
	}

	public void createLinearTopology(int count) {
		// IGate sensingGate = declareInputGate(MonitoredPacket.class);
		IGate inputGate = declareInputGate(WirelessPacket.class);
		
		IMotModule module = (IMotModule)modules.get(Util.moduleName(0));
		
		IGate gate = module.declareGate(Const.lowerGateName);
		// inputGate -> gate
		inputGate.setTo(gate);
		gate.setFrom(inputGate);
		// gate -> outputGate
		gate.setTo(outputGate);
		outputGate.setFrom(gate);
		
		gate = module.declareGate(Const.upperGateName);
		for (int i = 1; i < count; i++) { 
			module = (IMotModule)modules.get(Util.moduleName(i));
			IGate dest = module.declareGate(Const.lowerGateName);
			// gate <-> dest
			// if (dest == null) System.err.print("null dest");
			gate.setTo(dest);
			dest.setFrom(gate);
			gate.setFrom(dest);
			dest.setTo(gate);
			gate = module.declareGate(Const.upperGateName);
		}
		
		// module = (IMotModule)modules.get(Util.moduleName(2));
		// gate = module.declareGate(Const.sensingGateName);
		// sensingGate -> gate
		// sensingGate.setTo(gate);
		// gate.setFrom(sensingGate);
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
							IPacket p = (IPacket)m.getData();
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
			this.mot = m;
		}
		/** message recieve wrapper */
		public boolean recieveMessage(IPacket m1) {
			IWirelessPacket m = (IWirelessPacket)m1;
			IMessage msg = new Message(s.getMessageInitData());
			s.assignMessage(mot, msg);
			msg.setType(EMessageType.DATA);
			msg.setDest(m.getDest());
			/* ISendCallback action = m.getOnSendAction();
			if (action != null) {
				action.run(msg);
				m.removeOnSendAction();
			} */
			msg.setData(m);
			return sendMessage(msg);
		}
		public IGate declareGate(String name) {
			if (name.equals("phy")) {
				input = new Gate(this, name);
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
