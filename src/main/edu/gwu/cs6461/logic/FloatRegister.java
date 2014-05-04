package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.util.FloatPoint;


public class FloatRegister extends Observable{
	/**logger to log message to file*/
	private final static Logger logger = Logger.getLogger(Register.class);
	/**holding the data in this register*/
	private float data = 0.0f;

	
	/**size of the register */
	private int size = 0;
	/**name of the register */
	private String name;

	/**Constructor */
	public FloatRegister(int size, String name) {
		this.size = size;
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**return value */
	public float getData() {
		return data;
	}
	
	/**sets the value of the float register both internally and on the GUI*/
	public void setData(float newData) {
		data = newData;
		
		if(this.name.equals("SI") || this.name.equals("SS")){
			return;
		}
		HardwareData hardwareData = new HardwareData();
		hardwareData.put(this.name, Float.toString(data));
		this.notifyObservers(hardwareData);
		CPUController.shareInstance().checkSingleStepModel();
	}

	public int getSize() {
		return size;
	}
	public void setSize(int newSize) {
		size = newSize;
	}
}
