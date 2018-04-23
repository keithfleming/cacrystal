package edu.neu.csye6200.cacrystal;

/*
Keith Fleming
Hexagon, based on hexagin pattern, uses adjecent cells to set frozen/unfrozen
 */
public class HexRule extends Rule{
    
    @Override
    //Method to create criteria for rule
    public boolean Criteria(CAFlake flake, int i, int j){
        int sum = HexSum(flake,i,j); //Gets sum fo adjacent cells
        return sum==1; //If there is one adjecent cell, it is set to frozen
    }
    
    //Method to get sum of adjacent cells
    private int HexSum(CAFlake flake, int i, int j) {
        int offset = j%2;//Offset based on current row
        int hexSum=0;//Initializes sum as 0
        //Looks at six surronding FlakeCells, if they are frozen one is added to the sum
        hexSum += super.getFlakeValue(flake, i+1 , j);
        hexSum += super.getFlakeValue(flake, i + offset, j+1);
        hexSum += super.getFlakeValue(flake, i-1+offset, j+1);
        hexSum += super.getFlakeValue(flake, i-1 ,j);                
        hexSum += super.getFlakeValue(flake, i+offset, j-1);                
        hexSum += super.getFlakeValue(flake, i-1+offset, j-1);
        return hexSum;//Returns sum
    }   
  
    @Override
    //Method to view rule as String
    public String toString(){
        return "Hexagon Rule";
    }
}
