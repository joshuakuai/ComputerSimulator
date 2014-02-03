package edu.gwu.cs6461.sim.mem;

public interface Memory <E> {

	public void set(int add, E val);
		
	public E get(int add);
	
}
