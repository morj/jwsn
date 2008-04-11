package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.IPacket;

/**
 * MonitoredPacket interface.
 * Monitored Packet is Packet for Monitored Object.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMonitoredPacket extends IPacket {
	/** type getter */
	int getType();
}
