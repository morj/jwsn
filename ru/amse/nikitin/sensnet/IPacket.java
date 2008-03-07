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
	String getName();
	int getID();
	int getLength();
	int[] getData();
	void setData(int[] data);
	void toIntArr(int[] arr, int offset);
	ISendCallback getOnSendAction();
	void setOnSendAction(ISendCallback onSendAction);
}
