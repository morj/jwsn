package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.sensnet.impl.Mot;

/**
 * RandomArea interface.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IRandomArea {
	Mot[] getArea(int x, int y, int count,
			IMotModuleFactory sf,
			IMotModuleFactory cf,
			IMotModuleFactory bf, double bsPower);
}
