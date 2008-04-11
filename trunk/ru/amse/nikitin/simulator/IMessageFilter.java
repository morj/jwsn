package ru.amse.nikitin.simulator;
import java.util.List;
import java.util.Queue;

/**
 * Message Filter interface
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMessageFilter {
	/** message filtering */
	void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			// Queue<IMessage> dropped,
			List<IDisplayListener> dispListeners);
}
