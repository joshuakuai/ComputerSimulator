/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

/**
 *
 * @author Ahmed
 */
public class IR extends Register{
    private String IRstring="";
    private int OpCode=0;
    private int RFI1=0;
    private int RFI2=0;
    private int XFI=0;
    private int Address=0;
    private int Immed=0;
    private int Indirect=0;
    private int Trace=0;
    IR(String IR){
        IRstring = IR;   
    }  
    /*
    ******Get functions********************
    */
    public String getIRstring(){
        return IRstring;
    }
    public int getOpCode(){
        return OpCode;
    }
    public int getRFI1(){
        return RFI1;
    }
    public int getRFI2(){
        return RFI2;
    }
    public int getXFI(){
        return XFI;
    }
    public int getAddress(){
        return Address;
    }
    public int getImmed(){
        return Immed;
    }
    public int getIndirect(){
        return Indirect;
    }
    public int getTrace(){
        return Trace;
    }
    /*
    ******Set functions**********************
    */
    public void setOpCode(int OpCodeNew){
        OpCode= OpCodeNew;
    }
    public void setRFI1(int RFI){
        RFI1= RFI;
    }
    public void setRFI2(int RFI){
        RFI2=RFI;
    }
    public void setXFI(int XFINew){
        XFI=XFINew;
    }
    public void setAddress(int AddressNew){
        Address = AddressNew;
    }
    public void setImmed(int ImmedNew){
        Immed= ImmedNew;
    }
    public void setIndirect(int IndirectNew){
        Indirect =  IndirectNew;
    }
    public void setTrace(int TraceNew){
        Trace = TraceNew;
    }
    
}
