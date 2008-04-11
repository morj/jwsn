package ru.amse.nikitin.net;

import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IMessage;

public interface INetObject extends IActiveObject {
	IGate getOutputGate();
	boolean recieveMessage(IMessage m);
	// int getLastMessageID();
	int getLastMessageSource();
	int getLastMessageDest();
	int getID();
	void setID(int newID);
	IGate declareInputGate(Class<? extends IPacket> msgClass);
	IGate getInputGate(Class<? extends IPacket> msgClass);
	boolean hasInputGate(Class<? extends IPacket> msgClass);
	IModule getModule(String name);
	IModule addModule(String name, IModule module);
	void createTopology();
}
