package ru.amse.nikitin.centralized;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Packet;

public class SendApp extends EmptyApp {
	protected final static Time someUnitsTime = new Time(15);
	protected static int helloCount = 0;
	
	final Runnable step = new Runnable() {
		public void run () {
			int[] data = new int [2];
			data[0] = Const.hello;
			data[1] = ++helloCount;
			Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
					"allocated " + helloCount);
			Packet packet = new Packet(3);
			packet.setData(data);
			lower.sendMessage(packet);
			scheduleEvent(this, someUnitsTime);
		}
	};
	
	public SendApp(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"snd init " + mot.getID());
		scheduleEvent(step, new Time(0));
	}
}
