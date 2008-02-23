package ru.amse.nikitin.gui;

import java.awt.event.MouseEvent;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;

public interface ITool {
	void mousePressed(MouseEvent arg0, IActiveObjectDesc affectedDesc);
	void mouseMoved(MouseEvent arg0);
	void mouseReleased(MouseEvent arg0);
}
