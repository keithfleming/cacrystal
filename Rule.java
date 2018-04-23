package edu.neu.csye6200.cacrystal;

/*
Keith Fleming
Abstract class for rules
 */
public abstract class Rule {
    //Method to initialize a flake
    public CAFlake InitializeFlake(int size){
        //Creates new flake based on size parameter
        CAFlake flake = new CAFlake(size);
        //Gets integers for middle of flake
        int midI = flake.getFlake().length/2;
        int midJ = flake.getFlake()[0].length/2;
        //Gets init String from GUI
        String init = (String)CAFlakeApp.initCBox.getSelectedItem();
        switch(init){
            case "Point":
                //Point selection sets the center FlakeCell frozen
                flake.getFlake()[midI][midJ].setFrozen(1);
                break;
            case "Box":
                //Box selection creates a 3x3 box of frozen FlakeCells at center of Flake
                for(int j = midJ-1;j<midJ+2;j++){
                    for(int i = midI-1;i<midI+2;i++){
                        flake.getFlake()[i][j].setFrozen(1);
                    }
                }
                //Middle FlakeCell is unfrozen
                flake.getFlake()[midI][midJ].setFrozen(0);
                break;
            case "Cross":
                //Cross selection sets the center and adjacent FlakeCells frozen at center of Flake
                flake.getFlake()[midI][midJ].setFrozen(1);
                flake.getFlake()[midI+1][midJ].setFrozen(1);
                flake.getFlake()[midI-1][midJ].setFrozen(1);
                flake.getFlake()[midI][midJ+1].setFrozen(1);
                flake.getFlake()[midI][midJ-1].setFrozen(1);
                break;
            case "'X'":
                //'X' selection sets the center and corner adjacent FlakeCells frozen at center of Flake
                flake.getFlake()[midI][midJ].setFrozen(1);
                flake.getFlake()[midI+1][midJ+1].setFrozen(1);
                flake.getFlake()[midI-1][midJ+1].setFrozen(1);
                flake.getFlake()[midI-1][midJ-1].setFrozen(1);
                flake.getFlake()[midI+1][midJ-1].setFrozen(1);
                break;                
        }
        
        return flake;
    }
    
    //Method for creating a new Flake
    public CAFlake NextFlake(CAFlake flake, int size){
        CAFlake newFlake = new CAFlake(size);//Creates the new flake based on the size parameter
        //Looks though entirety of CAFlake
        for(int j = 0; j < flake.getFlake().length; j++) {
            for(int i = 0; i < flake.getFlake()[j].length; i++) {               
                int current = flake.getFlake()[i][j].getFrozen();//Gets value for existing Flake                
                if(current == 1|Criteria(flake, i, j))
                    newFlake.getFlake()[i][j].setFrozen(1);//If the existing FlakeCell is frozen or criteria are met, the FlakeCell is frozen                                
            }
        }
        return newFlake;//Returns the new flake
    }
    
    //Method for getting the frozen value of a CAFlakeCell
    public int getFlakeValue(CAFlake flake, int i, int j){
        int value;//initializes value
        try {
            //If FlakeCell is frozen, value is set to 1
            value = flake.getFlake()[i][j].getFrozen();
        }
        catch(ArrayIndexOutOfBoundsException ex) {
            //If the FlakeCell is out of the bounds of the array, value is 0
            value = 0;
        }
        return value;
    }  
    
    //abstract class required for creating criteria
    public abstract boolean Criteria(CAFlake flake, int i, int j);
    
}
