package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.impl.Packet;
import ru.amse.nikitin.sensnet.ISendCallback;
import ru.amse.nikitin.sensnet.IWirelessPacket;

public class WirelessPacket extends Packet implements IWirelessPacket {
	private final int dest;
	private final int uid;
	private static int count = 0;
	
	protected ISendCallback onSendAction = null;
	
	public WirelessPacket(int dest, Mot owner) {
		super(owner);
		this.dest = dest;
		length = 1;
		uid = count;
		count++;
	}
	
	public IPacket decapsulate() {
		if (dest == -1) {
			if (data != null) {
				return (IPacket)data;
			} else {
				return null;
			}
		} else {
			return super.decapsulate();
		}
	}
	
	public int getDest() {
		return dest;
	}

	public String toString() {
		return (isLocked ? "![WP" : "[WP") + uid + ": " + dest + ", "
		            + (data == null ? "no data" : data.toString()) + " ]";
	}
	
	/* public ISendCallback getOnSendAction() {
		return onSendAction;
	}

	public void setOnSendAction(ISendCallback onSendAction) {
		this.onSendAction = onSendAction;
	}

	public void removeOnSendAction() {
		onSendAction = null;
	} */
	
	public int hashCode() {
		return uid;
	}

}
