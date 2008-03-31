package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.net.impl.NetObject;
import ru.amse.nikitin.net.impl.Packet;

public class MonitoredPacket extends Packet {

	public MonitoredPacket(NetObject owner) {
		super(owner);
	}

}
