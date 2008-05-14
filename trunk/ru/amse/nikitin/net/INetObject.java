package ru.amse.nikitin.net;

import ru.amse.nikitin.simulator.IActiveObject;

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
	/** getter for last arrived message source */
	int getLastMessageSource();
	/** getter for last arrived message destanation */
	int getLastMessageDest();
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
