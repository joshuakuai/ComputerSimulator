package edu.gwu.cs6461.sim.common;


public enum HardwarePart {
	R0("R0",20),
	R1("R1",20),
	R2("R2",20),
	R3("R3",20),
	X1("X1",13),
	X2("X2",13),
	X3("X3",13),
	MAR("MAR",13),
	MBR("MDR",20),
	MSR("MSR",20,false),
	MFR("MFR",4,false),
	CC("CC",4,false),
	IR("IR",20),
	PC("PC",13,false),
	MEMORY("MEMORY",20),
	NOTEXIST("NOTEXIST",-1,false);
	
	private final String name;
	private final int numOfBit;
	private final boolean editable;
	private HardwarePart(String name, int numOfbit, boolean edit){
		this.name= name;
		this.numOfBit = numOfbit;
		this.editable = edit;
	}
	private HardwarePart(String name, int numOfbit){
		this(name, numOfbit, true);
	}
	
	public String getVal(){
		return name;
	}
	
	public int getBit(){
		return numOfBit;
	}
	public boolean isEditable(){
		return editable;
	}
	public static HardwarePart fromName(String name){
		if (name == null || "".equals(name.trim())) {
			return NOTEXIST;
		}
		
        for (HardwarePart value : HardwarePart.values()) {
            if (value.getVal().equals(name)) {
                return value;
            }
        }
        return NOTEXIST;
	}
}
