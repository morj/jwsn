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
	MotModule App(Mot m);
	MotModule Net(Mot m);
	MotModule Mac(Mot m);
}
