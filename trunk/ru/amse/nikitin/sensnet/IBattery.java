package ru.amse.nikitin.sensnet;

/** 
 * Battery interface.
 * Battery determines how long a mot can act normally.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IBattery {
	/** capacity getter */
	double getCapacity();
	/** drained property */
	boolean drain();
}
