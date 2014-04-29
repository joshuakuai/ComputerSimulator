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
 * @Revised   Jan 20, 2014 - 11:24:39 AM 
 */
public class IR extends Register {
	/**intruction in binary form */
	private String IRstring = "";
	/**Opcode of the instruction */
	private int OpCode = 0;
	/**General register for the instruction */
	private int RFI1 = 0;
	/**General register for the instruction */
	private int RFI2 = 0;
	/**Index register for the instruction */
	private int XFI = 0;
	/**Address to main memory*/
	private int Address = 0;
	/**immediate value in the instruction */
	private int Immed = 0;
	/**indirect mode of the instruction */
	private int Indirect = 0;
	/**Trace mode of the instruction */
	private int Trace = 0;
	/**count for shift/rotate instructions */
	private int count=0;
	/**left or right shift/rotate */
	private int LeftorRight=0;
	/**logica or arithmetic shift/rotate */
	private int LogicalorArithmetic=0;
	/** Device id for IO instructions*/
	private int deviceID=0;
	/**Trap code in the instruction*/
	private int trapCode=0;
	/**Condition code for transfer instructions*/	
	private int cc=0;
	/**Float Registers index*/
	private int FRI =0;
	
	/** Constructor */
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
	
	public int getDeviceID() {
		return deviceID;
	}

	public int getTrapCode() {
		return trapCode;
	}
	public int getFRI(){
		return FRI;
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
	
	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public void setTrapCode(int trapCode) {
		this.trapCode = trapCode;
	}
	public void setFRI(int fri){
		FRI=fri;
	}

	/**
	 * check if instruction register is empty or not
	 */
	public boolean isEmpty() {
		if (IRstring == null || "".equals(IRstring.trim()) || this.getData() == 0) {
			return true;
		}
		return false;
	}
}
