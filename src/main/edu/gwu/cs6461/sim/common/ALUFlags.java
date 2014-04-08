package edu.gwu.cs6461.sim.common;

/**
 * ALU status flag constant class
 * */
public enum ALUFlags {
	NotReady, /**ALU is in incorrect state*/
	Overflow,
	Divisionbyzero,
	Normal;
}
