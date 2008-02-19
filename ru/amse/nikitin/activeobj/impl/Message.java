package ru.amse.nikitin.activeobj.impl;

import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.activeobj.EMessageType;

/**
 * @author Pavel A. Nikitin
 * Message implementation
 *
 */
class Message implements IMessage {
	protected EMessageType type = EMessageType.TIMER;
	protected Time timer;
	protected int source;
	protected int dest;
	protected int id;
	// protected Object data;
	protected int[] data;
	/* must be package-private */ public void setSource(int id) {
		source = id;
	}
	/* must be package-private */ public boolean isOnTime(Time t) {
		return (timer.compareTo(t) == 0);
	}
	public Message (Time t, int i) {
		timer = t;
		id = i;
	}
	public EMessageType getType() {
		return type;
	}
	public void setType(EMessageType t) {
		type = t;
	}
	public void setTimer(Time t) {
		timer.copyFrom(t);
	}
	public void delay(Time t) {
		timer.addFrom(t);
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
	public void setID(int i) {
		id = i;
	}
	public int compareTo(Object o) {
		return timer.compareTo(((Message)o).timer);
	}
	/* public void setData(Object o) {
		data = o;
	}
	public Object getData() {
		return data;
	} */
	public void setData(int[] d) {
		data = d;
	}
	public int[] getData() {
		return data;
	}
	public String toString() {
		String res = "type: " + type + " id: " + id + " from: " + source + " to " + dest
			+ " data:";
		for (int i = 0; i < data.length; i++) {
			res += " ";
			res += data[i];
		}
		return res;
	}
}
