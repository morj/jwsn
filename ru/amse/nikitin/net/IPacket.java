package ru.amse.nikitin.net;

public interface IPacket {
	boolean encapsulate(IPacket p);
	IPacket decapsulate();
	boolean isEncapsulating();
	
	int getLength();
	Object getData();
	void setData(Object data) throws UnsupportedOperationException;
}
