package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.sensnet.impl.Mot;

/**
 * Mot Generator interface.
 * Mot Generator generates mots at given position.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMotGenerator {
	/** generation method */
	Mot generateMot(int x_, int y_, double power, double threshold);
}
