package edu.neu.csye6200.cacrystal;

import edu.neu.csye6200.cacrystal.CAFlakeSet.State;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 *
 * @author Keith
 */
public class CAFlakeApp extends CAApp {
    
    private static final Logger log = Logger.getLogger(CAFlakeApp.class.getName());

    private JPanel northPanel = null;
    private JButton startBtn, stopBtn, pauseBtn;//Buttons used on northPanel
    protected static JComboBox<Rule> ruleCBox = null; //Rule CBox used in northPanel
    protected static JComboBox<String> initCBox = null; //String CBox used in northPanel
    private JLabel ruleLabel = null, stepsLabel = null, speedLabel = null, initLabel = null; //Labels used in northPanel
    protected static JTextField stepsTF = null; //TF used in northPanel
    protected static JSlider speedSlider = null; //JSlider used in northPanel 
    private CACanvas caPanel = null; //Canvas to display simulation
    private Dimension minSize = null; //Minimum dimension of frame
    private Rule basicRule = null, cornerRule = null, hexRule = null, colRule = null, colHexRule = null;//Rules added to ruleCBox
    Thread simThread; //Thread to run simulation
    CAFlakeSet simulation;//Instance of CAFlakeSet
	
    //Constructor for Frame
    public CAFlakeApp() {
        simulation = CAFlakeSet.instance();//Get instance of CAFlakeSet
        
        //Set frame size to appropriate sized box
    	frame.setSize(900, 700);
        //Create a minimum size to prevent components from the North panel frombeing hidden
        minSize = new Dimension(850,600);
        frame.setMinimumSize(minSize);
        //Add title to frame
	frame.setTitle("CAFlake Cellular Automata");
		
    	caPanel = new CACanvas(simulation); //Get CACanvas
    	frame.add(caPanel, BorderLayout.CENTER); // Add to the center of our frame
	frame.setVisible(true); // The UI is built, so display it
    }
   
    @Override
    //MEthod to get the NorthPanel of the Frame
    public JPanel getNorthPanel() {
    	northPanel = new JPanel();
    	northPanel.setLayout(new FlowLayout());
    	
        //Create a start button to run the simulation
        startBtn = new JButton("Start");
        northPanel.add(startBtn);
        startBtn.addActionListener(new ActionListener() {
            //Action Listener for the start button
            @Override
            public void actionPerformed(ActionEvent ae) {
                switch(CAFlakeSet.currentState) {
                    //If the simulation is already running nothing will happen
                    case RUNNING:
                        break;
                    //If the simulation is paused, the simulation will restart and the text will show start again     
                    case PAUSED:
                        CAFlakeSet.currentState = State.RUNNING;
                        startBtn.setText("Start");//Sets button text to Start
                        break;
                    //If the simulation is stopped, a new simulation will begin    
                    case STOPPED:
                        //Check for valid entry in Steps TF
                        if (verifyStepsTF()) {//Ensures steps are integer between 2 and 128
                            if(verifyInit()){//Ensures Hexagon rules are only initialized with a point
                                CAFlakeSet.currentState = State.RUNNING;//Sets simulation state to RUNNING
                                //If all conditions are met a new simulation will begin
                                simThread = new Thread(simulation);                            
                                simThread.start();//Starts thread
                            }
                            else{
                                //If invalid Rule and Initilization combination selection are picked, user is instructed to fix
                                JOptionPane.showMessageDialog(frame, "Can not run simulation.\nHexagon Rule and Color Hex Rule require 'Point' Initilization.\nPlease pick a different Rule or Initialization and try again!");
                            }
                        }
                        else {
                            //If the Steps TF is invalid, the user will be instructed to try with a different number
                            JOptionPane.showMessageDialog(frame, "Can not run simulation.\nSteps must be integer between 2 and 128.\nPlease enter a different number for steps and try again!");
                        }
                        break;
                }
            }            
        });
        
        //Create a pause button to freeze an existing simulaiton
        pauseBtn = new JButton("Pause");
        northPanel.add(pauseBtn);
        pauseBtn.addActionListener(new ActionListener(){
            //Add Action Listener for the Pause button
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Set the simulation to run and change text on "Start" button to Re-Start
                if(CAFlakeSet.currentState == State.RUNNING) {
                    CAFlakeSet.currentState = State.PAUSED;//Sets state of simualtion to PAUSED
                    startBtn.setText("Re-Start");//Sets "Start" button text to "R.-Start"
                }
                    
            }            
        });
    	
        //Create a stop button to end an existing simulation
        stopBtn = new JButton("Stop");
        northPanel.add(stopBtn);
        stopBtn.addActionListener(new ActionListener(){
            //Add an Action Listener to the Stop button
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Change the text on the Start button to "Start" and end the simulaiton
                startBtn.setText("Start");//Sets "Start" button text to "Start" (Could ahve been paused
                CAFlakeSet.currentState = State.STOPPED;
                //Change simulation steps;
            }            
        });
        
        //Create a combo box to select to rule for the simulation to use
        basicRule = new BasicRule();
        cornerRule = new CornerRule();
        hexRule = new HexRule();
        colRule = new ColorRule();
        colHexRule = new ColorHexRule();
        ruleCBox = new JComboBox<>();
        //Creates label for rule
        ruleLabel = new JLabel("Rule:");
        //Adds label
        northPanel.add(ruleLabel);
        //Adds items to ruleCBox
        ruleCBox.addItem(basicRule);
        ruleCBox.addItem(cornerRule);
        ruleCBox.addItem(colRule);
        ruleCBox.addItem(hexRule);
        ruleCBox.addItem(colHexRule);
        northPanel.add(ruleCBox);//Adds ruleCBox to frame
        
        //CBox with the different initialization options
        //Creates and adds label for CBox
        initLabel = new JLabel("Initialization:");
        northPanel.add(initLabel);
        initCBox = new JComboBox<>();
        //Adds Strings to initCBox
        initCBox.addItem("Point");//Single point
        initCBox.addItem("Box"); 
        initCBox.addItem("Cross");
        initCBox.addItem("'X'");
        northPanel.add(initCBox);//Adds initCBox to frame
                
        //Create a TF for users to enter the number of steps for the simulation to run
        stepsTF = new JTextField(3);
        stepsTF.setText("32");
        stepsLabel = new JLabel("Steps: ");
        northPanel.add(stepsLabel);
        northPanel.add(stepsTF);
                
        //Creates slider for user to select simulation speed
        //Sets minimum and maximum speed
        int speedSMin = 200;
        int speedSMax = 3000;
        //Creates label for slider
        speedLabel = new JLabel("Speed:");
        //Creates slider with based on given speeds
        speedSlider = new JSlider(speedSMin, speedSMax, (speedSMin+speedSMax)/2);
        //Sets labels for values on
        speedSlider.setPaintLabels(true);
        //Reverses direction of slider
        speedSlider.setInverted(true);
        //Create the label table
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
        table.put(speedSMin+200, new JLabel("Fast") );//Fast label associated with 200
        table.put(speedSMax-200, new JLabel("Slow") );//Slow label assocaited with 3000
        speedSlider.setLabelTable(table);//Adds labels to slider
        northPanel.add(speedLabel);//Adds label to northPanel
        northPanel.add(speedSlider);//Adds slider to northPanel                
        
    	return northPanel; //Returns northPanel
    }
    
    @Override
    //Method for an actionPerformed
    public void actionPerformed(ActionEvent ae) {
        log.info("We received an ActionEvent " + ae);       
    }
    
    //Method to verify the number of steps
    private boolean verifyStepsTF() {
        try{
            //Verifies that entry is an integer
            int steps = Integer.parseInt(stepsTF.getText());
            //Steps must be between 2 and 128.  If it is too big the cells can't be seen
            return(steps>1 && steps<=128);
        }
        catch(NumberFormatException ex) {
            //If entry is not an integer, the simulation won't start
            return false;
        }     
    }
    
    //Method to verify the initialization selection
    private boolean verifyInit() {
        boolean validInit = true;//Assume the selection is valid
        if((Rule)ruleCBox.getSelectedItem() instanceof HexRule){
            if(((String)initCBox.getSelectedItem()).compareTo("Point")!=0)
                validInit=false;//Hexagon Rules can only use point.  There isn't a hexagon box, cross or 'X'
        }
        return validInit;//Returns boolean     
    }
        
    @Override
    public void windowOpened(WindowEvent e) {
	log.info("Window opened");
    }

    @Override
    public void windowClosing(WindowEvent e) {	
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    	log.info("Window iconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {	
    	log.info("Window deiconified");
    }

    @Override
    public void windowActivated(WindowEvent e) {
    	log.info("Window activated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {	
	log.info("Window deactivated");
    }
    
    public static void main(String[] args) {
        //Main method to create GUI
        CAFlakeApp caFlakeApp = new CAFlakeApp();
        log.info("Simulation started");
    }    
}