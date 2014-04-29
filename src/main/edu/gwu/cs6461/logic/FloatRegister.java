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
	private String data = "";

	
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
	public String getData() {
		String dData = data;
		return dData;
	}
	
	/**sets the value of the float register both internally and on the GUI*/
	public void setData(int newData) {
		data = Integer.toBinaryString(newData);
		
		if(this.name.equals("SI") || this.name.equals("SS")){
			return;
		}
		HardwareData hardwareData = new HardwareData();
		String unTwo = getData(); 
		
		if(unTwo.length()>20)
			unTwo= unTwo.substring(unTwo.length()-20, unTwo.length());		
		else{
			int len = unTwo.length();
			while(len<20){
				unTwo = "0"+unTwo;
				len++;
			}
		}
		
		System.out.println("X*X*X*X*X*X**X*X*X*X*X*X*X* " +unTwo);
		FloatPoint fp= new FloatPoint(0.0f);
		try {
			//fp = new FloatPoint(unTwo.substring(0,1),unTwo.substring(1,8),unTwo.substring(8,20));
			fp = new FloatPoint("0","0011111","000000000011");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float value = fp.floatValue();
		hardwareData.put(this.name, Float.toString(value));
	
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
