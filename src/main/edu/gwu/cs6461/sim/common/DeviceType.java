package edu.gwu.cs6461.sim.common;

public enum DeviceType {
	Keyboard("Keyboard",0),
	ConsolePrinter("ConsolePrinter",1),
	CarReader("CardReader",2);
	
	private final String name;
	private final int id;
	private DeviceType(String name, int id){
		this.name= name;
		this.id = id;
	}
	
}
