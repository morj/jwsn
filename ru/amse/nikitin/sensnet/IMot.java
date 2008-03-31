package ru.amse.nikitin.sensnet;

public interface IMot extends IMovingObject {
	void createLinearTopology(int count);
	double getTransmitterPower();
	double getThreshold();
}
