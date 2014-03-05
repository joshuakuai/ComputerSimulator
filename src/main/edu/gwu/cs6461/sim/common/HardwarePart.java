package edu.gwu.cs6461.sim.common;


/**
 * A constants class represents all the hardware parts that're supported by this 
 * simulator
 * 
 * @author marcoyeung
 *
 */
public enum HardwarePart {
	R0("R0",20),
	R1("R1",20),
	R2("R2",20),
	R3("R3",20),
	X1("X1",13),
	X2("X2",13),
	X3("X3",13),
	MAR("MAR",13),
	MDR("MDR",20),
	MSR("MSR",20,false),
	MFR("MFR",4,false),
	CC("CC",4,false),
	IR("IR",20,false),
	PC("PC",13,false),
	MEMORY("MEMORY",20),
	RES("RES",20,false),
	NOTEXIST("NOTEXIST",-1,false);
	
	/**
	 * hardware part name, this is used to show on the GUI
	 */
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
	
	/**
	 * returns the hardware part name
	 * @return    hardware part name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * return the number of bits is used for this hardware part in memory or program
	 * 
	 * @return  number of bits   
	 */
	public int getBit(){
		return numOfBit;
	}
	
	/**
	 * Whether or not to allow this hardware component to be edited through
	 * switches in GUI 
	 * 
	 * @return   True if this part can be displayed in Front-end GUI
	 */
	public boolean isEditable(){
		return editable;
	}
	
	/**
	 * return hardwarePart enum object by lookup the hardware part name.
	 * 
	 */
	public static HardwarePart fromName(String name){
		if (name == null || "".equals(name.trim())) {
			return NOTEXIST;
		}
		
        for (HardwarePart value : HardwarePart.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return NOTEXIST;
	}
}
