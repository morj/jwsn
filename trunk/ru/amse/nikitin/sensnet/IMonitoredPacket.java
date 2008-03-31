package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.net.IPacket;

public interface IMonitoredPacket extends IPacket {
	int getType();
}
