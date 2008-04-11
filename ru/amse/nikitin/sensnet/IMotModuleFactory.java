package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;

/** 
 * Mot Factory interface.
 * Mot Factory generates a bunch of related modules to be used in one Mot.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModuleFactory {
	/** how many Modules will be generated */
	int getModuleCount();
	/** creates Module for given Mot */
	MotModule createModule(Mot m, int index);
}
