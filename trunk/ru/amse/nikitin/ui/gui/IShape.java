package ru.amse.nikitin.ui.gui;

import java.awt.Graphics;

/**
 * @author Pavel A. Nikitin
 * shape interface
 *
 */
public interface IShape {
	/** drawing method */
	void draw(Graphics g);
	/** erasing method */
	void erase(Graphics g);
}
