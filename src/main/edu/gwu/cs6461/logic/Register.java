/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.*;

/**
 * 
 * @author Ahmed
 */
public class Register extends Observable{

	private int data = 0;
	private int size = 0;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getData() {
		return data;
	}

	public int getSize() {
		return size;
	}

	public void setData(int newData) {
		data = newData;
		
		HardwareData hardwareData = new HardwareData();
		if ("IR".equals(this.name)) {
			hardwareData.put(this.name, Integer.toBinaryString(0x100000 | newData).substring(1));
		} else 
			hardwareData.put(this.name, Integer.toString(newData));
		
		this.notifyObservers(hardwareData);
		
		CPUController.shareInstance().checkSingleStepModel();
	}

	public void setSize(int newSize) {
		size = newSize;
	}

}