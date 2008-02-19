package ru.amse.nikitin.activeobj;

/**
 * @author Pavel A. Nikitin
 * UI listener interface
 *
 */
public interface IDisplayListener {
	/** on object add */
	void objectAdded(int id, IActiveObjectDesc desc);
	/** on decription changed */
	void descChanged(int id);
	/** on message conflicted */
	void messageConflicted(int source, int dest);
	/** on message recieved */
	void messageRecieved(int source, int dest, int[] data);
	/** on message accepted by dest object */
	void messageAccepted(int dest);
	/** on notification from mot arrived */
	void notificationArrived(int id, String notification);
	/** on step started */
	void stepStarted();
	/** on step performed */
	void stepPerformed();
}
