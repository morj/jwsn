package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.IPacket;

/** 
 * WirelessPacket interface.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IWirelessPacket extends IPacket {
	// ISendCallback getOnSendAction();
	// void setOnSendAction(ISendCallback onSendAction);
	// void removeOnSendAction();
	/** destanation getter */
	int getDest();
}
