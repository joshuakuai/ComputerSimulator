package edu.gwu.cs6461.sim.bridge;

import java.util.*;

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

	public void unRegister(Observer obs){
		observerList.remove(obs);
	}
}