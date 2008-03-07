package ru.amse.nikitin.activeobj;

// import ru.amse.nikitin.activeobj.impl.Time;

/**
 * @author Pavel A. Nikitin
 * Message interface
 *
 */
public interface IMessage extends Comparable {
	/* private boolean isOnTime(Time t); */ 
	EMessageType getType();
	void setType(EMessageType t);
	/* private void setTimer(Time t); */
	/* private void delay(Time t); */
	int getSource();
	int getDest();
	void setDest(int id);
	int getID();
	void setID(int i);
	void setData(Object d);
	Object getData();
}
