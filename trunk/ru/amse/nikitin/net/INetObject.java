package ru.amse.nikitin.net;

import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IMessage;

/**
 * NetObject interface.
 * Net Objects are extended Active Objects used to simulate networks.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface INetObject extends IActiveObject {
	/** default output gate getter */
	IGate getOutputGate();
	/** called to pass a message to object */
	boolean recieveMessage(IMessage m);
	// int getLastMessageID();
	/** getter for last arrived message source */
	int getLastMessageSource();
	/** getter for last arrived message destanation */
	int getLastMessageDest();
	/** id setter */
	int getID();
	/** id getter */
	void setID(int newID);
	/** declares input gate for specifed class */
	IGate declareInputGate(Class<? extends IPacket> msgClass);
	/** returns input gate for specified class, if exists */
	IGate getInputGate(Class<? extends IPacket> msgClass);
	/** returns if input gate for specified class exists */
	boolean hasInputGate(Class<? extends IPacket> msgClass);
	/** returns module with specified name */
	IModule getModule(String name);
	/** adds module with specified name */
	IModule addModule(String name, IModule module);
	/** connects all modules within object */
	void createTopology();
	/** raises an pop-up notification */
	void notification(String text);
}
