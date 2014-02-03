/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

/**
 * 
 * @author Ahmed
 */
public class Register {

	private int data = 0;
	private int size = 0;

	public int getData() {
		return data;
	}

	public int getSize() {
		return size;
	}

	public void setData(int newData) {
		data = newData;
		CPUController.shareInstance().checkSingleStepModel();
	}

	public void setSize(int newSize) {
		size = newSize;
	}

}