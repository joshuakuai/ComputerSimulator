package edu.gwu.cs6461.logic;
/**
 * This is a temp 40bit register to hold the result of the 
 * multiplication
 * @Revised   Jan 20, 2014 - 11:24:39 AM
 *
 */
public class Multiply extends Register{
	private long result=0;
	public Multiply(int size, String name) {
		super(size, name);
		// TODO Auto-generated constructor stub
	}		
	public long getResult(){
		return result;
	}
	public void setResult(long Result){
		result=Result;
	}
}

