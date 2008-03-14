package ru.amse.nikitin.net;

public interface IModule {
	boolean recieveMessage(IPacket m);
	IGate declareGate(String name);
	IGate getGate(String name);
	void setArrivedOn(String arrivedOn);
}
