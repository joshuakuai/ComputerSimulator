/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.*;

/**
 * Register class
 * Holds the size, value, and name of the register
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
	//sets the value of the register both internally and on the GUI
	public void setData(int newData) {
		data = newData;
		String signBit=Integer.toBinaryString(newData);
		HardwareData hardwareData = new HardwareData();
		if ("IR".equals(this.name)) {
			hardwareData.put(this.name, Integer.toBinaryString(0x100000 | newData).substring(1));
		} else {
			if(signBit.substring(0,1).equals("1")&&signBit.length()==20){
				newData=Integer.parseInt(signBit.substring(1),2);
				newData*=-1;
				hardwareData.put(this.name, Integer.toString(newData));
				System.out.println("inside set");
			}
			else{

				hardwareData.put(this.name, Integer.toString(newData));
			}
		}
		this.notifyObservers(hardwareData);
		
		CPUController.shareInstance().checkSingleStepModel();
	}

	public void setSize(int newSize) {
		size = newSize;
	}

}