package edu.gwu.cs6461.sim.bridge;

public interface Observable {
	public void notifyObservers();

	public void register(Observer obs);

	public void unRegister(Observer obs);
}