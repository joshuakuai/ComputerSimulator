package edu.gwu.cs6461.sim.common;

public enum ALUOperator {
	Addition("+"),
	Subtraction("-"),
	Division("/"),
	Multiply("*");
	
	private String operator;
	private ALUOperator(String operator) {
		this.operator = operator;
	}
	
	public String getOpt() {
		return operator;
	}
	
}
