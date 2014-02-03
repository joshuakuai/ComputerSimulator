package edu.gwu.cs6461.test;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.RegisterName;

public class SampleObservable implements Observable {

	List<Observer> listeners = new ArrayList<Observer>();

	HardwareData data;
	public SampleObservable() {
		data = new HardwareData();
		
		data.put(RegisterName.R1.getVal(), "001010100");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				notifyObservers();
				
			}
		}).start();
		
		
	}

	@Override
	public void notifyObservers() {
		for (Observer ob : listeners) {
			ob.refreshData(data);
		}
	}

	@Override
	public void register(Observer obs) {
		listeners.add(obs);
		
	}

	@Override
	public void unRegister(Observer obs) {
		listeners.remove(obs);
	}

}
