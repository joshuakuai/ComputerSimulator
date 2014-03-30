/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.bridge.*;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.SimConstants;
import edu.gwu.cs6461.sim.util.Convertor;

/**
 * Register class
 * Holds the size, value, and name of the register
 */
public class Register extends Observable{
	private final static Logger logger = Logger.getLogger(Register.class);
	private int data = 0;
//	private String dataStr = "";
	private boolean signedVal = false;
	private int size = 0;
	private String name;

	public Register(int size, String name) {
		this(size,name, false);
	}
	public Register(int size,String name, boolean signed) {
		this.size = size;
		this.name = name;
		this.signedVal = signed;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getData() {
		int dData=data;
		
		if (signedVal) {
			String signedData=Integer.toBinaryString(data);			
			if(signedData.substring(0,1).equals("1")&&signedData.length()== size){
				dData = Convertor.getSignedValFromBin(signedData, size);
			}
		}
		
		return dData;
	}

	public int getSize() {
		return size;
	}
	//sets the value of the register both internally and on the GUI
	public void setData(int newData) {
		data = newData;
		
		if(this.name.equals("SI") || this.name.equals("SS")){
			return;
		}
		
		HardwareData hardwareData = new HardwareData();
		if (HardwarePart.IR.getName().equals(this.name)) {
			hardwareData.put(this.name, Integer.toBinaryString(0x100000 | newData).substring(1));
		} else {
			hardwareData.put(this.name, Integer.toString(getData()));
		}
		this.notifyObservers(hardwareData);
		
		CPUController.shareInstance().checkSingleStepModel();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name:").append(name).append(",").append("data:")
				.append(data).append(",size:").append(size);
		return sb.toString();
	}
	public void setSize(int newSize) {
		size = newSize;
	}
}