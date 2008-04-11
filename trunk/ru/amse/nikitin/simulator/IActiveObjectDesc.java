package ru.amse.nikitin.simulator;

import javax.swing.ImageIcon;

/**
 * Active Object description interface.
 * 
 * @author Pavel A. Nikitin
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
	/** X coordinate setter */
	public void setX(int x);
	/** Y coordinate setter */
	public void setY(int y);
	public void invalidate();
}
