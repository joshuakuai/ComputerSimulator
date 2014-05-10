package edu.gwu.cs6461.sim.common;

/**
 * ALU Operator constant class
 * */
public enum ALUOperator {
	Addition("+"),
	Subtraction("-"),
	Division("/"),
	Multiply("*"),
	NA("?");
	
	private String operator;
	private ALUOperator(String operator) {
		this.operator = operator;
	}
	
	public String getOpt() {
		return operator;
	}
	
	public static ALUOperator fromOpt(String opt) {
		for (ALUOperator value : ALUOperator.values()) {
			if (value.getOpt().equals(opt)) {
				return value;
			}
		}

		return NA;
	}
	
}
