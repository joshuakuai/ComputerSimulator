/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;
import java.io.Console;

/**
 *
 * @author Ahmed
 */

public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Register reg1 =  new Register();
       // reg1.setData("ldr,0,3,54,I");
       // reg1.setSize(20);
        
        //System.out.println(reg1.getData());
        //System.out.println(reg1.getSize());
       //IR IRobject = new IR("00000111001000110110");
       IR IRobject = new IR("00010011011000110110");
       RF RFtable  = new RF();
       XF XFtable  = new XF();
       Memory Mem  = new Memory();
       ALU    ALU = new ALU();
       Mem.initMem();
       Mem.setMem(54, 100);
       Mem.setMem(100, 1023);
       Mem.setMem(204,512);
       Mem.setMem(512, 513);
       RFtable.setSwitch(3, 10);
       Control CPURun = new Control();
       CPURun.Decode(IRobject);
       XFtable.setX1(150);
       CPURun.RunInstruction(IRobject, RFtable, XFtable, Mem, ALU);
       System.out.println("R3="+RFtable.getSwitch(IRobject.getRFI1()));  
    }
}
