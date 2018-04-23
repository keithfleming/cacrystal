package edu.neu.csye6200.cacrystal;

import java.awt.Color;

/*
Keith Fleming
This class is the individual flakes that will make up a snow flake
Each cell will have a value of 1 if the cell is frozen, otherwise it will be 0
 */
public class CAFlakeCell {
    //Value to set if cell is frozen
    private int frozen;
    //Values used to determine colors
    public int red, green, blue;
    
    //FlakeCell Constructor
    CAFlakeCell(){
        //Each cell is initialized as 0, or turned off
        frozen = 0;
        //The color values are initialized as randomintegers between 0 and 255
        red = (int)(255*Math.random());
        blue = (int)(255*Math.random());
        green = (int)(255*Math.random());
    }

   
    //Method to get the value of frozen
    public synchronized int getFrozen() {
        return frozen;
    }

    //Method to set the value of frozen
    public synchronized void setFrozen(int frozen) {
        this.frozen = frozen;
    }
}
