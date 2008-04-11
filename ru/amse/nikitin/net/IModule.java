package ru.amse.nikitin.net;

/**
 * Module interface.
 * Modules are parts of active objects representing different logical entities.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IModule {
	/** called to pass a packet to module */
	boolean recieveMessage(IPacket m);
	/** declares a gate with specified name */
	IGate declareGate(String name);
	/** returns a gate with specified name if exist */
	IGate getGate(String name);
	/** setter for the "arrived on field" */
	void setArrivedOn(String arrivedOn);
}
