/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

/**
 * Decode the IR instructions into the various part opcode r x address I T immed
 */
public class Decode {
	private final static Logger logger = Logger.getLogger(Decode.class);
	
	private int OpCode = 0;
	private int base = 2;
	private int RFI1 = 0;
	private int RFI2 = 0;
	private int XFI  = 0;
	private int Address = 0;
	private int Indirect = 0;
	private int Immediate = 0;
	private int Count=0;
	private int LeftorRight=0;
	private int LogicalorArithmetic=0;
	private int cc=0;
	//depending on the opcode the functions decided which decode function to use to break down
	//the instruction
	public void decodeSwitch(IR IRobject) {
		OpCode = Integer.parseInt(IRobject.getIRstring().substring(0, 6), base);
		IRobject.setOpCode(OpCode);
		logger.debug("OpCode=" + edu.gwu.cs6461.sim.common.OpCode.fromCode(OpCode));
		if (OpCode == 1 || OpCode==2 || OpCode==3||OpCode==4||OpCode==5||OpCode==10||OpCode==11||OpCode==16||OpCode==17)
			function1(IRobject);
		else if (OpCode == 41 || OpCode==42 ||OpCode==13||OpCode==14)
			function3(IRobject);
		else if (OpCode == 6||OpCode==7)
			function4(IRobject);
		else if(OpCode == 20 || OpCode==21 || OpCode==22 || OpCode==23 || OpCode==24 || OpCode==25)
			function5(IRobject);
		else if(OpCode==31 || OpCode==32)
			function6(IRobject);
		else if(OpCode==15)
			function7(IRobject);		
		else if(OpCode==12)
			function8(IRobject);
		
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
	public void function5(IR IRobject){
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setRFI1(RFI1);
		RFI2 = Integer.parseInt(IRobject.getIRstring().substring(8, 10), base);
		IRobject.setRFI2(RFI2);
	}
	public void function6(IR IRobject){
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setRFI1(RFI1);		
		LogicalorArithmetic = Integer.parseInt(IRobject.getIRstring().substring(10, 11), base);
		IRobject.setLogicalorArithmetic(LogicalorArithmetic);;
		LeftorRight = Integer.parseInt(IRobject.getIRstring().substring(11, 12), base);
		IRobject.setLeftorRight(LeftorRight);
		Count = Integer.parseInt(IRobject.getIRstring().substring(15, 20), base);
		IRobject.setCount(Count);		
	}
	public void function7(IR IRobject){
		Immediate = Integer.parseInt(IRobject.getIRstring().substring(12, 20),base);
		IRobject.setImmed(Immediate);
		logger.debug("Immediate=" + Immediate);
	}
	public void function8(IR IRobject){
		cc = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setCC(cc);
		logger.debug("CC=" + cc);
		XFI = Integer.parseInt(IRobject.getIRstring().substring(8, 10), base);
		IRobject.setXFI(XFI);
		logger.debug("XFI=" + XFI);
		Indirect = Integer.parseInt(IRobject.getIRstring().substring(10, 11),base);
		IRobject.setIndirect(Indirect);
		logger.debug("Indirect=" + Indirect);
		Address = Integer.parseInt(IRobject.getIRstring().substring(12, 20), base);
		IRobject.setAddress(Address);
		logger.debug("Address=" + Address);
	}
}
