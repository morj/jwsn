package ru.amse.nikitin.sensnet;

/**
 * Mot interface.
 * Mots are fundamental components of sensor networks, i. e. base station or sensor.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMot extends IMovingObject {
	/** creates linear topology. useful for layered protocols */
	void createLinearTopology(int count);
	/** transmitter power getter */
	double getTransmitterPower();
	/** threshold getter */
	double getThreshold();
}
