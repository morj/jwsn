package ru.amse.nikitin.protocols.mac.centralized;

/* public */ class MacData {
	private int count;
	private int[] predecessors;

	public MacData(int count, int[] predecessors) {
		this.count = count;
		this.predecessors = predecessors;
	}

	public int getColor(int index) {
		return predecessors[index];
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
