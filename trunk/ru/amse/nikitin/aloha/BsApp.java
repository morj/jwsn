package ru.amse.nikitin.aloha;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;

public class BsApp extends MotModule {
	
	public BsApp(Mot m) {
		super(m);
	}
	
	public boolean lowerMessage(IPacket m) {
		// AppMessage msg = new AppMessage(m);
		if (m.getData()[0] == Const.hello) {
			Logger.getInstance().logMessage(ELogMsgType.RECIEVE,
				"hello message " + m.getData()[1] + " recieved by BS");
			return true;
		}
		return false;
	}
	
	public boolean upperMessage(IPacket m) {
		// AppMessage msg = new AppMessage(m);	
		// return lower.sendMessage(m);
		return true;
	}
	
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"bs init " + mot.getID());
	}
}
