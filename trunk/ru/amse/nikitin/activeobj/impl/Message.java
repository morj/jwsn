package ru.amse.nikitin.activeobj.impl;

import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.IActiveObject;
import ru.amse.nikitin.activeobj.EMessageType;

/**
 * @author Pavel A. Nikitin
 * Message implementation
 *
 */
public class Message implements IMessage {
	protected EMessageType type = EMessageType.TIMER;
	protected Time timer;
	protected int source;
	protected int dest;
	protected int id;
	protected Object data;
	private IActiveObject owner;
	
	/* package-private */ void setTimer(Time t) {
		timer.copyFrom(t);
	}
	
	/* package-private */ void delay(Time t) {
		timer.addFrom(t);
	}
	
	/* package-private */ void setSource(int id) {
		source = id;
	}
	
	/* package-private */ public boolean isOnTime(Time t) {
		return (timer.compareTo(t) == 0);
	}
	
	/* package-private */ void setID(int i) {
		id = i;
	}
	
	/* package-private */ void setOwner(IActiveObject owner) {
		this.owner = owner;
	}
	
	/* package-private */ IActiveObject getOwner() {
		return owner;
	}

	public Message(MessageInitData data) {
		timer = data.getT();
		id = data.getI();
	}
	
	public EMessageType getType() {
		return type;
	}
	
	public void setType(EMessageType t) {
		type = t;
	}
	
	public void setDest(int id) {
		dest = id;
	}
	
	public int getSource() {
		return source;
	}
	
	public int getDest() {
		return dest;
	}
	
	public int getID() {
		return id;
	}
	
	public int compareTo(Object o) {
		return timer.compareTo(((Message)o).timer);
	}
	
	public void setData(Object o) {
		data = o;
	}
	
	public Object getData() {
		return data;
	}
	
	public String toString() {
		String res = "type: " + type + " id: " + id + " from: " + source + " to " + dest
			+ " data:";
		/* for (int i = 0; i < data.length; i++) {
			res += " ";
			res += data[i];
		} */
		res += data;
		return res;
	}

}
