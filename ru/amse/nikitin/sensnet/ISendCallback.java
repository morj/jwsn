package ru.amse.nikitin.sensnet;

import ru.amse.nikitin.simulator.IMessage;

/**
 * @author Pavel A. Nikitin
 *
 */
public interface ISendCallback {
	void run(IMessage msg);
}
