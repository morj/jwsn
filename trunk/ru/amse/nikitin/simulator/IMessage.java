package ru.amse.nikitin.simulator;

/**
 * Message interface
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMessage extends Comparable {
	/** type getter */
	EMessageType getType();
	/** type setter */
	void setType(EMessageType t);
	/** source getter */
	int getSource();
	/** dest getter */
	int getDest();
	/** id getter */
	int getID();
	/** dest setter */
	void setDest(int id);
	/** data setter */
	void setData(Object d);
	/** data getter */
	Object getData();
}
