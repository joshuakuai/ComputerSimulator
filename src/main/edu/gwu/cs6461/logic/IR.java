/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.common.HardwarePart;

/**
 * This class holds the IR instruction binary string.
 * The decoded parts are sent back to it for storage 
 * to be used by other classes.
 * it extends the Register class so has the normal properties of Register
 * but has the extension of holding the break down of the instruction 
 */
public class IR extends Register {
	private String IRstring = "";
	private int OpCode = 0;
	private int RFI1 = 0;
	private int RFI2 = 0;
	private int XFI = 0;
	private int Address = 0;
	private int Immed = 0;
	private int Indirect = 0;
	private int Trace = 0;
	private int count=0;
	private int LeftorRight=0;
	private int LogicalorArithmetic=0;
	private int cc=0;
	IR(int size, String IR) {
		super(size,"IR");
		IRstring = IR;
	}

	IR() {
		this(HardwarePart.IR.getBit(),"IR");
	}

	/*
	 * *****Get functions********************
	 */
	public int getCC(){
		return cc;
	}
	public int getCount(){
		return count;
	}
	public int getLeftorRight(){
		return LeftorRight;
	}
	public int getLogicalorArithmetic(){
		return LogicalorArithmetic;
	}
	public String getIRstring() {
		return IRstring;
	}

	public int getOpCode() {
		return OpCode;
	}

	public int getRFI1() {
		return RFI1;
	}

	public int getRFI2() {
		return RFI2;
	}

	public int getXFI() {
		return XFI;
	}

	public int getAddress() {
		return Address;
	}

	public int getImmed() {
		return Immed;
	}

	public int getIndirect() {
		return Indirect;
	}

	public int getTrace() {
		return Trace;
	}

	/*
	 * *****Set functions**********************
	 */
	public void setCC(int CC){
		cc=CC;
	}
	public void setCount(int Count){
		count= Count;
	}
	public void setLeftorRight(int LorR){
		LeftorRight=LorR;
	}
	public void setLogicalorArithmetic(int LorA){
		LogicalorArithmetic=LorA;
	}
	public void setOpCode(int OpCodeNew) {
		OpCode = OpCodeNew;
	}

	public void setRFI1(int RFI) {
		RFI1 = RFI;
	}

	public void setRFI2(int RFI) {
		RFI2 = RFI;
	}

	public void setXFI(int XFINew) {
		XFI = XFINew;
	}

	public void setAddress(int AddressNew) {
		Address = AddressNew;
	}

	public void setImmed(int ImmedNew) {
		Immed = ImmedNew;
	}

	public void setIndirect(int IndirectNew) {
		Indirect = IndirectNew;
	}

	public void setTrace(int TraceNew) {
		Trace = TraceNew;
	}
	
	public void setIRstring(String IRstring){
		this.setData(Integer.parseInt(IRstring,2));
				
		this.IRstring = IRstring;
	}

	/*
	 * *****Checking functions********************
	 */
	public boolean isEmpty() {
		if (IRstring == null || "".equals(IRstring.trim()) || this.getData() == 0) {
			return true;
		}
		return false;
	}
}
