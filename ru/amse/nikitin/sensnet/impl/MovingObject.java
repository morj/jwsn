package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;
import ru.amse.nikitin.net.impl.NetObject;
import ru.amse.nikitin.sensnet.IMovingObject;
import ru.amse.nikitin.simulator.IActiveObjectDesc;

public class MovingObject extends NetObject implements IMovingObject{
	protected MotDescription description;
	protected int x, y;
	
	public MovingObject(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
		description = new MotDescription(image, name, x, y);
		description.setOwner(this);
		return description;
	}
	
	public double squaredDistanceTo(IMovingObject m) {
		return((x-m.getX())*(x-m.getX()) + (y-m.getY())*(y-m.getY()));
	}
	
	public IActiveObjectDesc getDesc() {
		return description;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if (this.x != x) {
			int oldX = this.x;
			description.setX(x);
			s.changeDesc(this, oldX, y);
		}
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if (this.y != y) {
			int oldY = this.y;
			description.setY(y);
			s.changeDesc(this, x, oldY);
		}
		this.y = y;
	}
	
}
