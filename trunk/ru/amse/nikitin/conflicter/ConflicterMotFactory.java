package ru.amse.nikitin.conflicter;

import ru.amse.nikitin.sensnet.IMotModuleFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;

import ru.amse.nikitin.aloha.CommonNet;
import ru.amse.nikitin.aloha.CommonMac;

public class ConflicterMotFactory implements IMotModuleFactory {
	private static IMotModuleFactory instance = null;
	
	public static IMotModuleFactory getInstance () {
		if (instance == null) {
			instance = new ConflicterMotFactory();
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
		return new ConflicterApp(m);
	}

	protected MotModule Net(Mot m) {
		return new CommonNet(m);
	}

	protected MotModule Mac(Mot m) {
		return new CommonMac(m);
	}

	private ConflicterMotFactory () {
	}
}