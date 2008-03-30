package ru.amse.nikitin.simulator.util.xml;

import ru.amse.nikitin.simulator.IActiveObject;
import java.util.*;

public class FieldInfo {
	protected List<? extends IActiveObject> objects;
	protected int minHeight;
	protected int minWidth;
	public FieldInfo(List<? extends IActiveObject> objects) {
		this.objects = objects;
	}
	public int getMinHeight() {
		return minHeight;
	}
	/* package-private */ void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}
	public int getMinWidth() {
		return minWidth;
	}
	/* package-private */ void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}
	public List<? extends IActiveObject> getObjects() {
		return objects;
	}
	/* package-private */ void setObjects(List<? extends IActiveObject> objects) {
		this.objects = objects;
	}	
}
