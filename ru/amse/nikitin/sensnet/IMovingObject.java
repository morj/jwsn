package ru.amse.nikitin.sensnet;

public interface IMovingObject {
	double squaredDistanceTo(IMovingObject m);
	int getX();
	int getY();
}
