package ru.amse.nikitin.models.hugetest;

import ru.amse.nikitin.sensnet.IMotModuleFactory;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;

public class Factory implements IMotModuleFactory {

	public int getModuleCount() {
		return 1;
	}

	public MotModule createModule(Mot m, int index) {
		return (index == 0) ? new App(m) : null;
	}

}
