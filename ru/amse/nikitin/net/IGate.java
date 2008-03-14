package ru.amse.nikitin.net;

public interface IGate {
	String getName();
	void setFrom(IGate from);
	void setTo(IGate to);
	boolean recieveMessage(IPacket msg, IModule pwner);
}
