package edu.gwu.cs6461.logic.unit;

import edu.gwu.cs6461.logic.unit.MainMemory.Entry;
import edu.gwu.cs6461.sim.exception.MemoryException;


/**
 * 
 * Base type for Cache Mapper. <BR>
 * Depends on the implementation of the simualtor, the subtype could be DirectMapper, 
 * Full associative Mapper or Set Assocciative Mapper
 * 
 * @author marcoyeung
 *
 */
public interface CacheMapper {

	/**
	 * Write data into cache for locality
	 * @param address    Main Memory address
	 * @param entry      MainMemory.Entry. the data structure in this Cache 
	 */
	public void writeCache(int address, Entry entry);
	
	/**
	 * Return true if the data with specified address is in Cache.<BR>
	 * The checking won't cause replacement flag get impacted.
	 * 
	 * @param address    Main Memory address
	 * @return       True if data with specified address is in cache; false otherwise.
	 */
	public boolean isDataInCache(int address);
	/**
	 * Return the data entry, in word, from the specified address.
	 * 
	 * @param address    Main Memory address
	 * @return        Main Memory object ie:MainMemory.Entry
	 * @throws MemoryException     Exception if address is not found in cache or failed to pull data from Main Memory into cache.
	 */
	public Entry getDataFromCache(int address) throws MemoryException;
	
	/**
	 * Output Cache content for information purpose
	 * */
	public void printCacheStatus();
}
