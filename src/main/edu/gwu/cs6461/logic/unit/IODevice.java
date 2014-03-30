package edu.gwu.cs6461.logic.unit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.IORegister;
import edu.gwu.cs6461.sim.bridge.HardwareData;
import edu.gwu.cs6461.sim.bridge.Observable;
import edu.gwu.cs6461.sim.common.DeviceType;
import edu.gwu.cs6461.sim.common.HardwarePart;
import edu.gwu.cs6461.sim.common.SimConstants;

public class IODevice extends Observable{
	private final static Logger logger = Logger.getLogger(IODevice.class);

	/** either IN, OUT or CHK IO instructions */
	private DeviceType type;
	
	/**IO Device internal register for data holding*/
	private IORegister register;// = new Register(20, "Keyboard", true);
	
	private ReentrantLock lock = new ReentrantLock();
	private Condition noData = lock.newCondition();
	private Condition hasData = lock.newCondition();
	
	public IODevice(DeviceType type) {
		this.type = type;
		this.register = new IORegister(SimConstants.WORD_SIZE, type.name());
		

		//TODO REMOVE IT
//		tester();
	}
	
	@Deprecated
	private void tester() {
		if (type == DeviceType.Keyboard) {

			new Thread(new Runnable() {
				String result ="";

				@Override
				public void run() {
					logger.debug("I'll get data from keyboard.");

					int d;

					/**get data value from keyboard
					 * delimited by '\n'
					 * */
					for (int i = 0; i < 3; i++) {
						
						while (true) {
							d = getData(true);

							if ((char)d =='\n') {
								break;
							}
							result = result + (char)d;
						}
						logger.debug(" data got from keyboard: "+ result);
						result="";
					} //
				}
			},"INPUT").start();

		}
		if (type == DeviceType.ConsolePrinter) {
			/**************************************************/
			String result ="this is from cpu";
			final String toOut = result;
			new Thread(new Runnable() {
				@Override
				public void run() {
					logger.debug("I'll send back to gui");
					int len = toOut.length();
					for (int i = 0; i < len; i++) {

						if (i == 0) {
							putData(toOut.charAt(i), true);
						} else {
							putData(toOut.charAt(i), false);
						}

					}
					putData('\n', false);
				}
			},"OUTPUT")/*.start()*/;
		}
	}
	
	
	public int getData() {
		return getData(false);
	}
	/**
	 * CPU/IODevice tries to get data from register
	 * wait if data is not available
	 * <BR>
	 * After getting the data , the register will be emptied
	 * 
	 * @return   		data that is in register.
	 * @param notify   	true means to notify data producer to get ready to create data; otherwise, simply wait for data
	 */
	public int getData(boolean notify) {
		int d = -1;
		try {
			
			if (notify) {
				notifyProducer();
			}
			lock.lock();
			while (!register.isDataAvailable()) {
				logger.debug("no data available in "+type+" register, waiting...");
				hasData.await();
			}
			d = register.getData();
			register.setDataAvailable(false);
			noData.signalAll();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return d;
	}
	
	/**
	 * Let GUI knows that new data from IODevices is needed
	 * <BR>
	 * the data producer could GUI (keyboard) if IODevice is Keyboard; otherwise,
	 * the producer is CPU who wants to send data GUI
	 */
	private void notifyProducer() {
		HardwareData hardwareData = new HardwareData();
		hardwareData.put(HardwarePart.INPUT.getName(),
				"DATAREQUEST");

		this.notifyObservers(hardwareData);

	}
	
	/**
	 * notify consumer after putting data,
	 * <BR> 
	 * consumer could be CPU if IODevice is Keyboard, 
	 * or it could be GUI is IODevice is Console Printer
	 */
	private void notifyConsumer() {
		HardwareData hardwareData = new HardwareData();
		hardwareData.put(HardwarePart.OUTPUT.getName(),
				"DATAAVAILABLE");

		this.notifyObservers(hardwareData);
		
	}
	public void putData(int data) {
		putData(data,false);
	}
	/**
	 * CPU/IO devices put data into IO devices' register
	 * Wait if the there is data has not yet been consumed
	 * <br>
	 * After putting data, the register will be full and available for consuming
	 * 
	 * @param data  data to be put into IO devices register.
	 * @param notify   true means to notify data consumer to get ready to pick data; otherwise, put the data and quit
	 */
	public void putData(int data, boolean notify) {
		try {
			if (notify) {
				notifyConsumer();
			}
			lock.lock();
			while (register.isDataAvailable()) {
				logger.debug("data is already in register "+type+". waiting to be consumed...");
				noData.await();
			}
			register.setData(data);
			register.setDataAvailable(true);
			hasData.signalAll();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	} 
	
}
