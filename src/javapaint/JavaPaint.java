/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javapaint;

/**
 *
 * @author lukasz
 */
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class JavaPaint extends JFrame
{
	
		JButton brushBut, lineBut, ellipseBut, rectBut, strokeBut, fillBut, undoBut, rubberBut, saveBut;
                JComponent table;
                int z;
                ArrayList <Integer> removeBrush = new ArrayList<>();
//                BufferedImage image;
                
//                ArrayList<Shape> shapes;
//                ArrayList<Color> shapeFill;
//                ArrayList<Color> shapeStroke;
//                ArrayList<Float> transPercent;
                //        	
                ArrayList<Shape> shapes = new ArrayList<Shape>();
                ArrayList<Color> shapeFill = new ArrayList<Color>();
                ArrayList<Color> shapeStroke = new ArrayList<Color>();
                ArrayList<Float> transPercent = new ArrayList<Float>();
//		
		// Slider used to change the transparency
		
		JSlider transSlider;
		
		JLabel transLabel;
		
		// Makes sure the float for transparency only shows 2 digits
		
		DecimalFormat dec = new DecimalFormat("#.##");
		
		// Contains all of the rules for drawing 
		
		Graphics2D graphSettings;
		
		// Homework use graphSettings.setStroke(new BasicStroke(5F));
		// To change the stroke dynamically with a component
		
		// Going to be used to monitor what shape to draw next
		
		int currentAction = 1;
		
		// Transparency of the shape
		
		float transparentVal = 1.0f;
		
		// Default stroke and fill colors
		
		Color strokeColor=Color.BLACK, fillColor=Color.BLACK;
	
        public static void main(String [] args)
        {
                new JavaPaint();
        }

        public JavaPaint()
        {
        	// Define the defaults for the JFrame
        	
            this.setSize(1000, 600);
            this.setTitle("Java Paint");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel buttonPanel = new JPanel();
            
            // Swing box that will hold all the buttons
            
            Box theBox = Box.createHorizontalBox();
            
            // Make all the buttons in makeMeButtons by passing the
            // button icon. 
            
            brushBut = makeMeButtons("./src/brush.png", 1);
            lineBut = makeMeButtons("./src/Line.png", 2);
            ellipseBut = makeMeButtons("./src/Ellipse.png", 3);
            rectBut = makeMeButtons("./src/Rectangle.png", 4);
            
            // Make all the buttons in makeMeColorButton by passing the
            // button icon and true for stroke color or false for fill
            
            strokeBut = makeMeColorButton("./src/Stroke.png", 5, true);
            fillBut = makeMeColorButton("./src/Fill.png", 6, false);
            undoBut = makeMeButtons2("./src/undo.png");
            rubberBut = makeMeButtons2("./src/Rubber.png");
            saveBut = makeMeButtons2("./src/save.png");
            undoBut.addActionListener(new lUndoBut());
            rubberBut.addActionListener(new lRubberBut());
            saveBut.addActionListener(new lSaveBut());
            
            
            // Add the buttons to the box
            
            theBox.add(saveBut);
            theBox.add(undoBut);
            theBox.add(rubberBut);
            theBox.add(brushBut);
            theBox.add(lineBut);
            theBox.add(ellipseBut);
            theBox.add(rectBut);
            theBox.add(strokeBut);
            theBox.add(fillBut);
            
            // Add the transparent label and slider
            
            transLabel = new JLabel("Transparent: 1");
            
            // Min value, Max value and starting value for slider
            
            transSlider = new JSlider(1, 99, 99);
            
            // Create an instance of ListenForEvents to handle events
            
            ListenForSlider lForSlider = new ListenForSlider();
            
            // Tell Java that you want to be alerted when an event
            // occurs on the slider
           
            transSlider.addChangeListener(lForSlider);

            theBox.add(transLabel);
            theBox.add(transSlider);
            
            // Add the box of buttons to the panel
            
//            buttonPanel.add(undoBut);
//            buttonPanel.add(rubberBut);
            buttonPanel.add(theBox);

            // Position the buttons in the bottom of the frame
            
            this.add(buttonPanel, BorderLayout.SOUTH);
            
            // Make the drawing area take up the rest of the frame
            
            table = new DrawingBoard();
            this.add(table, BorderLayout.CENTER);
            
            // Show the frame
            
            this.setVisible(true);
        }
        
        // Spits out buttons based on the image supplied
        // actionNum represents each shape to be drawn
        
        public JButton makeMeButtons(String iconFile, final int actionNum){
        	JButton theBut = new JButton();
            Icon butIcon = new ImageIcon(iconFile);
            theBut.setIcon(butIcon);
            
            // Make the proper actionPerformed method execute when the
            // specific button is pressed
            
            theBut.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					currentAction = actionNum;
					
				}
            });
            
            return theBut;  
        }
        
        // Spits out buttons based on the image supplied and
        // whether a stroke or fill is to be defined
        
        public JButton makeMeColorButton(String iconFile, final int actionNum, final boolean stroke){
        	JButton theBut = new JButton();
            Icon butIcon = new ImageIcon(iconFile);
            theBut.setIcon(butIcon);
            
            theBut.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					if(stroke){
						
						// JColorChooser is a popup that lets you pick a color
						
						strokeColor = JColorChooser.showDialog(null,  "Pick a Stroke", Color.BLACK);
					} else {
						fillColor = JColorChooser.showDialog(null,  "Pick a Fill", Color.BLACK);
					}
					
				}
            });
            
            return theBut;  
        }
        public JButton makeMeButtons2(String iconFile){
        	JButton theBut = new JButton();
            Icon butIcon = new ImageIcon(iconFile);
            theBut.setIcon(butIcon);
            
          
            return theBut;  
        }



        private class DrawingBoard extends JComponent
        {
        	
        	// ArrayLists that contain each shape drawn along with
        	// that shapes stroke and fill
//        	
//                ArrayList<Shape> shapes = new ArrayList<Shape>();
//                ArrayList<Color> shapeFill = new ArrayList<Color>();
//                ArrayList<Color> shapeStroke = new ArrayList<Color>();
//                ArrayList<Float> transPercent = new ArrayList<Float>();
                
                Point drawStart, drawEnd;

                // Monitors events on the drawing area of the frame
                
                public DrawingBoard()
                {
                	
                        this.addMouseListener(new MouseAdapter()
                          {
                        	
                            public void mousePressed(MouseEvent e)
                            {
                            	
                            	if(currentAction != 1){
                            	
                            	// When the mouse is pressed get x & y position
                            	
                            	drawStart = new Point(e.getX(), e.getY());
                            	drawEnd = drawStart;
                                repaint();
                                
                            	}
                            	
                                
                                }

                            public void mouseReleased(MouseEvent e)
                                {
                            	
                            	if(currentAction != 1){
                            	
                            	  // Create a shape using the starting x & y
                            	  // and finishing x & y positions
                            	
                            	Shape aShape = null;
                            	
                            	if (currentAction == 2){
                            		aShape = drawLine(drawStart.x, drawStart.y,
                            				e.getX(), e.getY());
                            	} else 
                            	
                            	if (currentAction == 3){
                            		aShape = drawEllipse(drawStart.x, drawStart.y,
                            				e.getX(), e.getY());
                            	} else 
                            	
                            	if (currentAction == 4) {
                            		
                            		// Create a new rectangle using x & y coordinates
                            		
                                    aShape = drawRectangle(drawStart.x, drawStart.y,
                                    		e.getX(), e.getY());
                            	}
                                  
                                  // Add shapes, fills and colors to there ArrayLists
                                  
                                  shapes.add(aShape);
                                  shapeFill.add(fillColor);
                                  shapeStroke.add(strokeColor);
                                  
                                  // Add transparency value to ArrayList
                                  
                                  transPercent.add(transparentVal);
                                  
                                  drawStart = null;
                                  drawEnd = null;
                                  
                                  // repaint the drawing area
                                  
                            	}
                                  repaint();
                                  
                                  
                                }
                          } );

                        this.addMouseMotionListener(new MouseMotionAdapter()
                        {
                        	
                          public void mouseDragged(MouseEvent e)
                          {
//                                  System.out.println("wielkosc shapes " + (shapes.size()));
//                                z = shapes.size();
//                                z++;
                        	  // If this is a brush have shapes go on the screen quickly
                        	  
                        	  if(currentAction == 1){
                      			
                      			int x = e.getX();
                      			int y = e.getY();
//                                        System.out.println(z);
                      			Shape aShape = null;
                      			
                      			// Make stroke and fill equal to eliminate the fact that this is an ellipse
                      			
                      			strokeColor = fillColor;
                      			
                                  
                      			aShape = drawBrush(x,y,5,5);
                      			shapes.add(aShape);
                                  shapeFill.add(fillColor);
                                  shapeStroke.add(strokeColor);
                                  
                                  // Add the transparency value
                                  
                                  transPercent.add(transparentVal);
                      		} 
                        	  
                        	// Get the final x & y position after the mouse is dragged
                        	  
                        	drawEnd = new Point(e.getX(), e.getY());
//                                System.out.println(z);
                            repaint();
                          }
                        } );
                }
                

                public void paint(Graphics g)
                {
                		// Class used to define the shapes to be drawn
                	
                        graphSettings = (Graphics2D)g;

                        // Antialiasing cleans up the jagged lines and defines rendering rules
                        
                        graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Defines the line width of the stroke
                        
                        graphSettings.setStroke(new BasicStroke(4));

                        // Iterators created to cycle through strokes and fills
                        Iterator<Color> strokeCounter = shapeStroke.iterator();
                        Iterator<Color> fillCounter = shapeFill.iterator();
                        
                        // Iterator for transparency
                        
                        Iterator<Float> transCounter = transPercent.iterator();
                        
                        for (Shape s : shapes)
                        {
                        	
                        	// Sets the shapes transparency value
                            
                            graphSettings.setComposite(AlphaComposite.getInstance(
                                    AlphaComposite.SRC_OVER, transCounter.next()));
                        	
                        	// Grabs the next stroke from the color arraylist
                        	graphSettings.setPaint(strokeCounter.next());
                        	
                        	graphSettings.draw(s);
                        	
                        	// Grabs the next fill from the color arraylist
                        	graphSettings.setPaint(fillCounter.next());
                        	
                        	graphSettings.fill(s);
                        }

                        // Guide shape used for drawing
                        if (drawStart != null && drawEnd != null)
                        {
                        	// Makes the guide shape transparent
                            
                            graphSettings.setComposite(AlphaComposite.getInstance(
                                    AlphaComposite.SRC_OVER, 0.40f));
                        	
                            // Make guide shape gray for professional look
                            
                        	graphSettings.setPaint(Color.LIGHT_GRAY);
                        	
                        	Shape aShape = null;
                        	
                        	if (currentAction == 2){
                        		aShape = drawLine(drawStart.x, drawStart.y,
                                		drawEnd.x, drawEnd.y);
                        	} else 
                        	
                        	if (currentAction == 3){
                        		aShape = drawEllipse(drawStart.x, drawStart.y,
                                		drawEnd.x, drawEnd.y);
                        	} else 
                        	
                        	if (currentAction == 4) {
                        		
                        		// Create a new rectangle using x & y coordinates
                        		
                                aShape = drawRectangle(drawStart.x, drawStart.y,
                                		drawEnd.x, drawEnd.y);
                        	}
                                
                                
                                graphSettings.draw(aShape);
                        }
                }

                private Rectangle2D.Float drawRectangle(
                        int x1, int y1, int x2, int y2)
                {
                	// Get the top left hand corner for the shape
                	// Math.min returns the points closest to 0
                	
                        int x = Math.min(x1, x2);
                        int y = Math.min(y1, y2);
                        
                        // Gets the difference between the coordinates and 
                        
                        int width = Math.abs(x1 - x2);
                        int height = Math.abs(y1 - y2);

                        return new Rectangle2D.Float(
                                x, y, width, height);
                }
                
                // The other shapes will work similarly
                // More on this in the next tutorial
                
                private Ellipse2D.Float drawEllipse(
                        int x1, int y1, int x2, int y2)
                {
                        int x = Math.min(x1, x2);
                        int y = Math.min(y1, y2);
                        int width = Math.abs(x1 - x2);
                        int height = Math.abs(y1 - y2);

                        return new Ellipse2D.Float(
                                x, y, width, height);
                }
                
                private Line2D.Float drawLine(
                        int x1, int y1, int x2, int y2)
                {

                        return new Line2D.Float(
                                x1, y1, x2, y2);
                }
                
                private Ellipse2D.Float drawBrush(
                        int x1, int y1, int brushStrokeWidth, int brushStrokeHeight)
                {
                	removeBrush.add(shapes.size());
                	return new Ellipse2D.Float(
                            x1, y1, brushStrokeWidth, brushStrokeHeight);
                	
                }

        }
        
     // Implements ActionListener so it can react to events on components
        
        private class ListenForSlider implements ChangeListener{
        	
        	// Called when the spinner is changed
        	
        	public void stateChanged(ChangeEvent e) {
        	
        		// Check if the source of the event was the button
        	
        		if(e.getSource() == transSlider){
        	
        			// Change the value for the label next to the spinner
        			// Use decimal format to make sure only 2 decimals are ever displayed
        	
        			transLabel.setText("Transparent: " + dec.format(transSlider.getValue() * .01) );
        			
        			// Set the value for transparency for every shape drawn after
        			
        			transparentVal = (float) (transSlider.getValue() * .01);
        			
        		}
        	
        	}
        	
        }
        private class lUndoBut implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == undoBut && !shapes.isEmpty()) {
                        System.out.println("removeBrush " + removeBrush.isEmpty());
                        System.out.println("ksztalty " + shapes);
                        System.out.println("elementy remove " + (removeBrush.size()));
                        System.out.println("elementy shape " + (shapes.size()));
                        Shape aShape = shapes.get(shapes.size()-1);
                        
                        System.out.println(aShape.getBounds2D().getWidth());
                        
                        if (aShape.getBounds2D().getWidth()==5.0&&!removeBrush.isEmpty()){
//                        if (removeBrush.get(removeBrush.size()-1)==shapes.size()-1&&!removeBrush.isEmpty()){
                                System.out.println("wchodzi do if");
                            for(;!removeBrush.isEmpty()&&removeBrush.get(removeBrush.size()-1)==removeBrush.get(removeBrush.size()-2)+1;)
//                                while(removeBrush.get(removeBrush.size()-1)==removeBrush.get(removeBrush.size()-2)+1&&!removeBrush.isEmpty())
                                {
                                    System.out.println("wchodzi do while");
                                removeBrush.remove(removeBrush.size()-1);
                                    System.out.println("usuwam pedzel " + (removeBrush.size()-1));
                                    shapes.remove(shapes.size()-1);
                                    System.out.println("usuwam ksztalt " + (shapes.size()-1));

                                    shapeFill.remove(shapeFill.size()-1);
                                    shapeStroke.remove(shapeStroke.size()-1);
                                    transPercent.remove(transPercent.size()-1);
                                    
                                    repaint();
                                }
                                
                        }else{
                                    shapes.remove(shapes.size()-1);
                                    shapeFill.remove(shapeFill.size()-1);
                                    shapeStroke.remove(shapeStroke.size()-1);
                                    transPercent.remove(transPercent.size()-1);
                                    
                                    repaint();
                        }
                    }
//                    else if (e.getSource() == undoBut && !shapes.isEmpty() && currentAction==1) {
//                                    System.out.println(z);
//                                    System.out.println(shapes.size());
//                        for(int i=0; i<z; i++){
//                                    shapes.remove(shapes.size()-1);
//                                    shapeFill.remove(shapeFill.size()-1);
//                                    shapeStroke.remove(shapeStroke.size()-1);
//                                    transPercent.remove(transPercent.size()-1);
//                                    
//                                    }
//                                    z=shapes.size();
//                                    repaint();
//                    }
                    System.out.println(shapes.size()); 
                    System.out.println(removeBrush.size()); 
               }
        }
        private class lRubberBut implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == rubberBut) {
                                    shapes.clear();
                                    shapeFill.clear();
                                    shapeStroke.clear();
                                    transPercent.clear();
                                    repaint();
                    }

                }
        }
    private  class lSaveBut implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == saveBut) {
                        try {
                            BufferedImage image = ScreenImage.createImage(table);
//                            String graphFileName = JOptionPane.showInputDialog("Please enter a name for the S1Mian graphical output file: ");
                            JFileChooser fileBrowser = new JFileChooser();
//                            int result = fileBrowser.showOpenDialog(this);
                            
                            fileBrowser.setFileFilter(new FileNameExtensionFilter(".gif", "gif"));
                            fileBrowser.setFileFilter(new FileNameExtensionFilter(".png", "png"));
                            fileBrowser.setFileFilter(new FileNameExtensionFilter(".bmp", "bmp"));
                            fileBrowser.setFileFilter(new FileNameExtensionFilter(".wbmp", "wbmp"));
                            fileBrowser.setFileFilter(new FileNameExtensionFilter(".jpeg", "jpeg"));
                            fileBrowser.setFileFilter(new FileNameExtensionFilter(".jpg", "jpg"));
                            fileBrowser.setAcceptAllFileFilterUsed(false);
                            fileBrowser.setMultiSelectionEnabled(false);
                            
                            
                            
                            if (fileBrowser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                                String fileName = fileBrowser.getSelectedFile().getAbsolutePath()+fileBrowser.getFileFilter().getDescription();
//                                fileBrowser.getFileFilter().getDescription();
//                                System.out.println(fileName);
                                ScreenImage.writeImage(image, fileName);
                            }
                            
                            
//                            ScreenImage.writeImage(image, "./src/save.jpg");
//                            JOptionPane.showMessageDialog(null, graphFileName + " has been created and saved to your directory...", "File Saved", JOptionPane.INFORMATION_MESSAGE);
                            
                        } catch (IOException ex) {
                            Logger.getLogger(JavaPaint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
}