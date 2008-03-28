package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;

import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.IDispatcher;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Dispatcher;

public class MonitoredObject implements IActiveObject {
	protected int id;
	protected IDispatcher disp = Dispatcher.getInstance();
	
	public int getID() {
		return id;
	}
	
	public void setID(int newID) {
		id = newID;
	}
	
	public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
		return null;
	}
	
	public IActiveObjectDesc getDesc() {
		return null;
	}
	
	public void onInit() {
	}
	
	public boolean recieveMessage(IMessage m) {
		switch (m.getType()) {
			case INIT:
				onInit();
			break;
			case TIMER:
			break;
			default:
		}
		return false;
	}
	
	public boolean sendMessage(IMessage m) {
		return false;
	}
}
