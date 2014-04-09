package edu.gwu.cs6461.logic;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.logic.unit.IODevice;
import edu.gwu.cs6461.logic.unit.MMU;
import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.DeviceType;
import edu.gwu.cs6461.sim.exception.IOCmdException;
import edu.gwu.cs6461.sim.ui.MainSimFrame;

/**
 * This class holds most of the objects need to run the CPU. 
 * To run it first checks if the user clicked single mode or run which would set its debug
 * Register (SS), which is used as a condition to control the CPU thread by suspending it
 * after each microcode step is finished. Example after decode, effective address
 * ALU calculation etc.
 * @Revised   Jan 20, 2014 - 11:24:39 AM
 */
public class CPUController extends Thread {
	/**Initial Registers */
	public RegisterContainer registerContainer = new RegisterContainer();

	/** Memory unit */
	private MMU mmu = MMU.instance();
	
	/** Logic control */
	public Control cpuControl = new Control();
	
	/** Singleton instance */
	private static CPUController instance = new CPUController();
	
	/** Thread suspend flag */
	private boolean suspendflag = false;
	
	/**logger to log message into log file */
	private final static Logger logger = Logger.getLogger(CPUController.class);
	
	/**logger to log message into front-end GUI */
	private final static Logger simConsole = Logger.getLogger("simulator.console");

	/** Keep a weak reference of mainSimFrame */
	private MainSimFrame mainFrame = null;

	/**input device - keyboard */
	private IODevice inputDevice =null;
	/**output device - console printer */
	private IODevice outputDevice =null;	
	
	/** CPU holds a weak reference of the memory but CPU doesn't own the memory */
	private CPUController() {
		cpuControl.setALU(ALU.getInstance());
		cpuControl.setMem(mmu);
		cpuControl.setRegisters(registerContainer);

		inputDevice = new IODevice(DeviceType.Keyboard);
		outputDevice = new IODevice(DeviceType.ConsolePrinter);
		cpuControl.setINDevices(inputDevice);
		cpuControl.setOUTDevices(outputDevice);
	}

	/** This recreates the cpu thread after an instruction is finished
	and a new one is started */
	public void recreateCPUController(boolean isReserveData) {
		CPUController tmpController = new CPUController();
		
		if(isReserveData){
			tmpController.registerContainer = instance.registerContainer;
			tmpController.cpuControl = instance.cpuControl;
			tmpController.inputDevice = instance.inputDevice;
			tmpController.outputDevice = instance.outputDevice;
		}
		
		instance = tmpController;
	}

	/**  Singleton method */
	public static CPUController shareInstance() {
		return instance;
	}

	/**  Suspend is deprecated but we'll still use it */
	public void checkSingleStepModel() {
		if (registerContainer.SS.getData() == 1 && this.isAlive()) {
			this.Suspend();
		}
	}

	public void setMemData(int address, String data){
		mmu.setData(address, data);
	}
	
	public void setMainFrame(MainSimFrame mf) {
		mainFrame = mf;
	}
	
	/**Clear all the observers from hardwares */
	public void clearObserver(){
		mmu.clearObserver();
		registerContainer.clearAllRegistersObserver();
		inputDevice.clear();
		outputDevice.clear();
	}

	/**Register all the observers to hardwares */
	public void setRegisterObserver(Observer obs) {
		mmu.registerObserver(obs);
		registerContainer.registerObserver(obs);
		inputDevice.register(obs);
		outputDevice.register(obs);
		
	}

	/**
	 * suspend the main execution thread; this is used to simulate the single step or single instruction mode
	 */
	public void Suspend() {
		if(suspendflag){
			return;
		}else{
			suspendflag = true;
		}
		
		String inforString = String.format("suspended PC: %d",registerContainer.PC.getData());
		simConsole.info(inforString);
		super.suspend();
	}

	/**
	 * Resume the main execution thread; this is used to simulate the single step or single instruction mode
	 */
	public void Resume() {
		suspendflag = false;
		simConsole.info("resumed.");
		super.resume();
	}

	/**
	 * Returns the main thread suspension status
	 * */
	public boolean isSuspended() {
		return suspendflag;
	}
	
	/**Main execution execution point
	 * This is the place where the simulation begins.
	 * */
	public void run() {
		logger.debug("CPU thread begins.");

		while(true){
			//Fetch the IR
			cpuControl.FetchIR();
						
			cpuControl.Decode();

			cpuControl.RunInstruction();
			
			//If is the terminate operate, we will stop the running process
			if(registerContainer.IRobject.getOpCode() == 55){
				break;
			}
			
			if (registerContainer.SI.getData() == 1 && this.isAlive()) {
				registerContainer.SI.setData(0);
				this.Suspend();
			}
		}
		
		// Finish all instructions, show message to user to let it know
		mainFrame.showProgrameFinishDialog();
	
		logger.debug("CPU thread ends.");
	}
	
	/**load up program file into memory and publish data to GUI*/
	public void loadFromFile(String fileName) throws IOCmdException  {
		mmu.loadFromFile(fileName);
	}
	/**load up ROM file into memory and publish data to GUI*/
	public void loadROM() {
		mmu.loadROM();
	}

	/**perform IN io instruction */
	public void setInputDeviceData(DeviceType type, int data) {
		inputDevice.putData(data);
	}

	/**perform OUT io instruction */
	public int getOuputDeviceData() {
		return outputDevice.getData(); //output device
	}
}
