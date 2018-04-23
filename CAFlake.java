package edu.neu.csye6200.cacrystal;

/*
Keith Fleming
This class is a set of CAFlakeCells that combine to make a flake
 */
public class CAFlake {
    //Array of CAFlaeCells that make up a flake
    private CAFlakeCell[][] flake;
    
    //Flake Constructor
    CAFlake() {
        flake = new CAFlakeCell[65][65];
        //Initialize CAFlake with CAFlakeCell objects
        for(int i = 0; i < flake.length; i++) {
            for(int j = 0; j < flake[0].length; j++) {
                flake[i][j] = new CAFlakeCell();
                }
            }
    }
    
    //Flake constructor with size given
    CAFlake(int size) {
        //Flake is initialized with the given size
        flake = new CAFlakeCell[size][size];
        //Initialize CAFlake with CAFlakeCell objects
        for(int i = 0; i < flake.length; i++) {
            for(int j = 0; j < flake[0].length; j++) {
                flake[i][j] = new CAFlakeCell();
                }
            }
    }
    
    //Method to output CAFlake, outputs "*" when cell is frozen and " " otherwise
    public void OutputFlake() {
        for(int i = 0; i < flake.length; i++) {
            for(int j = 0; j < flake[0].length; j++) {
                if(flake[i][j].getFrozen() == 1) {
                    System.out.print("*");
                }
                else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //Method to get a CAFlakeCell object
    public synchronized CAFlakeCell[][] getFlake() {
        return flake;
    }   
}
