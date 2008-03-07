package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.activeobj.IMessage;

public interface ISendCallback {
	void run(IMessage msg);
}
