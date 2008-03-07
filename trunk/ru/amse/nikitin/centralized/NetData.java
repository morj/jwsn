package ru.amse.nikitin.centralized;

public class NetData {
	int neighbour;

	public NetData(int neighbour) {
		this.neighbour = neighbour;
	}

	public int getPredecessor() {
		return neighbour;
	}
	
}
