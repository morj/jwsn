package ru.amse.nikitin.protocols.rooting.centralized;

import java.util.Arrays;

/* public */ class NetData {
	private int[] predecessors;

	public NetData(int[] predecessors) {
		this.predecessors = predecessors;
	}

	public int getPredecessor(int index) {
		return predecessors[index];
	}

	public String toString() {
		return "[CND " + Arrays.toString(predecessors) + " ]";
	}
	
	
	
}
