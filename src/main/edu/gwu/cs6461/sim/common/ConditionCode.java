package edu.gwu.cs6461.sim.common;


/**
 *  A constants class for ConditionCode handling
 *  
 * @author marcoyeung
 *
 */
public enum ConditionCode {
	OVERFLOW(0),
	UNDERFLOW(1), 
	DIVZERO(2), 
	EQWUALORNOT(3), 
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
