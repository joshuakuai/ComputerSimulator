package edu.gwu.cs6461.sim.common;

public enum ConditionCode {

	OVERFLOW(1), 
	UNDERFLOW(2), 
	DIVZERO(3), 
	EQWUALORNOT(4), 
	NOTEXIST(-1);

	private final int code;

	private ConditionCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public ConditionCode fromCode(int code) {

		for (ConditionCode value : ConditionCode.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		return NOTEXIST;

	}

}
