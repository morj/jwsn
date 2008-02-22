package ru.amse.nikitin.sensnet.random;

import java.util.Random;

/* package-private */ class CoordInfo {
	/* package-private */ final int cellX;
	/* package-private */ final int cellY;
	/* package-private */ final int cellSizeX;
	/* package-private */ final int cellSizeY;
	/* package-private */ final int[] motsX;
	/* package-private */ final int[] motsY;
	protected final double commonMotPower;
	
	protected static final Random randomizer = new Random();
	
	/* package-private */ CoordInfo(int x, int y, int count, double commonMotPower) {
		this.commonMotPower = commonMotPower;
		
		cellX = (int)Math.ceil(Math.sqrt(x * count / y));
		cellY = (int)Math.ceil((double)(y * cellX) / x);
		cellSizeX = x / cellX;
		cellSizeY = y / cellY;
		
		// System.out.println(cellX + " " + cellY);
		// System.out.println(cellSizeX + " " + cellSizeY);
		
		motsX = new int[count];
		motsY = new int[count];
		
		for (int i = 0; i < count; i++) {
			int currX = i % cellX;
			int currY = i / cellX;
			
			// System.out.println(currX + " " + currY);
			
			motsX[i] = currX * cellSizeX + rand(cellSizeX);
			motsY[i] = currY * cellSizeY + rand(cellSizeY);
		}
	}
	
	protected int rand(int max) {
		int res = randomizer.nextInt() % max;
		return (res >= 0? res : -res);
	}
	
	/* package-private */ double getThreshold(int i) {
		int x = i % cellX;
		int y = i / cellX;
		double maxd = 1;
		double curr;
		
		if (y > 0) {
			curr = RandomArea.squaredDistance(
				motsX [i],
				motsY [i],
				motsX [i - cellX],
				motsY [i - cellX]
			);
			if (maxd < curr) maxd = curr;
		}
		if (x > 0) {
			curr = RandomArea.squaredDistance(
				motsX [i],
				motsY [i],
				motsX [i - 1],
				motsY [i - 1]
			);
			if (maxd < curr) maxd = curr;
		}
		if (y < cellY - 2) {
			curr = RandomArea.squaredDistance(
				motsX [i],
				motsY [i],
				motsX [i + cellX],
				motsY [i + cellX]
			);
			if (maxd < curr) maxd = curr;
		}
		if ((i < motsX.length - 1) && (x < cellX - 1)) {
			curr = RandomArea.squaredDistance(
					motsX [i],
					motsY [i],
					motsX [i + 1],
					motsY [i + 1]
				);
			if (maxd < curr) maxd = curr;
		}
		if ((y == cellY - 2) && (x < cellX - 2)) {
			curr = RandomArea.squaredDistance(
				motsX [i],
				motsY [i],
				motsX [i + cellX],
				motsY [i + cellX]
			);
			if (maxd < curr) maxd = curr;
		}
		
		// System.out.println(commonMotPower / maxd);
		return (commonMotPower / maxd) - 0.01;
	}
}
