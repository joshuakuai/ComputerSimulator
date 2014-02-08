/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

/**
 * Decode the IR instructions into the various part opcode r x address I T
 */
public class Decode {
	private final static Logger logger = Logger.getLogger(Decode.class);
	
	private int OpCode = 0;
	private int base = 2;
	private int RFI1 = 0;
	private int XFI = 0;
	private int Address = 0;
	private int Indirect = 0;
	private int Immediate = 0;
	//depending on the opcode the functions decided which decode function to use to break down
	//the instruction
	public void decodeSwitch(IR IRobject) {
		OpCode = Integer.parseInt(IRobject.getIRstring().substring(0, 6), base);
		IRobject.setOpCode(OpCode);
		logger.debug("OpCode=" + OpCode);
		if (OpCode == 1)
			function1(IRobject);
		else if (OpCode == 2)
			function1(IRobject);
		else if (OpCode == 3)
			function1(IRobject);
		else if (OpCode == 41)
			function3(IRobject);
		else if (OpCode == 42)
			function3(IRobject);
		else if (OpCode == 4)
			function1(IRobject);
		else if (OpCode == 5)
			function1(IRobject);
		else if (OpCode == 6)
			function4(IRobject);
		else if (OpCode == 7)
			function4(IRobject);
	}
//shared function1, function3, function4 shared functions between the different 
//instructions
	public void function1(IR IRobject) {
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setRFI1(RFI1);
		logger.debug("RFI=" + RFI1);
		XFI = Integer.parseInt(IRobject.getIRstring().substring(8, 10), base);
		IRobject.setXFI(XFI);
		logger.debug("XFI=" + XFI);
		Indirect = Integer.parseInt(IRobject.getIRstring().substring(10, 11),
				base);
		IRobject.setIndirect(Indirect);
		logger.debug("Indirect=" + Indirect);
		Address = Integer.parseInt(IRobject.getIRstring().substring(12, 20),
				base);
		IRobject.setAddress(Address);
		logger.debug("Address=" + Address);
	}

	public void function3(IR IRobject) {
		XFI = Integer.parseInt(IRobject.getIRstring().substring(8, 10), base);
		IRobject.setXFI(XFI);
		logger.debug("XFI=" + XFI);
		Indirect = Integer.parseInt(IRobject.getIRstring().substring(10, 11),
				base);
		IRobject.setIndirect(Indirect);
		logger.debug("Indirect=" + Indirect);
		Address = Integer.parseInt(IRobject.getIRstring().substring(12, 20),
				base);
		IRobject.setAddress(Address);
		logger.debug("Address=" + Address);
	}

	public void function4(IR IRobject) {
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setRFI1(RFI1);
		logger.debug("RFI=" + RFI1);
		Immediate = Integer.parseInt(IRobject.getIRstring().substring(12, 20),
				base);
		IRobject.setImmed(Immediate);
		logger.debug("Immediate=" + Immediate);
	}

}
