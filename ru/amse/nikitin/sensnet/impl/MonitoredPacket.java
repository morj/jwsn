package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.IMonitoredPacket;
import ru.amse.nikitin.net.impl.NetObject;
import ru.amse.nikitin.net.impl.Packet;

public class MonitoredPacket extends Packet implements IMonitoredPacket {
	protected int type;

	public MonitoredPacket(NetObject owner, int type) {
		super(owner);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public MonitoredPacket(NetObject owner) {
		super(owner);
	}

}
