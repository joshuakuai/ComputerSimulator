package edu.gwu.cs6461.sim.exception;

public class MemoryException extends Exception {

	
	private String message;
	public MemoryException(String message) {
		
	this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
