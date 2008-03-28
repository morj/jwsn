package ru.amse.nikitin.protocols.app;

import ru.amse.nikitin.sensnet.IWirelessPacket;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.simulator.util.graph.IGraph;


public class EmptyApp extends MotModule {
	public EmptyApp(Mot m) {
		super(m);
	}
	public boolean lowerMessage(IWirelessPacket m) {
		return getGate("lower").recieveMessage(m, this); // just resend
	}
	public boolean upperMessage(IWirelessPacket m) {
		return true;
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"init " + mot.getID());
	}
}
