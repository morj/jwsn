package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.activeobj.IMessage;

public interface IGate {
	String getName();
	void setFrom(IGate from);
	void setTo(IGate to);
	boolean recieveMessage(IPacket msg, IMotModule pwner);
}
