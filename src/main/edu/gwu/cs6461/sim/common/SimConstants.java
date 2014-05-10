package edu.gwu.cs6461.sim.common;


/**
 * Constants used in the simulator
 * @author marcoyeung
 * */
public class SimConstants {
	public static final int WORD_SIZE = 20;
	public static final int WORD_MIN_VALUE = -524288;
	public static final int WORD_MAX_VALUE = 524287;
	public static final long TWOWORD_MIN_VALUE= -549755813888L;
	public static final long TWOWORD_MAX_VALUE = 549755813887L;
	public static final int WORD_UNSIGN_VALUE = 1048576;
	public static final int BIN_MASK_20 = 0b11111111111111111111;
	
	public static final String FILE_INSTRUCTION_HEAD = "INSTR";
	public static final String FILE_DATA_HEAD = "DATA";
	public static final String FILE_COMMENT = "#";
	public static final String MSG_TO_GUI_DELIMITER = "|";
	
	public static final int MEMORY_ADDRESS_LIMIT = 8192;
	public static final int MEMORY_ADDRESS_SPACE = 13;
	
	public static final int TRAP_HDLER_ADDRESS =0;
	public static final int FAULT_HDLER_ADDRESS =1;
	public static final int TRAP_PC_STORE = 2;
	public static final int TRAP_MSR_STORE = 3;
	public static final int FAULT_PC_STORE = 4;
	public static final int FAULT_MSR_STORE = 5;
	
	
	public static final String ECONSOLE_ALU_MSG = "ECONSOLE_ALU";
	public static final String ECONSOLE_DECODE_MSG = "ECONSOLE_DECODE";
}