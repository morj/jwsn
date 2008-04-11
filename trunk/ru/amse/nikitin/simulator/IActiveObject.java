package ru.amse.nikitin.simulator;

import javax.swing.ImageIcon;

/**
 * Active Object interface
 * Active Objects are elements of any simulated network.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IActiveObject {
	/** id getter */
	int getID();
	/** id setter */
	void setID(int newID);
	/** create new description */ 
	IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y);
	/** description getter */
	IActiveObjectDesc getDesc();
	/** message reciever */
	boolean recieveMessage(IMessage m);
}
