package ru.amse.nikitin.protocols.app;

public class CarData {
	int x; int y;

	public CarData(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "[CD (" + x + ", " + y + ") ]";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
