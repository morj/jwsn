package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;

public class MotDescription implements IActiveObjectDesc {
	protected ImageIcon image;
	protected String name;
	protected int x;
	protected int y;

	/* package-private */ MotDescription(ImageIcon image, String name, int x, int y) {
		this.image = image;
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public ImageIcon getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
