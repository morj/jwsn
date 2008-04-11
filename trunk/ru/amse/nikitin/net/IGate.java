package ru.amse.nikitin.net;


/**
 * Gate interface.
 * Gates are used to connect modules within an active object.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IGate {
	/** a getter for gate name */
	String getName();
	/** a setter for gate's "from" field, representing its input */
	void setFrom(IGate from);
	/** a setter for gate's "to" field, representing its output */
	void setTo(IGate to);
	/** called to pass a packet to gate */
	boolean recieveMessage(IPacket msg, IModule pwner);
}
