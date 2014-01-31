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
public class InstructionRun {
    private Register MAR = new Register();
    private Register MDR = new Register();
    
    ///Fetch IR from memory
    public void FetchIR(int PC, IR IRobject){
        
    }
    public void Decode(IR IRobject){
        Decode dec = new Decode();
        dec.decodeSwitch(IRobject);
    }
    public void RunInstruction(IR IRobject , RF RFtable){
        //TODO the rest of XFI
        //if (IRobject.getXFI()==1)
        //else if(IRobject.getXFI==2)
        //else if(IRobject.getXFI==3)
        MAR.setData(IRobject.getAddress());
        //TODO add mem call, this is just a test with MAR value
        MDR.setData(MAR.getData());
        if(IRobject.getIndirect()==1){
            MAR.setData(MDR.getData());
            //TODO add mem call, MDR is test value only
            MDR.setData(MDR.getData());
        }
        RFtable.tableSwitch(IRobject.getRFI1(), MDR.getData());
    }
    
}
