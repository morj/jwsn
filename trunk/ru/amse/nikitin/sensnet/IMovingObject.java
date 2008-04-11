package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.INetObject;

/**
 * Moving Object interface. 
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMovingObject extends INetObject {
	/** distance to other Moving Object */
	double squaredDistanceTo(IMovingObject m);
	/** x getter */
	int getX();
	/** y getter */
	int getY();
}
