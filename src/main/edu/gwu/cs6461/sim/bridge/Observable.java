package edu.gwu.cs6461.sim.bridge;

import java.util.*;


/** 
 * 
 * Observer pattern for data communication between GUI and back-end hareware logic  
 * 
 * Each hardware piece, like Registers, memory, that have data to publish to GUI will extends 
 * this class, and will later publish data back to GUI by calling observer's refreshdata method
 * 
 */
public class Observable {
	public List<Observer> observerList = new ArrayList<Observer>();

	public void notifyObservers(HardwareData data){
		Iterator<Observer> iterator = observerList.iterator();
		while (iterator.hasNext()) {
			Observer observer = iterator.next();
			observer.refreshData(data);
		}
	}

	public void register(Observer obs){
		observerList.add(obs);
	}
	
	public void clear(){
		observerList.clear();
	}
	
	public void unRegister(Observer obs){
		observerList.remove(obs);
	}
}