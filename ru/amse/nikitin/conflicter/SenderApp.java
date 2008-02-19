package ru.amse.nikitin.conflicter;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Packet;
import ru.amse.nikitin.aloha.*;

public class SenderApp extends EmptyApp {
	protected final static Time someUnitsTime = new Time(6);
	protected static int helloCount = 0;
	
	
	final Runnable step = new Runnable() {
		public void run () {
			System.out.println("sender step " + mot.getID());
			int[] data = new int [2];
			data[0] = Const.hello;
			data[1] = ++helloCount;
			Packet packet = new Packet(1);
			Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
					"allocated " + helloCount);
			packet.setData(data);
			lower.sendMessage(packet);
			scheduleEvent(this, someUnitsTime);
		}
	};
	
	public SenderApp(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"snd init " + mot.getID());
		step.run();
	}
}
