package ru.amse.nikitin.protocols.routing.centralized;

import java.util.Arrays;

/* public */ class CentralizedNetData {
	private int[] predecessors;

	public CentralizedNetData(int[] predecessors) {
		this.predecessors = predecessors;
	}

	public int getPredecessor(int index) {
		return predecessors[index];
	}

	public String toString() {
		return "[CND " + Arrays.toString(predecessors) + " ]";
	}
	
	
	
}
