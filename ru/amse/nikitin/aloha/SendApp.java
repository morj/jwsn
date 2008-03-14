package ru.amse.nikitin.aloha;

import ru.amse.nikitin.activeobj.ELogMsgType;
import ru.amse.nikitin.activeobj.impl.Logger;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.WirelessPacket;
import ru.amse.nikitin.sensnet.util.EmptyApp;
import ru.amse.nikitin.sensnet.util.BsData;

public class SendApp extends EmptyApp {
	protected static int helloCount = 0;
	
	final Runnable step = new Runnable() {
		public void run () {
			// int[] data = new int [2];
			// data[0] = Const.hello;
			// data[1] = ++helloCount;
			BsData data = new BsData(Const.hello, ++helloCount);
			WirelessPacket packet = new WirelessPacket(3);
			Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
					"allocated " + helloCount);
			packet.setData(data);
			sendMsgToLower(packet);
			scheduleEvent(this, 15);
		}
		
	};
	
	private void sendMsgToLower(WirelessPacket packet) {
		getGate("lower").recieveMessage(packet, this);
	}
	
	public SendApp(Mot m) {
		super(m);
	}
	public void init(IGraph<Integer> topology) {
		Logger.getInstance().logMessage(ELogMsgType.INFORMATION,
			"snd init " + mot.getID());
		step.run();
	}
}
