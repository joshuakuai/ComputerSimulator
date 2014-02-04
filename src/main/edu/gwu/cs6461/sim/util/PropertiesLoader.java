package edu.gwu.cs6461.sim.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * 
 * @author Marco Yeung
 *
 */
public class PropertiesLoader {
	private final static Logger logger = Logger.getLogger(PropertiesLoader.class);

	public static final String FLOWCONTROL_PROPERTY_FILE = "sim.property";


	
	private static PropertiesParser  INSTANCE;
	static{
		INSTANCE = loadProperties();
	}
	
	public static PropertiesParser getPropertyInstance(){
		return INSTANCE;
				
	}
	
	private static PropertiesParser loadProperties() {

		String requestedFile = System.getProperty(FLOWCONTROL_PROPERTY_FILE);
		String propFileName = requestedFile != null ? requestedFile
				: "sim.property";

		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(propFileName));
			props.load(in);
		} catch (Exception e) {
			logger.error("unable to load flow control properties.", e);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignore) { /* ignore */
				}
			}
		}

		return new PropertiesParser(props);
	}
}
