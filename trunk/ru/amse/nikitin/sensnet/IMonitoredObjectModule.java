package ru.amse.nikitin.sensnet;

/**
 * MonitoredObjectModule interface.
 * Monitored Object Modules are lightweight and provide only data reading method.
 * 
 * @author Pavel A. Nikitin
 *
 */
public interface IMonitoredObjectModule {
	void init();
	Object getReading();
}
