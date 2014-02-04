/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import java.util.*;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;

/**
 * 
 * @author Ahmed
 */
public class Memory extends Observable {
	private Vector<Integer> Mem = new Vector<Integer>();
	private int size = 8192;
	private static final Memory instance = new Memory();

	private Memory() {
		for (int i = 0; i < size; i++) {
			Mem.addElement(0);
		}
		System.out.println("MEM=" + Mem.size());
	}

	// Singleton Method
	public static Memory shareInstance() {
		return instance;
	}

	public int getMem(int Address) {
		return Mem.elementAt(Address);
	}

	public void setMem(int Address, int Data) {
		Mem.set(Address, Data);

		HardwareData hardwareData = new HardwareData();
		hardwareData.put("MEMORY",
				Integer.toString(Address) + "," + Integer.toString(Data));

		this.notifyObservers(hardwareData);
	}
}
