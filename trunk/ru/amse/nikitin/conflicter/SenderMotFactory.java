package ru.amse.nikitin.conflicter;

import ru.amse.nikitin.sensnet.IMotModuleFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;
import ru.amse.nikitin.aloha.*;

public class SenderMotFactory implements IMotModuleFactory {
	private static IMotModuleFactory instance = null;
	
	public static IMotModuleFactory getInstance () {
		if (instance == null) {
			instance = new SenderMotFactory();
		}
		return instance;
	}

	public MotModule App(Mot m) {
		return new SenderApp(m);
	}

	public MotModule Net(Mot m) {
		return new CommonNet(m);
	}

	public MotModule Mac(Mot m) {
		return new CommonMac(m);
	}

	private SenderMotFactory () {
	}
}
