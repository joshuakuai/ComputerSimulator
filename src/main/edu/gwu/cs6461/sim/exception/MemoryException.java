package edu.gwu.cs6461.sim.exception;


/** 
 * Memory exception class for throwing 
 * exception if invalid memory address is encountered
 * */
public class MemoryException extends Exception {

	
	private String message;
	public MemoryException(String message) {
		
	this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
