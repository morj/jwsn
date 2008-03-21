package ru.amse.nikitin.sensnet.impl;

import javax.swing.ImageIcon;

import ru.amse.nikitin.activeobj.IActiveObjectDesc;

public class MotDescription implements IActiveObjectDesc {
	protected ImageIcon image;
	protected String name;
	protected int x;
	protected int y;
	protected Mot owner;

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

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	/* package-private */ void setOwner(Mot owner) {
		this.owner = owner;
	}

	public void invalidate() {
	}

}
