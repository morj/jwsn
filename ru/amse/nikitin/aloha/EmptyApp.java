package ru.amse.nikitin.aloha;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.graph.IGraph;


public class EmptyApp extends MotModule {
	public EmptyApp(Mot m) {
		super(m);
	}
	public boolean recieveMessage(IPacket m) {
		// AppMessage msg = new AppMessage(m);
		return lower.sendMessage(m); // just resend
	}
	public boolean sendMessage(IPacket m) {
		// AppMessage msg = new AppMessage(m);
		// return lower.sendMessage(m);
		return true;
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"init " + mot.getID());
	}
}
