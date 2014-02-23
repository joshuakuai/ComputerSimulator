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

public class Cache {
	private static final Logger logger = Logger.getLogger(Cache.class);

	private static final Cache instance = new Cache();
	public static Cache getInstance(){
		return instance;
	}
	
	private int lineSize;
	
	private Map<Integer, LinkedHashMap<Integer,List<Entry>>>cacheData;
	private Cache() {
		PropertiesParser prop = PropertiesLoader.getPropertyInstance();
		lineSize = prop.getIntProperty("sim.mem.cache.linesize", 4);
		cacheData = new TreeMap<Integer, LinkedHashMap<Integer,List<Entry>>>();
	}

	public Entry getData(MemoryAddress add) {
		return getData(add, true);
	}
	public Entry getData(MemoryAddress add, boolean setLRU) {
		int set = add.getSetField();
		int tag = add.getTagField();
		int word = add.getWordField();

		Map<Integer, List<Entry>> setEnt = cacheData.get(set);
		if (setEnt == null) {
			return null;
		}
		List<Entry>line = setEnt.get(tag);
		if (line == null) {
			return null;
		} else if (setLRU) {
			//set LRU
			List<Entry>lru = setEnt.remove(tag);
			setEnt.put(tag, lru);
			logger.debug("set tag:"+tag +" recently used in set:"+ set);
		}
		Entry data = line.get(word);
		return data;
	}
	public void pushDataToCache(MemoryAddress add, List<Entry> line) {
		int set = add.getSetField();
		int tag = add.getTagField();
		
		LinkedHashMap<Integer, List<Entry>> setEnt = cacheData.get(set);
		if (setEnt == null) { //this set doesn't exist yet
			setEnt = new LinkedHashMap<>();
			cacheData.put(set, setEnt);
		}
		
		int currLineSize = setEnt.size();
		if (currLineSize < lineSize) {
			setEnt.put(tag, line);
		} else {
			//eviction is needed
			Integer[] tags = setEnt.keySet().toArray(new Integer[0]);
			
			int lruTag = tags[0];
			List<Entry>ln = setEnt.get(lruTag);
			if (ln != null) {
				logger.warn("Eviction: LRU line in cache will be overwritten:"+ln);
				setEnt.remove(lruTag);
			}
			setEnt.put(tag, line);
		}
		
		String ids ="";
		for (Entry entry : line) {
			ids = ids + entry.getAddress() + ",";
		}
		logger.debug("line added to cache set:"+set +",tag:"+tag+",block:" + ids);
	}
	
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
