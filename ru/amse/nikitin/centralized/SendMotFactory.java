package ru.amse.nikitin.centralized;

import ru.amse.nikitin.sensnet.IMotModuleFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;

public class SendMotFactory implements IMotModuleFactory {
	private static IMotModuleFactory instance = null;
	
	public static IMotModuleFactory getInstance () {
		if (instance == null) {
			instance = new SendMotFactory();
		}
		return instance;
	}

	public MotModule App(Mot m) {
		return new SendApp(m);
	}

	public MotModule Net(Mot m) {
		return new CommonNet(m);
	}

	public MotModule Mac(Mot m) {
		return new SimpleMac(m);
	}

	private SendMotFactory () {
	}
}
