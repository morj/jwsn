package ru.amse.nikitin.net;

import ru.amse.nikitin.net.impl.NetObject;

public interface IPacket {
	boolean encapsulate(IPacket p);
	IPacket decapsulate();
	boolean isEncapsulating();
	boolean setLock(NetObject locker);
	boolean releaseLock(NetObject locker);
	boolean isLocked();
	
	int getLength();
	Object getData();
	void setData(Object data) throws UnsupportedOperationException;
}
