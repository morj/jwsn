package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;

import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.impl.Gate;
import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.sensnet.IMonitoredObjectModule;
import ru.amse.nikitin.simulator.EMessageType;
import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.impl.Message;
import ru.amse.nikitin.simulator.impl.Time;

public class MonitoredObject extends MovingObject {
	protected SensingModule sensingModule = new SensingModule(this);	

	public MonitoredObject(int x, int y) {
		super(x, y);
		s = Dispatcher.getInstance();
		outputGate = sensingModule.declareGate("phy");
		newDesc(null, "Sensing Object", x, y);
	}

	public void createTopology() {
		IModule module = modules.get("logic");
		
		if (module != null) {
			IGate gate = module.declareGate("output");
			// gate -> outputGate
			gate.setTo(outputGate);
			outputGate.setFrom(gate);
		}
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
					((MonitoredObjectModule)module).fireEvent(id);
				}
				return true;
			case INIT:
				for (IModule module: modules.values()) {
					((IMonitoredObjectModule)module).init();
				}
				return true;
			default:
				return false;
		}
	}
	
	public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
		MonitoredObjectRegistry.registerSensingObject(name, this);
		return super.newDesc(image, name, x, y);
	}
	
	/* package-private */ Object getReading() {
		return ((IMonitoredObjectModule)modules.get("logic")).getReading();
	}
	
	/* package-private */ void scheduleMessage(IMessage msg, Time t) {
		s.scheduleMessage(msg, t);
	}
	
	/* package-private */ IMessage newMessage() {
		IMessage msg = new Message(s.getMessageInitData());
		s.assignMessage(this, msg);
		return msg;
	}
	
	private class SensingModule implements IModule {
		private Gate input;
		private MonitoredObject sensing;
		
		public SensingModule(MonitoredObject sensing) {
			this.sensing = sensing;
		}
		
		public boolean recieveMessage(IPacket m) {
			IMessage msg = new Message(s.getMessageInitData());
			s.assignMessage(sensing, msg);
			msg.setType(EMessageType.DATA);
			msg.setDest(sensing.getID());
			msg.setData(m);
			return s.sendMessage(msg);
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

		public void setArrivedOn(String arrivedOn) { }
		
	}

}
