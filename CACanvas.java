package edu.neu.csye6200.cacrystal;

import edu.neu.csye6200.obs3.EventSubscriber;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/*
Keith Fleming
Canvas used to display the simulation
 */
public class CACanvas extends JPanel implements Observer {
    
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(CACanvas.class.getName());//Add logger to class
    private Color col = null;
    private long counter = 0L;
    private CAFlakeSet caFlakeSet = null;
    private ArrayList<CAFlake> caFlakeList;//
    int cellSize, count;
	
    /**
     * CellAutCanvas constructor
     */
    public CACanvas(CAFlakeSet instance) {
	col = Color.WHITE;
        caFlakeSet = instance; //Gets instance of CAFlakeSet
        instance.addObserver(this);//Set CACanvas as subscriber to CAFlakeSet
    }

    /**
     * The UI thread calls this method when the screen changes, or in response
     * to a user initiated call to repaint();
    */
    public void paint(Graphics g) {
	drawCellularAutomaton(g); // Draws simulation output
    }
	
    /**
     * Draw the CA graphics panel
     * @param g
     */
    public synchronized void drawCellularAutomaton(Graphics g) {
        log.info("Drawing cell automation " + counter++);//Logs when new Canvas is drawn
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();//Gets size of canvas

        //Fill background of canvas a black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, size.width, size.height);
        
        caFlakeList = caFlakeSet.getFlakeSet();//Gets ArrayList of CAFlakes    
        
        //If there is a value in the array, the last flake will be drawn
        if(!caFlakeList.isEmpty()){
            //Gets number of Flakes in caFlakeList
            count = caFlakeList.size();
            g2d.setColor(Color.WHITE);
            //Displays which step of the simulation is currently on
            g2d.drawString("Step #" + count, 10, 15);
            //Gets the rule being used by the simulation
            Rule rule = caFlakeSet.getRule();
            //Gets the cellSize to use for the display
            cellSize = getCellSize(size, caFlakeList.get(0));
            //Gets number of cells in array
            int maxLength = caFlakeSet.getLastFlake().getFlake().length;
            //Calculates number of pixels to add to keep Flake centered in Canvas
            int centerWidth = (size.width-(maxLength*cellSize))/2;
            int centerHeight = (size.height-(maxLength*cellSize))/2;
            //Scans latest flake
            for (int j = 0; j < maxLength; j++) {                
                for (int i = 0; i < maxLength; i++) {
                    //If the rule implements UseColor, the color is created for the cell 
                    if(rule instanceof UseColor){
                        //Gets RGB values from FlakeCell
                        int red = caFlakeSet.getLastFlake().getFlake()[i][j].red;
                        int green = caFlakeSet.getLastFlake().getFlake()[i][j].green;
                        int blue = caFlakeSet.getLastFlake().getFlake()[i][j].blue;
                        col = new Color(red, green, blue);//Creates color
                    }
                    //Otherwise the color is a shade of blue set by the distance from the center
                    else{
                        //Calculated distance
                        int distance = (int)Math.sqrt(Math.pow((i-maxLength/2), 2) + Math.pow((j-maxLength/2), 2));
                        int redVal = 0;
                        int greenVal = 0;
                        int blueVal = validColor(255 - distance*255/maxLength);//Creates blue value
                        col = new Color(redVal, greenVal, blueVal);//Creates color
                    }   
                    // If the FlakeCell is frozen, it will be displayed
                    if(caFlakeSet.getLastFlake().getFlake()[i][j].getFrozen() == 1) {
                        //If the rule is a HexRule a hexagon is drawn
                        if(rule instanceof HexRule){                           
                            int offset = (j%2)*cellSize/2;//Offset value is calculated
                            double deg = Math.toRadians(30); //Angle is converted to radians
                            int adj = (int) (Math.tan(deg)*cellSize/2); //Value calculated to help draw hexagons
                            //Method called to draw hexagon
                            paintHex(g2d, (i*cellSize)+centerWidth-cellSize/4 + offset, (j*cellSize) + centerHeight-adj/2, cellSize-1, adj, col);
                        }
                        //For all other rules a rectangle is drawn
                        else{
                            //Method called to draw rectangle
                            paintRect(g2d,(i*cellSize)+centerWidth, (j*cellSize) + centerHeight, cellSize-1, col);
                        }   
                    }
                }
            }
        }
        //If there is no flake yet, a message is displayed to users
        else {
            //Gives some basic instructions to how to start app
            g2d.setColor(Color.RED);
            g2d.drawString("Let's make a SnowFlake!", 10, 15);
            g2d.drawString("Select a rule and initialization, enter the number of steps, and press 'Start'", 10, 30);
        }
    }
	
    /*
    * A local routine to ensure that the color value is in the 0 to 255 range;
    */
    private int validColor(int colorVal) {
	if (colorVal > 255)
            colorVal = 255;
	if (colorVal < 0)
            colorVal = 0;
	return colorVal;
	}
	
    /*
     * A convenience routine to set the color and paint a square
     * @param g2d the graphics context
     * @param x horizontal offset in pixels
     * @param y vertical offset in pixels
     * @param size the square size in pixels
     * @param color the color to draw
     */
    private void paintRect(Graphics2D g2d, int x, int y, int size, Color color) {
	g2d.setColor(color);
	g2d.fillRect(x, y, size, size);
    }
    /**
     * A method to get the proper CellSize for the display
     * @param size a dimension object
     * @param flake the CAFlake that is being drawn
     * @return the value to use for cellSize
     */
    private int getCellSize(Dimension size, CAFlake flake) {
        int minSize;
        //Finds the cellSize for length and height, uses minimum
        minSize = size.width/(caFlakeSet.getLastFlake().getFlake().length);
        if(minSize>size.height/(caFlakeSet.getLastFlake().getFlake()[0].length))
            minSize = size.height/(caFlakeSet.getLastFlake().getFlake()[0].length);       
        return minSize;//returns the minimum size
    }
    
    /**
     * 
     * @param g2d the graphics context
     * @param i horizontal offset
     * @param j vertical offset
     * @param cellSize size of cell to draw
     * @param adj Height for top and bottom point of hexagon
     * @param color Color to draw with
     */
    private void paintHex(Graphics2D g2d, int i, int j, int cellSize, int adj, Color color) {
        //Creates array for x-coordinates of hexagon
        int[] xpoints = {i+cellSize/2,i,i, i+cellSize/2, i+cellSize, i+cellSize};
        //Creates array for y-coordinated of hexagon
        int[] ypoints = {j, j+adj, j+cellSize,j+cellSize+adj,j+cellSize,j+adj};
        int npoints = 6;
        Polygon hex = new Polygon(xpoints, ypoints, npoints);//Creates hexagon
        g2d.setColor(color);
        g2d.fillPolygon(hex);//Draws hexagon
    }

    @Override
    //Method to update Canvas when notified(i.e. New Flake is added)
    public void update(Observable o, Object o1) {
        repaint();//Repaints the canvas with new ArrayList
    }
	
}
