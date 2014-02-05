package edu.gwu.cs6461.sim.bridge;


/**
 * Observer pattern for data communication between GUI and back-end hareware logic
 * 
 * Observer will be implemented by objects that need the state update from Observable object.
 * The data are passed through refreshData method from observable object to all the 
 * observers. 
 * 
 * 
 * @author marcoyeung
 *
 */
public interface Observer {

	public void refreshData(HardwareData subject);
	
}
