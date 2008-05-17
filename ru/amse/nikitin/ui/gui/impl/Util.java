package ru.amse.nikitin.ui.gui.impl;
import javax.swing.ImageIcon;

import ru.amse.nikitin.ui.gui.IUtil;

public class Util implements IUtil {
	public static final String prefix0 = "/icons/";
	public static final String prefix1 = "/";
	
	private static Util instance = null;
	
	private final Class<? extends Util> this_class; // quick singleton reference
	
	private Util() {
		this_class = getClass();
	}

	public static Util getInstance() {
		if(instance == null) {
			instance = new Util();
		}
		return instance;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	public ImageIcon createImageIcon(String path) {
		String fullPath = prefix0 + path;
	    java.net.URL imgURL = this_class.getResource(fullPath);
	    if (imgURL == null) {
	    	fullPath = prefix1 + path;
	    	imgURL = this_class.getResource(fullPath);
	    }
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("couldn't find file: " + fullPath);
	        return null;
	    }
	}

}
