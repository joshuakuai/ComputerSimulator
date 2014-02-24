package edu.gwu.cs6461.sim.exception;

public class IOCmdException extends Exception {

	
	private String message;
	public IOCmdException(String message) {
		
	this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
