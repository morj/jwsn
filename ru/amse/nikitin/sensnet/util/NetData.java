package ru.amse.nikitin.sensnet.util;

public class NetData {
	int predecessor;

	public NetData(int neighbour) {
		this.predecessor = neighbour;
	}

	public int getPredecessor() {
		return predecessor;
	}
	
}
