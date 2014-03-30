package edu.gwu.cs6461.sim.common;

public enum DeviceType {
	Keyboard("Keyboard",0),
	ConsolePrinter("ConsolePrinter",1),
	CarReader("CardReader",2),
	Unknown("unknown",-1);
	
	private final String name;
	private final int id;
	private DeviceType(String name, int id){
		this.name= name;
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public static DeviceType fromId(int id) {
		
        for (DeviceType value : DeviceType.values()) {
            if (value.id == id) {
                return value;
            }
        }
        return Unknown;
	}	
	
}
