package ru.amse.nikitin.conflicter;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.Packet;

import ru.amse.nikitin.aloha.Const;
import ru.amse.nikitin.aloha.EmptyApp;

public class ConflicterApp extends EmptyApp {
	protected final static Time period = new Time(3);
	protected final static Time startTime = new Time(6);
	
	final Runnable step = new Runnable() {
		public void run () {
			System.out.println("conflicter step " + mot.getID());
			int[] data = new int [2];
			data[0] = Const.hello;
			data[1] = 0;
			Packet packet = new Packet(1);
			Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
					"allocated conflicter message");
			packet.setData(data);
			lower.sendMessage(packet);
			scheduleEvent(this, period);
		}
	};
	
	public ConflicterApp(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"snd init " + mot.getID());
		
		scheduleEvent(step, startTime);
	}
}
