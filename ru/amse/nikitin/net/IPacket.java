package ru.amse.nikitin.net;

import ru.amse.nikitin.net.impl.NetObject;

/**
 * Packet interface.
 * Packets are used to send data between modules within object.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IPacket {
	/** incapsulates another packet in current. incapsulating packet cannot handle data */
	boolean encapsulate(IPacket p);
	/** decapsulates another packet from current */
	IPacket decapsulate();
	/** checks if encapsulates */
	boolean isEncapsulating();
	/** sets a decapsulation lock (only locker can release/decapsulate) */
	boolean setLock(NetObject locker);
	/** releases a decapsulation lock (only locker can release/decapsulate) */
	boolean releaseLock(NetObject locker);
	/** checks if decapsulation lock is set */
	boolean isLocked();
	
	/** length getter */
	int getLength();
	/** data getter */
	Object getData();
	/** data setter */
	void setData(Object data) throws UnsupportedOperationException;
}
