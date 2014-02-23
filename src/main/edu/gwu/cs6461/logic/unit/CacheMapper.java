package edu.gwu.cs6461.logic.unit;

import edu.gwu.cs6461.logic.unit.MainMemory.Entry;
import edu.gwu.cs6461.sim.exception.MemoryException;

public interface CacheMapper {

	public void writeCache(int address, Entry entry);
	public boolean isDataInCache(int address);
	public Entry getDataFromCache(int address) throws MemoryException;
	public void printCacheStatus();
}
