package edu.gwu.cs6461.sim.exception;


/** 
 * Memory exception class for throwing 
 * exception if invalid memory address is encountered
 * */
public class FloatingPointException extends Exception {

	
	private String message;
	public FloatingPointException(String message) {
		
	this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
