package ru.amse.nikitin.sensnet.random;

import ru.amse.nikitin.sensnet.IMotModuleFactory;
import ru.amse.nikitin.sensnet.IRandomArea;
import ru.amse.nikitin.sensnet.impl.Mot;
import ru.amse.nikitin.ui.gui.impl.Util;

public class RandomArea_ implements IRandomArea {
	public static final double commonMotPower = 900;
	private static RandomArea_ instance = null;
	
	private RandomArea_() {};
	
	public static RandomArea_ getInstance () {
		if (instance == null) {
			instance = new RandomArea_();
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
		
		double t = coordInfo.getThreshold(0);
		mots[0] = new Mot(
			coordInfo.motsX[0], coordInfo.motsY[0],
			commonMotPower, t, sf
		);
		for (int i = 1; i < count - 1; i++) {
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
		
		
		mots[0].newDesc(
			Util.getInstance().createImageIcon("terminal_vs.gif"), "send " + 0,
			coordInfo.motsX[0], coordInfo.motsY[0]
		);
		for (int i = 1; i < count-1; i++) {
			mots[i].newDesc(
				Util.getInstance().createImageIcon("palm2_vs.gif"), "mot " + i,
				coordInfo.motsX[i], coordInfo.motsY[i]
			);
		}
		mots[count-1].newDesc(
			Util.getInstance().createImageIcon("laptop_vs.gif"), "bs " + (count-1),
			coordInfo.motsX[count-1], coordInfo.motsY[count-1]
		);
		
		return mots;
	}

}
