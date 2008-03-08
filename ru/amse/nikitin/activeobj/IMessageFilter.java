package ru.amse.nikitin.activeobj;
import java.util.*;

/**
 * @author Pavel A. Nikitin
 * message filter interface
 *
 */
public interface IMessageFilter {
	/** message filtering */
	void Filter(List<IActiveObject> objs, Queue<IMessage> messages,
			// Queue<IMessage> dropped,
			List<IDisplayListener> dispListeners);
}