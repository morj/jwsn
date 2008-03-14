package ru.amse.nikitin.net.impl;

import ru.amse.nikitin.net.IGate;
import ru.amse.nikitin.net.IModule;
import ru.amse.nikitin.net.IPacket;

public class Gate implements IGate {
	private IGate from;
	private IGate to;
	private IModule owner;
	private String name;

	public String getName() {
		return name;
	}

	public Gate(IModule owner, String name) {
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

	public boolean recieveMessage(IPacket msg, IModule pwner) {
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
					System.err.println("Null owner in " + msg);
				}
			}
		}
		System.err.println("bad gate logic");
		return false;
	}
	
}
