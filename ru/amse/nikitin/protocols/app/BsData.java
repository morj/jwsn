package ru.amse.nikitin.protocols.app;

public class BsData {
	int type;
	int index;
	
	public BsData(int type, int index) {
		this.type = type;
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		return "[BD " + index + " (" + type + ") ]";
	}
	
}
