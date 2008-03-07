package ru.amse.nikitin.sensnet;

/** 
 * Packet
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IPacket {
	boolean encapsulate(IPacket p);
	IPacket decapsulate();
	boolean isEncapsulating();
	
	int getID();
	int getLength();
	Object getData();
	void setData(Object data);
	
	ISendCallback getOnSendAction();
	void setOnSendAction(ISendCallback onSendAction);
}
