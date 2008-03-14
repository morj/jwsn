package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.IPacket;

/** 
 * Packet
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IWirelessPacket extends IPacket {
	ISendCallback getOnSendAction();
	void setOnSendAction(ISendCallback onSendAction);
	int getID();
}
