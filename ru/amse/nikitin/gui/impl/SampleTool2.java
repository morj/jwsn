package ru.amse.nikitin.gui.impl;

import java.awt.event.MouseEvent;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;
import ru.amse.nikitin.gui.ITool;
import ru.amse.nikitin.gui.Const;

public class SampleTool2 implements ITool {
	protected IActiveObjectDesc desc = null;
	
	public void mouseMoved(MouseEvent arg0) {
		/* int x = affectedDesc.getX();
		int y = affectedDesc.getY();
		int dx = x - arg0.getX();
		int dy = y - arg0.getY(); */
		if (desc != null) {
			// System.err.println(desc.getName());
			desc.setX(arg0.getX() - Const.POINT_X_SIZE / 2);
			desc.setY(arg0.getY() - Const.POINT_Y_SIZE / 2);
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		desc = null;
	}

	public void mousePressed(MouseEvent arg0, IActiveObjectDesc affectedDesc) {
		desc = affectedDesc;
		// System.err.println(desc.getName());
	}

}
