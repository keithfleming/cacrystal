package edu.neu.csye6200.cacrystal;

import java.util.ArrayList;
import java.util.Observable;

/*
Keith Fleming
This class is an arraylist of the CAFlakes in a set
 */
public class CAFlakeSet extends Observable implements Runnable{
    private static CAFlakeSet instance = null;
    private ArrayList<CAFlake> flakeSet; //ArrayList to store CAFlake objects
    enum State {RUNNING, PAUSED, STOPPED} //Enumeration for possible states of the system
    public static State currentState = State.STOPPED; //The current state of the system is stopped
    Rule rule; //Rule will be retrieved from GUI during simulation
    
    //Singleton pattern creation for CAFlakeSet
    public static CAFlakeSet instance() {
        if(instance == null) instance = new CAFlakeSet(); //If CAFlakeSet does not exist, build one
        return instance; //Return only instance of CAFlakeSet
    }
    
    //Constructor method for CAFlakeSet
    CAFlakeSet() {
        //Creates ArrayList for storing flakes
        flakeSet = new ArrayList<>();
    }
    
    @Override
    //Run method for simulation
    public void run() {
        flakeSet.clear();//Clears any previous simulations
        rule = (Rule) CAFlakeApp.ruleCBox.getSelectedItem(); //Gets rule to use from GUI
        int steps = Integer.parseInt(CAFlakeApp.stepsTF.getText()); //Gets number of steps to run for from GUI
        Simulate(steps,rule); //Runs simulation with given rule for number of steps
    }
    
    //Method to run simulation. The number of steps and rule to use are passed into the method
    private void Simulate(int steps, Rule rule) {
        //Method runs for the number of steps
        int size = steps*2 + 1;  //Sets the size of the Flake array as twice the number of steps plus one to keep the size odd
        //Simulation will run for the given number of steps
        for(int i = 0; i<steps;i++) {
            switch (currentState){//If the currentState is set to RUNNING, the simulation will complete another step
                case RUNNING:
                    CAFlake newFlake; //Creates a new flake
                    System.out.println("Step #" + i+1 + ":");
                    //If this is the start of a simulation, a new flake is created
                    if (i == 0) {
                    newFlake = rule.InitializeFlake(size);//Initializes first flake
                    flakeSet.add(newFlake);//Adds flake to flakeSet
                    newFlake.OutputFlake(); //Displays flake in console
                    }
                    //Otherwise, the rule passed in is used to create a new flake from the previous flake
                    else {
                        newFlake = rule.NextFlake(flakeSet.get(i-1), size);//Creates new flake qccording to the selected rule, using the preious flake
                        flakeSet.add(newFlake); //Adds flake to flakeSet
                        newFlake.OutputFlake(); //Displays flakein console
                    }
                    
                    setChanged();
                    notifyObservers(); //Notifies the Canvas that there is a new flake to display                    
                    try {                        
                        Thread.sleep(CAFlakeApp.speedSlider.getValue()); //Thread sleeps for length given by GUI               
                    } catch (InterruptedException ex) {
                    //Thread sleeps
                    }
                    break;
                //If the thread is paused, the simulation does not progress    
                case PAUSED:
                    i--; // Deincriment the counter to preent progression
                    try {
                        Thread.sleep(100);//Thread for 0.1s                 
                    } catch (InterruptedException ex) {
                    //Thread sleeps
                    }
                    break;
                //If the currentState of the simulation is STOPPED, the steps are set to i to cause end of thread
                case STOPPED:
                    steps = i;//Ends for loop and completes thread
                    break;                    
            }
        }
        currentState = State.STOPPED;//Sets currentState to system to STOPPED at completion of thread
    }
    
    //Method to return the CAFlakeSet ArrayList
    public synchronized ArrayList<CAFlake> getFlakeSet() {
        return flakeSet;
    }
    
    //Method to get the last flake in flakeSet
    public synchronized CAFlake getLastFlake(){
        return flakeSet.get(flakeSet.size()-1);
    }
    
    //Method to get the rule in use by the Simulation
    public Rule getRule() {
        return rule;
    }     
}
