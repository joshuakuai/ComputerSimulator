package edu.gwu.cs6461.logic.unit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.unit.MainMemory.Entry;
import edu.gwu.cs6461.logic.unit.SetAssociativeMapper.MemoryAddress;
import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;


/**
 * Simulate the Cache hardware for this simulator.
 * <BR><BR>
 * This class is designed for Set Associative mapping
 * <BR>
 * Cache data are stored in a tree map data structure so that data are in order.
 * <BR>
 * Data lines in cache are stored in LinkedHashMap so that replacement policy could be easily achieved. 
 * @author marcoyeung
 * 
 * @Revised   Feb 20, 2014 - 11:24:39 AM
 * 
 */
public class Cache {
	/**logger to log message to log file*/
	private static final Logger logger = Logger.getLogger(Cache.class);

	/**the singleton object for this cache class*/
	private static final Cache instance = new Cache();
	
	/**return the cache singleton object*/
	public static Cache getInstance(){
		return instance;
	}
	/**Number of line could be put into a set*/
	private int lineSize;
	
	/**Data structure to store the cache entries; Linked list is used for line in set so that replacement policy could be achieved */
	private Map<Integer, LinkedHashMap<Integer,List<Entry>>>cacheData;
	
	/**Private constructor. Only one instance of the class is created.*/
	private Cache() {
		//simulator property loader 
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		//line size is configurable
		lineSize = prop.getIntProperty("sim.mem.cache.linesize", 4);
		init();
	}
	
	/**
	 * Re-initialize the cache. All data will be purged.
	 */
	public void init(){
		cacheData = new TreeMap<Integer, LinkedHashMap<Integer,List<Entry>>>();
	}
	/**
	 * Return the cache entry with the specified Main memory address.
	 * 
	 * @param add  Main Memory address in Set associative form
	 * @return     Cache Entry:MainMemory.Entry
	 */ 
	public Entry getData(MemoryAddress add) {
		return getData(add, true);
	}
	/**
	 * Return the cache entry with the specified Main memory address.
	 * <BR>
	 * This overloaded version also update Least Recent used Cache entry for replacement policy
	 *  
	 * @param add  Main Memory address in Set associative form
	 * @param setLRU    true to update cache entry for Least recent used; otherwise, no update is done
	 * @return     Cache Entry:MainMemory.Entry  
	 */
	public Entry getData(MemoryAddress add, boolean setLRU) {
		int set = add.getSetField();
		int tag = add.getTagField();
		int word = add.getWordField();

		Map<Integer, List<Entry>> setEnt = cacheData.get(set);
		if (setEnt == null) {
			return null;
		}
		List<Entry>line = setEnt.get(tag);
		if (line == null) { //Cache entry not found with the specified address
			return null;
		} else if (setLRU) {
			//set LRU, put the referenced cache entry in the front 
			List<Entry>lru = setEnt.remove(tag);
			setEnt.put(tag, lru);
			logger.debug("set tag:"+tag +" recently used in set:"+ set);
		}
		Entry data = line.get(word);
		return data;
	}
	
	/**
	 * Put the Main memory block/line into specific set in cache.
	 * 
	 * @param add  Main Memory address in Set associative form
	 * @param line  Main Memory in line/block
	 */
	public void pushDataToCache(MemoryAddress add, List<Entry> line) {
		int set = add.getSetField();
		int tag = add.getTagField();
		
		//determine the set for the line
		LinkedHashMap<Integer, List<Entry>> setEnt = cacheData.get(set);
		if (setEnt == null) { //this set doesn't exist yet
			setEnt = new LinkedHashMap<>();
			cacheData.put(set, setEnt);
		}
		

		int currLineSize = setEnt.size();
		if (currLineSize < lineSize) {  //put the line in the corresponding set
			setEnt.put(tag, line);
		} else {
			//if the set is full, evication is needed. 
			//evict the LRU entry
			//eviction is needed
			Integer[] tags = setEnt.keySet().toArray(new Integer[0]);
			
			int lruTag = tags[0];
			List<Entry>ln = setEnt.get(lruTag);
			if (ln != null) {
				logger.warn("Eviction: LRU line in cache will be overwritten:"+ln);
				setEnt.remove(lruTag);
			}
			//put the line in the corresponding set after eviction
			setEnt.put(tag, line);
		}
		
		//log down the message into log file
		String ids ="";
		for (Entry entry : line) {
			ids = ids + entry.getAddress() + ",";
		}
		logger.debug("line added to cache set:"+set +",tag:"+tag+",block:" + ids);
	}
	
	/**
	 *output the cache content for information purpose 	 
	 */
	public void printCacheStatus(){
		int size = cacheData.size();
		logger.debug("Total Set:" + size);
		for (java.util.Map.Entry<Integer, LinkedHashMap<Integer, List<Entry>>> entry : cacheData.entrySet()) {
			int ts = entry.getKey();
			LinkedHashMap<Integer, List<Entry>> lin = entry.getValue();
			int linesize  = lin.size();
			int tagKey = -1;
			logger.debug("set#:" + ts + ",linesize: " + linesize);
		}
	}
/*	protected static class CacheEntry {
		private int tagField;
		private List<Entry> line; 
		
		
	}*/
}
