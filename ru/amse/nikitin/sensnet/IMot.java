package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.sensnet.impl.Mot;

public interface IMot {
	void createLinearTopology(int count);
	double squaredDistanceTo(Mot m);
	double getTransmitterPower();
	double getThreshold();
}
