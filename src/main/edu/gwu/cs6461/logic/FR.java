package edu.gwu.cs6461.logic;

import edu.gwu.cs6461.sim.bridge.Observer;
import edu.gwu.cs6461.sim.common.HardwarePart;

public class FR {
	/**create float register*/
	private FloatRegister FR0 = new FloatRegister(HardwarePart.FR0.getBit(),HardwarePart.FR0.getName());
	/**create float register*/
	private FloatRegister FR1 = new FloatRegister(HardwarePart.FR1.getBit(),HardwarePart.FR1.getName());
	public FR(){
	}
	/**updates values to GUI */
    public void setRegisterObserver(Observer obs){
    	FR0.register(obs);
    	FR1.register(obs);    	
    }
    public void clearObserver(){
    	FR0.clear();
    	FR1.clear();    	
    }
    
    
    /***get functions
     */
     public float getFR0(){
         return FR0.getData();
     }
     public float getFR1(){
         return FR1.getData();
     }
    
     /****set functions
     */
     public void setFR0(float FR){
         FR0.setData(FR);
     }
     public void setFR1(float FR){
    	 System.out.println("************** "+FR);
         FR1.setData(FR);
     }
     
     /** takes the index (FRI) of the float register and puts the passed value(Data) into it */
     public void setSwitch(int FRI,float Data){
         if (FRI==0)
             setFR0(Data);
         else if (FRI==1)
             setFR1(Data);        
     }
     /**takes the index (FRI) and returns the value of that float register */
     public float getSwitch(int FRI){
         if (FRI==0)
            return getFR0();
         else if (FRI==1)
            return getFR1();         
        return 0.0f;
     }
}
