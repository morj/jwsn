package ru.amse.nikitin.protocols.app;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IWirelessPacket;

public class BsApp extends MotModule {
	
	public BsApp(Mot m) {
		super(m);
	}
	
	public boolean lowerMessage(IWirelessPacket m) {
		if (m.getData() != null) {
			BsData data = (BsData)m.getData();
			if (data.getType() == Const.hello) {
				Logger.getInstance().logMessage(ELogMsgType.RECIEVE,
					"hello message " + data.getIndex() + " recieved by BS");
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
