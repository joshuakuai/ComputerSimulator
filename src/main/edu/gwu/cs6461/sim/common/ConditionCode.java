package edu.gwu.cs6461.sim.common;


/**
 *  A constants class for ConditionCode handling
 *  
 * @author marcoyeung
 *
 */
public enum ConditionCode {
	O(0),
	OVERFLOW(1),
	UNDERFLOW(2), 
	DIVZERO(4), 
	EQUALORNOT(8), 
	NORMAL(5), 
	NOTEXIST(-1);

	private final int code;

	private ConditionCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ConditionCode fromCode(int code) {

		for (ConditionCode value : ConditionCode.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		return NOTEXIST;

	}

}
