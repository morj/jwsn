package ru.amse.nikitin.models.conflicter;

import ru.amse.nikitin.protocols.app.BsData;
import ru.amse.nikitin.protocols.app.Const;
import ru.amse.nikitin.protocols.app.EmptyApp;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.simulator.ELogMsgType;
import ru.amse.nikitin.simulator.impl.Logger;
import ru.amse.nikitin.simulator.impl.Time;
import ru.amse.nikitin.simulator.util.graph.IGraph;

public class ConflicterApp extends EmptyApp {
	protected final static Time offsetTime = new Time(6);
	protected final static Time someUnitsTime = new Time(3);
	protected final static Time oneUnitTime = new Time(0);
	
	protected static int helloCount = 1;
	
	final Runnable step = new Runnable() {
		public void run () {
			// int[] data = new int [2];
			// data[0] = Const.hello;
			// data[1] = ++helloCount;
			BsData data = new BsData(Const.hello, helloCount);
			WirelessPacket packet = new WirelessPacket(2, mot);
			packet.setData(data);
			if(sendMsgToLower(packet)) {
				scheduleEvent(this, someUnitsTime); // wait for next resend
				Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
						"allocated " + helloCount);
				helloCount++;
			} else {
				scheduleEvent(this, oneUnitTime); // resend
			}
		}
		
	};
	
	private boolean sendMsgToLower(WirelessPacket packet) {
		return getGate("lower").recieveMessage(packet, this);
	}
	
	public ConflicterApp(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"snd init " + mot.getID());
		scheduleEvent(step, offsetTime);
	}
}
