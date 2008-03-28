package ru.amse.nikitin.simulator;

// import ru.amse.nikitin.activeobj.impl.Time;

/**
 * @author Pavel A. Nikitin
 * Message interface
 *
 */
public interface IMessage extends Comparable { 
	EMessageType getType();
	void setType(EMessageType t);
	int getSource();
	int getDest();
	int getID();
	void setDest(int id);
	void setData(Object d);
	Object getData();
}
