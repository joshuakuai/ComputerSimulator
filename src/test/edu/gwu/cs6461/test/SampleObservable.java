package edu.gwu.cs6461.test;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.HardwarePart;

public class SampleObservable extends Observable {

	List<Observer> listeners = new ArrayList<Observer>();

	HardwareData data;
	public SampleObservable() {
		data = new HardwareData();
		
		data.put(HardwarePart.MEMORY.getVal(), "10,001010100");
		data.put(HardwarePart.MEMORY.getVal(), "8,001010100");
		data.put(HardwarePart.MEMORY.getVal(), "0,001010100");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					for (int i = 0; i < 2; i++) {
						Thread.sleep(3000);
						if (i==0) {
							data.put(HardwarePart.MEMORY.getVal(), "10,001010100");
						} else if (i==1) {
							data.put(HardwarePart.MEMORY.getVal(), "9,001010100");
						}
						notifyObservers(data);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}).start();
		
		
	}


}
