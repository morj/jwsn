package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.net.IPacket;
import ru.amse.nikitin.net.impl.Packet;
import ru.amse.nikitin.sensnet.ISendCallback;
import ru.amse.nikitin.sensnet.IWirelessPacket;

public class WirelessPacket extends Packet implements IWirelessPacket {
	private int id = 0;
	
	protected ISendCallback onSendAction = null;
	
	public WirelessPacket(int id, Mot owner) {
		super(owner);
		this.id = id;
		length = 1;
	}
	
	public IPacket decapsulate() {
		if (id == -1) {
			if (data != null) {
				return (IPacket)data;
			} else {
				return null;
			}
		} else {
			return super.decapsulate();
		}
	}
	
	public ISendCallback getOnSendAction() {
		return onSendAction;
	}

	public void setOnSendAction(ISendCallback onSendAction) {
		this.onSendAction = onSendAction;
	}

	public int getID() {
		return id;
	}

	public String toString() {
		return (isLocked ? "![WP " : "[WP") + id + ", "
		            + (data == null ? "no data" : data.toString()) + " ]";
	}

}
