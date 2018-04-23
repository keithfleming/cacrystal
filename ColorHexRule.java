package edu.neu.csye6200.cacrystal;

/*
Keith Fleming
Rule to add color to hexagon rule
 */
public class ColorHexRule extends HexRule implements UseColor{
    CAFlakeCell emptyCell = new CAFlakeCell();//Creates empty CAFlakeCell
    
    @Override
    //Method to override NextFlake method
    public CAFlake NextFlake(CAFlake flake,int size) {
        CAFlake newFlake = new CAFlake(size);//Creates new CAFlake
        for(int j = 0; j < flake.getFlake().length; j++) {
            int offset = j%2;//Calculated offset for each row of array
            for(int i = 0; i < flake.getFlake()[j].length; i++) {                 
                //If the existing CAFlakeCell is frozen, the cell is copied
                if(flake.getFlake()[i][j].getFrozen() == 1){
                    newFlake.getFlake()[i][j].setFrozen(1);
                    newFlake.getFlake()[i][j].red = flake.getFlake()[i][j].red;
                    newFlake.getFlake()[i][j].green = flake.getFlake()[i][j].green;
                    newFlake.getFlake()[i][j].blue = flake.getFlake()[i][j].blue;
                }
                //If the sum of teh adjacent cells is 1 or 3 the FlakeCell is set frozen
                else if (Criteria(flake,i,j)){
                    //Put all of the adjacent cells into an array to read from
                    CAFlakeCell flakeCells[] = new CAFlakeCell[6];
                    flakeCells[0] = getFlakeCell(flake, i+1 , j);
                    flakeCells[1] = getFlakeCell(flake, i + offset, j+1);
                    flakeCells[2] = getFlakeCell(flake, i-1+offset, j+1);
                    flakeCells[3] = getFlakeCell(flake, i-1 ,j);                
                    flakeCells[4] = getFlakeCell(flake, i+offset, j-1);                
                    flakeCells[5] = getFlakeCell(flake, i-1+offset, j-1);
                    //Uses array of FlakeCells to get array with suma nd colors
                    int colorArray[] = getColorArray(flakeCells);
                    newFlake.getFlake()[i][j].setFrozen(1);//Sets frozen
                    newFlake.getFlake()[i][j].red = colorArray[0];//Sets red value
                    newFlake.getFlake()[i][j].green = colorArray[1];//Sets green value
                    newFlake.getFlake()[i][j].blue = colorArray[2];//Sets blue value
                }                                                    
            }
        }
        return newFlake;//Returns the new flake
    }
    
    //Method to get a flake cell.  If the flake cell is out of the bounds of the array, a new empty flake cell is returned
    private CAFlakeCell getFlakeCell(CAFlake flake, int i, int j){
        try{
            return flake.getFlake()[i][j];//If flake has Cell Value for i, j it returns the flake
        }
        //If the Flake Cell is out of bounds of the array, a new empty flake cell is returned
        catch(ArrayIndexOutOfBoundsException ex){
            return emptyCell;
        }
    }
    
    //Method to get the color array
    private int[] getColorArray(CAFlakeCell flakeCells[]){
        int sum = 0, redSum = 0, greenSum = 0, blueSum = 0;//Sets all sums to zero
        int colorArray[] = {0,0,0};
        //Looks through all CAFlakeCells that were passed
        for(int i = 0; i<flakeCells.length; i++ ){
            //If the FlakeCell is frozen, the sums are updated
            if(flakeCells[i].getFrozen()==1){
                sum += 1;
                redSum += flakeCells[i].red;
                blueSum += flakeCells[i].blue;
                greenSum += flakeCells[i].green;
            }            
        }
        //If the sum is greater than 0, the color sums are updated
        if(sum>0){
            colorArray[0] = VerifyColor(6*sum + redSum/sum);//Equation for red value
            colorArray[1] = VerifyColor(9+greenSum/sum);//Equation for green value
            colorArray[2] = VerifyColor(11*sum + blueSum/sum); //Equation for blue value
        }        
        return colorArray;//Returns the color array
    }
    
    //Method to verify a color value
    private int VerifyColor(int color){
        //If color value is outside the bounds of a color, it is set to 0
        if (color>255)
            color = 0;
        return color;//Returns color
    }
    
    @Override
    //Method to view rule as String
    public String toString(){
        return "Color Hex Rule";
    }
}
