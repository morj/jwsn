package ru.amse.nikitin.sensnet;

public interface IGate {
	String getName();
	void setFrom(IGate from);
	void setTo(IGate to);
	boolean recieveMessage(IPacket msg, IMotModule pwner);
}
