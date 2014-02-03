/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;
import java.util.*;
/**
 *
 * @author Ahmed
 */
public class Memory {
    private Vector<Integer> Mem= new Vector<Integer>();
    private int size=8192;
    
    public void initMem(){
        for(int i=0; i<8192;i++){
            Mem.addElement(0);
        }
        System.out.println("MEM="+Mem.size());
    }
    
    public int getMem(int Address){
        return Mem.elementAt(Address);
    }
    public void setMem(int Address, int Data){
        //Mem.add(Address, Data);
       // Mem.addElement(Data);
      // Mem.insertElementAt(Data, Address);
        Mem.set(Address, Data);
    }
}
