/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import java.util.*;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.util.Convertor;

/**
 * Memory class to store instructions and values
 */
@Deprecated
public class Memory extends Observable {
	private Vector<Integer> Mem = new Vector<Integer>();
	private int size = 8192;
	private static final Memory instance = new Memory();
//initialize the memory
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

	/**
	 * 
	 * @param address
	 * @param data  this simulator is 20bit only, int is enough to accommodate
	 */
	public void setMem(int address, int data) {
		Mem.set(address, data);
		
		String signedD = Integer.toBinaryString(data);
		int iVal = Convertor.getSignedValFromBin(signedD, SimConstants.WORD_SIZE);
		
		HardwareData hardwareData = new HardwareData();
		hardwareData.put(HardwarePart.MEMORY.getName(),
				Integer.toString(address) + "," + Integer.toString(iVal));

		this.notifyObservers(hardwareData);
	}
	
}
