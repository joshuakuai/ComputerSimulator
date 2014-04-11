package edu.gwu.cs6461.sim.common;


/**
 * Opcode constants supported by this simulator
 * 
 * @author marcoyeung
 *
 */
public enum OpCode {
	HLT(0,"000000"),
	TRAP(30,"011110"),
	LDR(1,"000001"),
	STR(2,"000010"),
	LDA(3,"000011"),
	LDX(41,"101001"),
	STX(42,"101010"),
	AMR(4,"000100"),
	SMR(5,"000101"),
	AIR(6,"000110"),
	SIR(7,"000111"),
	JZ(10,"001010"),
	JNE(11,"001011"),
	JCC(12,"001100"),
	JMP(13,"001101"),
	JSR(14,"001110"),
	RFS(15,"001111"),
	SOB(16,"010000"),
	JGE(17,"010001"),
	MLT(20,"010100"),
	DVD(21,"010101"),
	TRR(22,"010110"),
	AND(23,"010111"),
	ORR(24,"011000"),
	NOT(25,"011001"),
	SRC(31,"011111"),
	RRC(32,"100000"),
	FADD(33,"100001"),
	FSUB(34,"100010"),
	VADD(35,"100011"),
	VSUB(36,"100100"),
	CNVRT(37,"100101"),
	LDFR(50,"110010"),
	STFR(51,"110011"),
	IN(61,"111101"),
	OUT(62,"111110"),
//	CHK(63,"111111"),
	DATA(99,"000000-"),
	EOP(55,"110111",false),
	NOTEXIST(-1,"-1111111",false);
	
	/**determine whether this opcode can be display in front-end Gui*/
	private final boolean editable;
	/**
	 * 6 bits fixed length in unsigned binary form
	 */
	private final String binVal; 
	/**
	 * OpCode in integer form
	 */
	private final int iVal;
	
	private OpCode(int iVal, String binVal) {
		this(iVal, binVal,true);
	}
	private OpCode(int iVal, String binVal,boolean editable) {
		this.iVal = iVal;
		this.binVal = binVal;
		this.editable = editable;
	}
	
	/**
	 *Return true is this OpCode can also be used in Front-end GUI 
	 * */
	public boolean isEditable(){
		return editable;
	}
	
	/**
	 * return the integer value of opcode 
	 * @return   integer value of opcode
	 */
	public int getCode(){
		return iVal;
	}
	
	/**
	 * return the bit value of opcode
	 * @return  bit value of opcode
	 */
	public String getbStr(){
		return binVal;
	}
	/**
	 * return OpCode if it is defined
	 * 	return NOTEXIST if bits pattern is not defined.
	 * 
	 * @param bits
	 * @return    OpCode if it is defined
	 */
	public static OpCode fromBit(String bits){
		if (bits == null || "".equals(bits.trim())) {
			return NOTEXIST;
		}
		
        for (OpCode value : OpCode.values()) {
            if (value.getbStr().equals(bits)) {
                return value;
            }
        }
        return NOTEXIST;
	}
	
	/***
	 * Return the OpCode enum object if opcode value is provided
	 * @param code
	 * @return OpCode enum object
	 */
	public static OpCode fromCode(int code) {
		
        for (OpCode value : OpCode.values()) {
            if (value.getCode()== code) {
                return value;
            }
        }
        return NOTEXIST;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(name()).append(",").append(getbStr()).append(",")
		.append(getCode()).append("]");
		return sb.toString();
	}
}
