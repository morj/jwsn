package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.Const;

public class Util {
	/* package-private */ static String moduleName(int index) {
		switch(index) {
			case  0: return Const.MAC_NAME;
			case  1: return Const.NET_NAME;
			case  2: return Const.APP_NAME;
			default: return Const.MDL_PREFIX + index;
		}
	}
}
