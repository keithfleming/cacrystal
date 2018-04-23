package edu.neu.csye6200.cacrystal;

/*
Keith Fleming
Corner rule, uses adjacent corner cells to look set frozen/unfrozen
 */

public class CornerRule extends Rule{

    @Override
    //Method to create criteria for frozen/unfrozen
    public boolean Criteria(CAFlake flake, int i, int j){
        int sum = CornerSum(flake, i, j); //Gets sum of 4 corner cells
        return(sum == 1|sum==3); //If sum is 1 or 3 it is set frozen
    }
    
    //Method to get the corner sum of a cell
    private int CornerSum(CAFlake flake, int i, int j) {
        int sum = 0;//Initializes sum as 0
        
        //If the corner is frozen, one is added to the sum
        sum += super.getFlakeValue(flake, i+1, j+1);
        sum += super.getFlakeValue(flake, i+1, j-1);
        sum += super.getFlakeValue(flake, i-1, j+1);
        sum += super.getFlakeValue(flake, i-1, j-1);        
        
        return sum;//Returns to sum
    }
    
    @Override
    //Method to view rule as String
    public String toString() {
        return "Corner Rule";
    }
}
