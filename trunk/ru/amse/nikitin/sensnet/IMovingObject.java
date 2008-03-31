package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.INetObject;

public interface IMovingObject extends INetObject {
	double squaredDistanceTo(IMovingObject m);
	int getX();
	int getY();
}
