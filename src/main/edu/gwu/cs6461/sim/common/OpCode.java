package edu.gwu.cs6461.sim.common;


/**
 * Opcode constants supported by this simulator
 * 
 * @author marcoyeung
 *
 */
public enum OpCode {
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
	IN(61,"111101"),
	OUT(62,"111110"),
	CHK(63,"111111"),
	NOTEXIST(-1,"-1111111");
	
	/**
	 * 6 bits fixed length in unsigned binary form
	 */
	private final String binVal; 
	/**
	 * OpCode in integer form
	 */
	private final int iVal;
	
	private OpCode(int iVal, String binVal) {
		this.iVal = iVal;
		this.binVal = binVal;
	}

	/**
	 * return the integer value of opcode 
	 * @return
	 */
	public int getiVal(){
		return iVal;
	}
	
	/**
	 * return the bit value of opcode
	 * @return
	 */
	public String getbStr(){
		return binVal;
	}
	/**
	 * return OpCode if it is defined
	 * 	return NOTEXIST if bits pattern is not defined.
	 * 
	 * @param bits
	 * @return
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
	public static OpCode fromCode(int code) {
		
        for (OpCode value : OpCode.values()) {
            if (value.getiVal()== code) {
                return value;
            }
        }
        return NOTEXIST;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(name()).append(",").append(getbStr()).append(",")
		.append(getiVal()).append("]");
		return sb.toString();
	}
}
