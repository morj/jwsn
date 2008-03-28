package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.simulator.IMessage;

public interface ISendCallback {
	void run(IMessage msg);
}
