package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.IRandomArea;
import ru.amse.nikitin.sensnet.IMotModuleFactory;

import javax.swing.ImageIcon;

public class RandomArea implements IRandomArea {
	public static final double commonMotPower = 900;
	private static RandomArea instance = null;
	
	private RandomArea() {};
	
	public static RandomArea getInstance () {
		if (instance == null) {
			instance = new RandomArea();
		}
		return instance;
	}
	
	public static double squaredDistance(
			int x1, int y1, int x2, int y2) {
		double rx = x1 - x2;
		double ry = y1 - y2;
		return rx * rx + ry * ry;
	}
	
	public Mot[] getArea(int x, int y, int count,
			IMotModuleFactory sf,
			IMotModuleFactory cf,
			IMotModuleFactory bf, double bsPower) {
		Mot[] mots = new Mot[count];
		CoordInfo coordInfo = new CoordInfo(x, y, count, commonMotPower);
		
		// for (int i = 0; i < coordInfo.cellX; i++) {}
		
		// int sendCount = coordInfo.cellX;
		int sendCount = 2;
		double t;
		for (int i = 0; i < sendCount; i++) {
			t = coordInfo.getThreshold(i);
			mots[i] = new Mot(
				coordInfo.motsX[i], coordInfo.motsY[i],
				commonMotPower, t, sf
			);
		}
		for (int i = sendCount; i < count - 1; i++) {
			t = coordInfo.getThreshold(i);
			mots[i] = new Mot(
				coordInfo.motsX[i], coordInfo.motsY[i],
				commonMotPower, t, cf
			);
		}
		t = coordInfo.getThreshold(count - 1);
		mots[count-1] = new Mot(
			coordInfo.motsX[count-1], coordInfo.motsY[count-1],
			bsPower, t, bf
		);
		
		
		for (int i = 0; i < sendCount; i++) {
			mots[i].newDesc(new ImageIcon(
				"icons\\terminal_vs.gif"), "send " + i,
				coordInfo.motsX[i], coordInfo.motsY[i]
			);
		}
		for (int i = sendCount; i < count-1; i++) {
			mots[i].newDesc(new ImageIcon(
				"icons\\palm2_vs.gif"), "mot " + i,
				coordInfo.motsX[i], coordInfo.motsY[i]
			);
		}
		mots[count-1].newDesc(new ImageIcon(
			"icons\\laptop_vs.gif"), "bs " + (count-1),
			coordInfo.motsX[count-1], coordInfo.motsY[count-1]
		);
		
		return mots;
	}

}
