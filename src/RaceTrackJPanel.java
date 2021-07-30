/**
 *
 * @author 1819364
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;

public class RaceTrackJPanel extends JPanel implements ActionListener, KeyListener
{	   
    /**
     *
     */
    private static final long serialVersionUID = 4992611432957196858L;
   
    BufferedImage track; // image as an accessible buffer of image data
    private BufferedImage kartLogo; // orginal logo, before changes, obtained from https://www.shutterstock.com/image-vector/kart-racing-logo-illustration-gaming-1589020807
    public static Timer timer; // Swing timer class object
    private JLabel statusLabelOne;
    private JLabel statusLabelTwo;

    // For handling off track borders, setting valid driving area.. 
    //   .. road is defined with rectangles
    //   .. create rectangles by setting location values on panel (x, y, width, height)
    static Rectangle[] r = new Rectangle[]
    {        
        new Rectangle( 45, 482, 755, 102 ),       new Rectangle( 30, 344, 135, 238 ), // rectangles 1 & 2 on this line
        new Rectangle( 45, 344, 338, 101 ),      new Rectangle( 232, 232, 151, 213 ), // rectangles 3 & 4     
        new Rectangle( 45, 225, 338, 106 ),       new Rectangle( 30, 90, 130, 241 ), // rectangles 5 & 6
        new Rectangle( 45, 90, 755, 97 ),        new Rectangle( 675, 90, 125, 208 ), // rectangles 7 & 8  
        new Rectangle( 383, 193, 417, 105 ),     new Rectangle( 375, 193, 166, 287 ), // rectangles 9 & 10      
        new Rectangle( 383, 375, 417, 105),      new Rectangle( 680, 375, 120, 207) // rectangles 11 & 12 
    };    
    
    public static Rectangle finishLineRectangle = new Rectangle( 580, 500, 28, 140);
    
    // Constructor
    public RaceTrackJPanel() throws Exception
    {   
        track = ImageIO.read( getClass().getResource( "Track/track.png" ) ); // obtain "track.png" from folder "Track" in current dir 
        kartLogo = ImageIO.read( getClass().getResource( "Track/kartLogo.png" ) ); 
        Kart.RetreiveImages();

        statusLabelOne = new JLabel();
        add(statusLabelOne);
        statusLabelTwo = new JLabel();
        add(statusLabelTwo);
                
        // Configure key listener to be notified when swing timer "goes off"
        addKeyListener( this );  
    
        // Fire swing timer
        timer = new Timer( 100, this );
        timer.start();

        // Update label text that shows race details
        updateFeed();
    } 

    // KeyPressed method
    @Override
    public void keyPressed( KeyEvent e ) 
    {
        if( !timer.isRunning() && e.getKeyCode() != KeyEvent.VK_R ) return;     
        if(RaceTrack.assignedKart == 0 && Kart.serverActive == false) return;               
        switch ( e.getKeyCode() ) 
        {                
            // Control keys for ARU kart
            case KeyEvent.VK_UP: Kart.karts[RaceTrack.assignedKart].Accelerate(); // key arrow up for speeding kart
               break;
            case KeyEvent.VK_DOWN: Kart.karts[RaceTrack.assignedKart].Brake(); // key arrow down for slowing down kart (or go back, negative value)
               break;
            case KeyEvent.VK_RIGHT: Kart.karts[RaceTrack.assignedKart].turnRight(); // key arrow right for turning kart one sprite clockwise
               break;
            case KeyEvent.VK_LEFT: Kart.karts[RaceTrack.assignedKart].turnLeft(); // key arrow left for turning kart one sprite anti-clockwise
               break;         
        }	
        try
        {
            Kart.kartAru.SendKartDetails(); // update Server of position of kart
            Kart.kartOpp.SendKartDetails(); // update Server of position of kart
        }
        catch (Exception ev)
        {
            ev.printStackTrace(); // catch exception 
        }	
    }   
    
    public static void Restart() 
    {
        try {

            Kart.kartAru.StartPosition(); // send kart to starting positions
            Kart.kartOpp.StartPosition();  timer.start(); // fire Swing timer again
        
        } catch (Exception e) {
            e.printStackTrace(); // catch exception 
        }
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        // Event handler for timer tick
        if( e.getSource() != timer ) return;
        for ( int i = 0; i < 2; i++ ) 
        {        
            try
            {
                Kart.kartAru.MoveKart(); // move each kart 
                Kart.kartOpp.MoveKart(); // move each kart
                
            }
            catch (Exception ev)
            {
                ev.printStackTrace(); // catch exception 
            }
        }      
       
        repaint();
    }

    @Override
    public void keyReleased( KeyEvent e ) { }

    @Override
    public void keyTyped( KeyEvent e ) {}
	
    @Override
    public void paintComponent(Graphics g) 
    {        
        super.paintComponent( g );
        
        g.drawImage( track, 50, 100, 746, 498, Color.white, this ); // draw track image
        g.drawImage( kartLogo, 366, 23, this );
        
        Kart.kartAru.paint(g, this); // get kart images and place them at correct position
        Kart.kartOpp.paint(g, this);  
        
        updateFeed();
    }

    private void updateFeed()
    {
         statusLabelOne.setText("<html><body align = 'left'>" + 
                              "<b><u>KART ARU</b></u><br>" +
                              "Laps Remaining : "+(3-Kart.kartAru.kartLap)+"<br>"  +
                              "Current Speed : "+Kart.kartAru.kartSpeed*30+" km/h <br>"   + 
                              "</body></html>");
         statusLabelOne.setFont( new Font( "Monospaced", 1, 15 ) );
         statusLabelOne.setLocation(121, 15);
                              
         statusLabelTwo.setText("<html><body align = 'left'>" + 
                              "<b><u>KART OPP</b></u><br>" + 
                              "Laps Remaining : "+(3-Kart.kartOpp.kartLap)+"<br>"  +
                              "Current Speed : "+Kart.kartOpp.kartSpeed*30+" km/h <br>"   +                      
                              "</body></html>");             
         statusLabelTwo.setFont( new Font( "Monospaced", 1, 15 ) );
         statusLabelTwo.setLocation(530, 15);
    }
} 
