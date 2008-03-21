package ru.amse.nikitin.net.impl;

import ru.amse.nikitin.net.IPacket;

public class Packet implements IPacket {
	protected Object data = null;
	protected int length = 0;
	protected boolean isIncapsulating = false;
	protected NetObject owner;
	protected boolean isLocked = false;
		
	public Packet(NetObject owner) {
		this.owner = owner;
	}
	
	public synchronized boolean setLock(NetObject locker) {
		if (owner == locker) {
			if (isIncapsulating) {
				((IPacket)data).setLock(locker);
			}
			isLocked = true;
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean releaseLock(NetObject locker) {
		if (owner == locker) {
			if (isIncapsulating) {
				isLocked = ((IPacket)data).setLock(locker);
			}
			isLocked = false;
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean isLocked() {
		return isLocked;
	}

	public boolean encapsulate(IPacket p) {
		if (!isIncapsulating) {
			if (p == null) {
				System.err.println("bad encapsulee");
			}
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
			if (isLocked) {
				System.err.println("packet is locked");
				return (IPacket)data;
			} else {
				IPacket p = (IPacket)data;
				length -= p.getLength();
				data = null;
				isIncapsulating = false;
				return p;
			}
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

	public String toString() {
		return (isLocked ? "![P " : "[P") +
			(data == null ? "no data" : data.toString())+ " ]";
	}
	
}
