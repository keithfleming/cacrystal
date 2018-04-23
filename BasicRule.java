package edu.neu.csye6200.cacrystal;

/*
Keith Fleming
Basic rule, uses adjecent cells to set frozen/unfrozen
 */
public class BasicRule extends Rule{
    
    @Override
    //Method to create criteria for rule
    public boolean Criteria(CAFlake flake, int i, int j){
        int sum = AdjacentSum(flake, i, j);//Gets the adjacent sum
        return(sum == 1 | sum == 3);//If the sum equals 1 or 3 it returns true, else false
    }
    
    //Method to get sum of adjacent cells
    private int AdjacentSum(CAFlake flake, int i, int j) {
        int sum = 0;//Sum initialized at 0

        //Gets frozen values of adjacent cells
        sum += super.getFlakeValue(flake, i, j+1);
        sum += super.getFlakeValue(flake, i, j-1);
        sum += super.getFlakeValue(flake, i+1, j);
        sum += super.getFlakeValue(flake, i-1, j);
        return sum;
    }   
    
    @Override
    //Method to see Rule as string
    public String toString() {
        return "Basic Rule";
    }    
}
