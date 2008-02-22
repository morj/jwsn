package ru.amse.nikitin.hugetest;

import ru.amse.nikitin.activeobj.impl.Time;
import ru.amse.nikitin.graph.IGraph;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
// import ru.amse.nikitin.activeobj.impl.Logger;
// import ru.amse.nikitin.activeobj.ELogMsgType;

public class App extends MotModule {
	protected Time someUnitsTime = null;
	
	final Runnable event = new Runnable() {
		public void run () {
			// Logger.getInstance().logMessage(ELogMsgType.INFORMATION, "> " + mot.getID());
			// System.out.println(mot.getID());
		}
	};
	
	public App(Mot m) {
		super(m);
	}

	public void init(IGraph<Integer> topology) {
		int d = (mot.getID() % Const.width) + Const.offset;
		someUnitsTime = new Time(d);
		scheduleEvent(event, someUnitsTime);
	}

}
