package ru.amse.nikitin.net.impl;

import ru.amse.nikitin.net.IPacket;

public class Packet implements IPacket {
	protected Object data = null;
	protected int length;
	protected boolean isIncapsulating = false;
		
	public boolean encapsulate(IPacket p) {
		if (!isIncapsulating) {
			data = p;
			length += p.getLength();
			isIncapsulating = true;
			return true;
		} else {
			return false;
		}
	}
	
	public IPacket decapsulate() {
		if (isIncapsulating) {
			IPacket p = (IPacket)data;
			length -= p.getLength();
			data = null;
			isIncapsulating = false;
			return p;
		} else {
			return null;
		}
	}
	
	public boolean isEncapsulating() {
		return isIncapsulating;
	}
	
	public int getLength() {
		return length;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) throws UnsupportedOperationException {
		// System.err.println(data.getClass());
		if (isIncapsulating) {
			throw new UnsupportedOperationException();
		} else {
			this.data = data;
		}
	}
	
}
