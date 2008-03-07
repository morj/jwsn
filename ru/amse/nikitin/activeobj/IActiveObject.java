package ru.amse.nikitin.activeobj;

import javax.swing.ImageIcon;

/**
 * @author Pavel A. Nikitin
 * Active object interface
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
