package ru.amse.nikitin.sensnet.impl;

// import ru.amse.nikitin.activeobj.IMessage;
import ru.amse.nikitin.sensnet.IGate;
import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.IMotModule;

public class Gate implements IGate {
	private IGate from;
	private IGate to;
	private IMotModule owner;
	private String name;

	public String getName() {
		return name;
	}

	public Gate(IMotModule owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	public IGate getFrom() {
		return from;
	}

	public void setFrom(IGate from) {
		this.from = from;
	}

	public IGate getTo() {
		return to;
	}

	public void setTo(IGate to) {
		this.to = to;
	}

	public boolean recieveMessage(IPacket msg, IMotModule pwner) {
		if (owner == pwner) {
			if (to != null) {
				return to.recieveMessage(msg, pwner);
			}
		} else {
			if (from != null) {
				if (owner != null) {
					// System.err.println(pwner + "->" + owner);
					owner.setArrivedOn(name);
					return owner.recieveMessage(msg);
				} else {
					System.err.println("Null owner! " + msg.getID());
				}
			}
		}
		System.err.println("bad gate logic");
		return false;
	}
	
}
