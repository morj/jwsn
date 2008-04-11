package ru.amse.nikitin.protocols.app;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class BsApp extends MotModule {
	
	public BsApp(Mot m) {
		super(m);
	}
	
	public boolean lowerMessage(IWirelessPacket m) {
		mot.notification("������!!!");
		if (m.getData() != null) {
			if (m.getData() instanceof BsData) {
				BsData data = (BsData)m.getData();
				if (data.getType() == Const.hello) {
					Logger.getInstance().logMessage(ELogMsgType.RECIEVE,
						"hello message " + data.getIndex() + " recieved by BS");
					return true;
				}
			}
			if (m.getData() instanceof CarData) {
				CarData data = (CarData)m.getData();
				Logger.getInstance().logMessage(ELogMsgType.RECIEVE,
						"car seen at " + data.getX() + ", " + data.getY());
					return true;
			}
		} else {
			System.err.println("null data for bs");
		}
		return false;
	}
	
	public boolean upperMessage(IWirelessPacket m) {
		// AppMessage msg = new AppMessage(m);	
		// return lower.sendMessage(m);
		return true;
	}
	
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"bs init " + mot.getID());
	}
}
