/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.common.DeviceType;
import edu.gwu.cs6461.sim.common.OpCode;
import edu.gwu.cs6461.sim.common.SimConstants;

/**
 * Decode the IR instructions into the various part opcode r x address I T immed
 * @Revised   Apr 8, 2014 - 1:09:33 AM  
 */
public class Decode extends Observable {
	private final static Logger logger = Logger.getLogger(Decode.class);
	
	 /**store Operator code in int format */
	private int code = 0;
	
	/**the base of instruction, default is base 2: binary*/
	private int base = 2;
	/**General register reference variable*/
	private int RFI1 = 0;
	/**General register reference variable*/
	private int RFI2 = 0;
	/**Index register reference variable*/
	private int XFI  = 0;
	/**Address to main memory*/
	private int Address = 0;
	/**indirect mode of the instruction */
	private int Indirect = 0;
	/**immediate value in the instruction */
	private int Immediate = 0;
	/**count for shift/rotate instructions */
	private int Count=0;
	/**left or right shift/rotate */
	private int LeftorRight=0;
	/**logica or arithmetic shift/rotate */
	private int LogicalorArithmetic=0;
	/** Device id for IO instructions*/
	private int deviceID = 0;
	/**Condition code for transfer instructions*/
	private int cc=0;
	/** Float Registers index*/
	private int FRI=0;
	
	/**depending on the opcode the functions decided which decode function to use to break down
	the instruction */
	public void decodeSwitch(IR IRobject) {
		code = Integer.parseInt(IRobject.getIRstring().substring(0, 6), base);
		IRobject.setOpCode(code);
		logger.debug("OpCode=" + OpCode.fromCode(code));
		if (code == OpCode.LDR.getCode() || code==2 || code==3||code==4||code==5||code==10||code==11||code==16||code==17)
			function1(IRobject);
		else if (code == 41 || code==42 ||code==13||code==14)
			function3(IRobject);
		else if (code == 6||code==7)
			function4(IRobject);
		else if(code == 20 || code==21 || code==22 || code==23 || code==24 || code==25)
			function5(IRobject);
		else if(code==31 || code==32)
			function6(IRobject);
		else if(code==15)
			function7(IRobject);		
		else if(code==12)
			function8(IRobject);
		else if (code== 61 || code== 62)
			ioInstructions(IRobject);
		else if (code == 30) 
			trapInstructions(IRobject);
		else if(code == 33 || code==34 || code==50 || code==51)
			floats(IRobject);
		else if(code ==35 || code==36 || code==37)
			function1(IRobject);
		
		publishEngineerData(IRobject);
	}
	/**
	 * Generic functions to decode instructions
	 * 
	 * shared function1, function3, function4 shared functions between the different 
	instructions */
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
	/**
	 * Generic functions to decode instructions
	 * 
	 * */
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

	/**
	 * Generic functions to decode instructions
	 * */
	public void function4(IR IRobject) {
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setRFI1(RFI1);
		logger.debug("RFI=" + RFI1);
		Immediate = Integer.parseInt(IRobject.getIRstring().substring(12, 20),
				base);
		IRobject.setImmed(Immediate);
		logger.debug("Immediate=" + Immediate);
	}
	/**
	 * Generic functions to decode instructions
	 * */
	public void function5(IR IRobject){
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setRFI1(RFI1);
		RFI2 = Integer.parseInt(IRobject.getIRstring().substring(8, 10), base);
		IRobject.setRFI2(RFI2);
	}
	/**
	 * Generic functions to decode instructions
	 * */
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
	/**
	 * Generic functions to decode instructions
	 * */
	public void function7(IR IRobject){
		Immediate = Integer.parseInt(IRobject.getIRstring().substring(12, 20),base);
		IRobject.setImmed(Immediate);
		logger.debug("Immediate=" + Immediate);
	}
	/**
	 * Generic functions to decode instructions
	 * */
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
	
	/***
	 * Decode IN and OUT IO operations
	 * 
	 * @param IRobject
	 */
	public void ioInstructions(IR IRobject) {
		RFI1 = Integer.parseInt(IRobject.getIRstring().substring(6,8), base);
		IRobject.setRFI1(RFI1);
		logger.debug("RFI=" + RFI1);
		deviceID = Integer.parseInt(IRobject.getIRstring().substring(16, 20), base);
		IRobject.setDeviceID(deviceID);
		logger.debug("deviceID=" + DeviceType.fromId(deviceID) );
		
	}
	/**
	 * Decode TRAP instruction
	 * get the address where stores the trap instruction handler.
	 * */
	public void trapInstructions(IR IRobject) {
		int trapCode = Integer.parseInt(IRobject.getIRstring().substring(12, 20), base);
		IRobject.setTrapCode(trapCode);
	}
	public void floats(IR IRobject){
		FRI = Integer.parseInt(IRobject.getIRstring().substring(6, 8), base);
		IRobject.setFRI(FRI);
		logger.debug("FRI=" + FRI);
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

	private void publishEngineerData(IR ir) { 
		HardwareData hardwareData = new HardwareData();
		String strD= ""; 
		strD = ir.getOpCode() + SimConstants.MSG_TO_GUI_DELIMITER+ 
				ir.getRFI1() + SimConstants.MSG_TO_GUI_DELIMITER+
				ir.getRFI2() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getXFI() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getAddress() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getIndirect() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getImmed() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getDeviceID() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getLeftorRight() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getLogicalorArithmetic() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getCount() + SimConstants.MSG_TO_GUI_DELIMITER
				+ ir.getFRI() + SimConstants.MSG_TO_GUI_DELIMITER;

		hardwareData.put(SimConstants.ECONSOLE_DECODE_MSG, strD);
		this.notifyObservers(hardwareData);
	}

}

