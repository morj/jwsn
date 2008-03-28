package ru.amse.nikitin.models;

import ru.amse.nikitin.sensnet.impl.Mot;

public interface IMotGenerator {
	Mot generateMot(int x_, int y_, double power, double threshol);
}
