package ru.amse.nikitin.protocols.rooting.centralized;

/* public */ class NetData {
	private int[] predecessors;

	public NetData(int[] predecessors) {
		this.predecessors = predecessors;
	}

	public int getPredecessor(int index) {
		return predecessors[index];
	}
	
}
