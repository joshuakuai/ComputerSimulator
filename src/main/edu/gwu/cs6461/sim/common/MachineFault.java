package edu.gwu.cs6461.sim.common;


/**
 * Machine fault constant class
 * 
 * */
public enum MachineFault {
    IllegalMemoryAddress (0,"Illegal Memory Address"),
	IllegalTrapCode(1,"Illegal Trap Code"),
	IllegalOpCode(2,"Illegal OpCode"),
	Unknown(-1,"Unknown");
    
    private final int id;
    private final String fault;
	private MachineFault(int id, String fault) {
		this.id = id;
		this.fault = fault;
	}
    public int getId() {
    	return id;
    }
    
    public static MachineFault valueOf(int id) {
    	
        for (MachineFault value : MachineFault.values()) {
            if (value.id == id) {
                return value;
            }
        }
        return Unknown;
    }
}
