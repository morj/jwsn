package ru.amse.nikitin.models.centralized;

import ru.amse.nikitin.protocols.app.BsApp;
import ru.amse.nikitin.protocols.mac.centralized.BsMac;
import ru.amse.nikitin.protocols.routing.centralized.BsNet;
import ru.amse.nikitin.sensnet.IMotModuleFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;

public class BsMotFactory implements IMotModuleFactory {
	private static IMotModuleFactory instance = null;
	
	public static IMotModuleFactory getInstance () {
		if (instance == null) {
			instance = new BsMotFactory();
		}
		return instance;
	}

	public int getModuleCount() {
		return 3;
	}
	
	public MotModule createModule(Mot m, int index) {
		switch (index) {
			case 0: return Mac(m);
			case 1: return Net(m);
			case 2: return App(m);
			default: return null;
		}
	}

	protected MotModule App(Mot m) {
		return new BsApp(m);
	}

	protected MotModule Net(Mot m) {
		return new BsNet(m);
	}

	protected MotModule Mac(Mot m) {
		return new BsMac(m);
	}

	private BsMotFactory () {
	}
}
