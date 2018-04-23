package edu.neu.csye6200.cacrystal;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 Abstract class for application
 Keith Fleming
 */
public abstract class CAApp implements ActionListener, WindowListener {
    protected JFrame frame = null;
	
    public CAApp() {
	//Initializes the GUI
        initGUI();
    }
        
    public void initGUI() {
        //Sets Frame
    	frame = new JFrame();
	frame.setTitle("JUIApp2");

	frame.setResizable(true); //Lets frame resize
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame.DISPOSE_ON_CLOSE)
		
	// Permit the app to hear about the window opening
	frame.addWindowListener(this); 
		
	frame.setLayout(new BorderLayout());
	frame.add(getNorthPanel(), BorderLayout.NORTH);

    }
    
    public abstract JPanel getNorthPanel() ;

}
