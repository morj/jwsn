package ru.amse.nikitin.sensnet;

/** 
 * Battery interface
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
