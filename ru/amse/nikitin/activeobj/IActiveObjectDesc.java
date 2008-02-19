package ru.amse.nikitin.activeobj;

import javax.swing.ImageIcon;

/**
 * @author Pavel A. Nikitin
 * Active object description interface
 *
 */
public interface IActiveObjectDesc {
	/** image getter */
	public ImageIcon getImage();
	/** name getter */
	public String getName();
	/** X coordinate getter */
	public int getX();
	/** Y coordinate getter */
	public int getY();
}
