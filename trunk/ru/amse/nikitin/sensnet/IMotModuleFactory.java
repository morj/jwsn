package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.sensnet.impl.MotModule;

/** 
 * Mot factory
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotModuleFactory {
	int getModuleCount();
	MotModule createModule(Mot m, int index);
}
