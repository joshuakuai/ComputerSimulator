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
	NOTEXIST(-1,"-9999");
	
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
	public String getbVal(){
		return binVal;
	}
	/**
	 * return OpCode if it is defined
	 * 	return NOTEXIST if bits pattern is not defined.
	 * 
	 * @param bits
	 * @return
	 */
	public OpCode fromBit(String bits){
		if (bits == null || "".equals(bits.trim())) {
			return NOTEXIST;
		}
		
        for (OpCode value : OpCode.values()) {
            if (value.getbVal().equals(bits)) {
                return value;
            }
        }
        return NOTEXIST;
	}
}
