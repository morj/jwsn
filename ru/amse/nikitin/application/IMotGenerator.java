package ru.amse.nikitin.application;

import ru.amse.nikitin.sensnet.impl.Mot;

public interface IMotGenerator {
	Mot generateMot(int x_, int y_, double power, double threshol);
}
