package edu.gwu.cs6461.sim.common;

public class SimConstants {
	public static final int WORD_SIZE = 20;
	public static final int WORD_MIN_VALUE = -524288;
	public static final int WORD_MAX_VALUE = 524287;
	public static final int WORD_UNSIGN_VALUE = 1048576;
	public static final int BIN_MASK_20 = 0b11111111111111111111;
	
	public static final String FILE_INSTRUCTION_HEAD = "INSTR";
	public static final String FILE_DATA_HEAD = "DATA";
	public static final String FILE_COMMENT = "#";
	
	
	public static final int MEMORY_ADDRESS_LIMIT = 8192;
	public static final int MEMORY_ADDRESS_SPACE = 13;
}
