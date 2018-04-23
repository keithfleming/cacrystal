package edu.neu.csye6200.cacrystal;

/*
 Keith Fleming
 Rule to add color to the BasicRule
 */
public class ColorRule extends BasicRule implements UseColor{
    CAFlakeCell emptyCell = new CAFlakeCell();

    @Override
    //Method looks at cells adjacent cells, if there is only one frozen adjacent cell it sets the cell equal to 1
    public CAFlake NextFlake(CAFlake flake, int size) {
        CAFlake newFlake = new CAFlake(size);
        //Looks at each FlakeCell in the Flake
        for(int i = 0; i < flake.getFlake().length; i++) {
            for(int j = 0; j < flake.getFlake()[i].length; j++) {
                //If the FlakeCell is frozen in the previous flake, it remains frozen with the same color
                if(flake.getFlake()[i][j].getFrozen() == 1){
                    newFlake.getFlake()[i][j].setFrozen(1);
                    newFlake.getFlake()[i][j].red = flake.getFlake()[i][j].red;
                    newFlake.getFlake()[i][j].green = flake.getFlake()[i][j].green;
                    newFlake.getFlake()[i][j].blue = flake.getFlake()[i][j].blue;
                }
                //If the adjacent sum is 1 or 3 the cell is frozen and colors are set to color values
                else if(Criteria(flake,i,j)) {
                    //Creates array to store adjacent FlakeCells for examination
                    CAFlakeCell flakeCells[] = new CAFlakeCell[4];
                    flakeCells[0] = getFlakeCell(flake, i, j+1);
                    flakeCells[1] = getFlakeCell(flake, i, j-1);
                    flakeCells[2] = getFlakeCell(flake, i+1, j);
                    flakeCells[3] = getFlakeCell(flake, i-1, j);
                    //Passes array of FlakeCells to get adjacent sum and color vallues
                    int colorArray[] = getColorArray(flakeCells);
                    newFlake.getFlake()[i][j].setFrozen(1);
                    newFlake.getFlake()[i][j].red = colorArray[0];
                    newFlake.getFlake()[i][j].green = colorArray[1];
                    newFlake.getFlake()[i][j].blue = colorArray[2];
                }
            }
        }
        return newFlake;//Returns the finished flake
    }
    
    //Method for getting the flake cells
    private CAFlakeCell getFlakeCell(CAFlake flake, int i, int j){
        try{
            return flake.getFlake()[i][j];//Returns flake cell if it is within bounds of array
        } catch(ArrayIndexOutOfBoundsException ex){
            return emptyCell;//Returns a new unfrozen cell otherwise
        }
    }
    
    //Method for getting the sum and color values
    private int[] getColorArray(CAFlakeCell flakeCells[]){
        int sum = 0, redSum = 0, greenSum = 0, blueSum = 0;//Initializes sums
        int colorArray[] = {0,0,0};//Creates color array with all values equal to 0
        //Looks through CAFlakeCell array passed in
        for(int i = 0; i<flakeCells.length; i++ ){
            //If the cell is frozen, it the values are added to the sum
            if(flakeCells[i].getFrozen()==1){
                sum += 1;
                redSum += flakeCells[i].red;
                blueSum += flakeCells[i].blue;
                greenSum += flakeCells[i].green;
            }            
        }
        //If the sum is greater than 0, the color values are created
        if(sum>0){
            colorArray[0] = VerifyColor(10*sum + redSum/sum);//Equation for Red value
            colorArray[1] = VerifyColor(255-greenSum/sum);//Equation for Green value
            colorArray[2] = VerifyColor(6*sum + blueSum/sum);//Equation for Blue Value
        }        
        return colorArray; //Returns array with sums and color
    }
    
    //Method the verify the color is between 0 and 255, otherwise it is set within range
    private int VerifyColor(int color){
        if (color>255)
            color = 0;//Values greater than 255 become 0
        else if (color<0)
            color = 255;//Values less than 0 become 255
        return color;//Returns the color
    }
    
    @Override
    //Method to display rule as text
    public String toString() {
        return "Color Rule";
    }
    
}