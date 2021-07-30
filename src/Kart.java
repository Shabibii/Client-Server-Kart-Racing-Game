/**
 *
 * @author 1819364
 */
import java.awt.*;
import javax.swing.*;

import java.awt.image.*;

public class Kart extends JPanel
{
    /**
     *
     */
    private static final long serialVersionUID = 7313785660806057025L;    

    BufferedImage track; // image as an accessible buffer of image data
    protected ImageIcon[] spriteImages; // array of images from kartAru (Anglia Ruskin University kart) and kartOpp (Opposition kart)
    private final int TOTAL_IMAGES = 16; // number of images per kart: 16

    private int kart = 0;
    public int[] kartLocation; // array containing each kart's coordinates in the panel (x & y values)
    public int kartDirection; // kart's direction (16 sprites in total for each kart)
    public int kartSpeed; // speed of kart, displacement

    public static Kart kartAru;
    public static Kart kartOpp;
    public static Kart[] karts;

    public int kartLap; // current lap for kart

    private boolean hasPassedcheckPoint = false; // kart is halfway to the finish line

    private Rectangle checkPoint = new Rectangle(380, 95, 200, 107); // checkpoint rectangle

    public static boolean serverActive = false;

    // Track kart details
    public String KartDetails()
    {
        return String.valueOf(RaceTrack.assignedKart) + " " 
        + String.valueOf(karts[RaceTrack.assignedKart].kartSpeed) + " " 
        + String.valueOf(karts[RaceTrack.assignedKart].kartDirection) + " " 
        + String.valueOf(karts[RaceTrack.assignedKart].kartLocation[0]) + " " 
        + String.valueOf(karts[RaceTrack.assignedKart].kartLocation[1]) + " "
        + String.valueOf(karts[RaceTrack.assignedKart].kartLap);   
    }
    
    // Kart rectangle
    public Rectangle getKartRectangle()
    {   
        return new Rectangle( kartLocation[ 0 ], kartLocation[ 1 ], 35, 20 );
    }

    public void UpdateKartDetails(String[] changes)
    {
        // Update kart details based on parameter
        int index = Integer.parseInt(changes[0]);
        karts[index].kartSpeed = Integer.parseInt(changes[1]);
        karts[index].kartDirection = Integer.parseInt(changes[2]);
        karts[index].kartLocation[0] = Integer.parseInt(changes[3]);
        karts[index].kartLocation[1] = Integer.parseInt(changes[4]);
        karts[index].kartLap = Integer.parseInt(changes[5]);
    }

    // Retrieve images
    public static void RetreiveImages() throws Exception
    {
        kartAru = new Kart("C1_ARU/kartAru", 0);
        kartOpp = new Kart("C2_OPP/kartOpp", 1);
        karts = new Kart[]{kartAru, kartOpp};
    }

    public Kart( String path, int kart ) throws Exception
    {
        spriteImages = new ImageIcon[ TOTAL_IMAGES ];
        
        SoundEffect.init(); // pre-load sound effects
        SoundEffect.START.play(); // play trumpet sound

        this.kart = kart;
        StartPosition(); 

        // Load 16 images for eacht kart (kartAru & kartOpp), where count defines sprite/direction
        for ( int count = 0; count < spriteImages.length; count++ ) 
        {            
            spriteImages[ count ] = new ImageIcon( getClass().getResource( path + count + ".png" ) );         
        }

       
    }

    // Handles accelerating in game
    void Accelerate()
    {
        if (kartSpeed < 5 ) // maximum kart speed
        {
            kartSpeed += 1;
        } 
    }

    // Handles slowing down in game
    void Brake()
    {
        if ( kartSpeed > -5 ) // maximum kart speed in reverse mode
        {
            kartSpeed -= 1;    
        }            
    }
    
    void turnLeft()
    {
        // Rotate kart one sprite anti-clockwise
        kartDirection--;
        if( kartDirection == -1) kartDirection = 15;
    }

    void turnRight()
    {
        // Rotate kart one sprite clockwise
        kartDirection++;
        if( kartDirection == 16 ) kartDirection = 0;
    }

    // Kart movement
    void MoveKart() throws Exception
    {        
        int deltaX = 0, deltaY = 0; // difference in x, y values for kart's location

        // Driving the kart in a direction is based on the model below, kartDirection == 8 is the starting point of the karts
        //  .. for instance, when the kart's direction is sprite #1, the kartLocation changes are:  x + 2 & y + 1 
        //  .. 
        //                     |====|====|====|====|====|
        //                     | 10 | 11 | 12 | 13 | 14 |  
        //                     |====|====|====|====|====|
        //                     |  9 |    |    |    | 15 |
        //                     |====|====|====|====|====| 
        //                     |  8 |    | XY |    | 0  |
        //                     |====|====|====|====|====|
        //                     |  7 |    |    |    | 1  | 
        //                     |====|====|====|====|====|
        //                     |  6 | 5  | 4  | 3  | 2  | 
        //                     |====|====|====|====|====|
        // ..
        switch( kartDirection )
        {          
            case 0: case 8:
                deltaX = kartSpeed;             
                if( kartDirection == 0 )
                    deltaX *= 2; // right movement of kart ( kart location x value increases )
                if( kartDirection == 8 )
                    deltaX *= -2; // left movement of kart ( kart location x value decreases )               
                break;
                  
            case 4: case 12:
               deltaY = kartSpeed;                
               if( kartDirection == 4 ) 
               deltaY *= 2; // down movement of kart ( kart location: y value increases )
               if( kartDirection == 12 )
               deltaY *= -2; // right movement of kart ( kart location: y value decreases )
               break;  
            
            // Check diagonal sprites/directions
            case 1: case 2: case 3: case 5: case 6: case 7:
            case 9: case 10: case 11: case 13: case 14: case 15:
                deltaX = kartSpeed / 2; // slow kart speed when driving in diagonal directions, X
                deltaY = kartSpeed / 2; // slow kart speed when driving in diagonal directions, Y

                if ( kartDirection == 1 || kartDirection == 2 || kartDirection == 14 || kartDirection == 15  ) 
                {
                    deltaX *= 2;
                }
                if ( kartDirection == 3 || kartDirection == 13 ) 
                {
                    deltaX *= 1;
                }
                if ( kartDirection == 5 || kartDirection == 11 ) 
                {
                    deltaX *= -1;
                }
                if ( kartDirection == 6 || kartDirection == 7 || kartDirection == 9 || kartDirection == 10  ) 
                {
                    deltaX *= -2;
                }

                if ( kartDirection == 2 || kartDirection == 3 || kartDirection == 5 || kartDirection == 6  ) 
                {
                    deltaY *= 2;
                }
                if ( kartDirection == 1 || kartDirection == 7 ) 
                {
                    deltaY *= 1;
                }
                if ( kartDirection == 9 || kartDirection == 15 ) 
                {
                    deltaY *= -1;
                }
                if ( kartDirection == 10 || kartDirection == 11 || kartDirection == 13 || kartDirection == 14  ) 
                {
                    deltaY *= -2;
                }
                break;                     
        }
        // Handle collisions based on speed and location
        handleKartCollision( deltaX, deltaY );     
        CheckLap();  
    }

    public void SendKartDetails() throws Exception
    {
        Sender.sendMessage(KartDetails());
    }

    // Changes to speed (0) or game over, collision handling
    void handleKartCollision( int deltaX, int deltaY ) throws Exception
    {                
        int xValueKart = kartLocation[ 0 ] + deltaX; // new x position of kart, kartLocation[0] is equal to kart's current x coordinate
        int yValueKart = kartLocation[ 1 ] + deltaY; // new y position of kart, kartLocation[1] is equal to kart's current y coordinate

        boolean road = false;
        Rectangle[] r = RaceTrackJPanel.r;

        // Create rectangle for karts (w24 x l46 pixels), smaller window taken
        Rectangle currentKart =  new Rectangle( xValueKart, yValueKart, 35, 20 );
        for( int i = 0; i < r.length; i++ )
        {     
            if( r[ i ].contains( currentKart ) ) // check if kart rectangle is in any of the 'track' rectangles
            {                
                road = true;
                break;
            } 
        }      
      
        // If kart goes off the track (green area and out of boundaries)
        if( road == false )
        {
            kartSpeed = 0; // reduce kart speed to 0
            SoundEffect.GRASS.play(); // play "floop" sound
        }
        else
        {
            Rectangle kartAruRectangle = kartAru.getKartRectangle();
            Rectangle kartOppRectangle = kartOpp.getKartRectangle();

            // Check if karts' rectangles intersect with each other
            if( kartAruRectangle.intersects( kartOppRectangle ))
            {
                SoundEffect.CRASH.play(); // play explosion sound    

                kartAru.StartPosition();
                kartOpp.StartPosition();

                // Present user with message and how to replay game
                JOptionPane.showMessageDialog( null, "The karts crashed. The game will restart automatically", "CRASH", JOptionPane.WARNING_MESSAGE );
            }
            else
            {   
                // Nothing happened, do not interrupt kart movement
                kartLocation[ 0 ] = xValueKart;
                kartLocation[ 1 ] = yValueKart;
            }        
        }
    }

    // Check if lap has been passed
    // Check if lap has been passed
    public void CheckLap()
    {
        Rectangle[] r = RaceTrackJPanel.r;
        Rectangle currentKart =  getKartRectangle();
        
        // Check road rectangles from rectangle 4 to end
        if( checkPoint.intersects( currentKart ) ) // check if kart rectangle is in the 'checkPoint' rectangle
        {                
            hasPassedcheckPoint = true;
            return;
        }   
         
        // If intersects with finish line rectangle
        if(RaceTrackJPanel.finishLineRectangle.intersects(currentKart) && kartLap < 3 && hasPassedcheckPoint == true)
        {
            kartLap++;
            hasPassedcheckPoint = false; // kart has gone far enough
        }

        if( (kartAru.kartLap == 3 || kartOpp.kartLap == 3) && RaceTrackJPanel.timer.isRunning())
        {
            RaceTrackJPanel.timer.stop(); // stop timer, end of game
            SoundEffect.WIN.play(); // play cheering sound                           
            
            String winMessage = kartAru.kartLap == 3 ? "Aru Won" : "Opposition Won";
            // Present user with message and how to replay game
            JOptionPane.showMessageDialog( null, winMessage + " press 'R' to restart", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
            hasPassedcheckPoint= false;
        }
    }

    public void StartPosition() throws Exception
    {
        if (kart == 0)
        {
            kartLocation = new int[] {645, 500};
        }
        else
        {
            kartLocation = new int[] {645, 550};
        }
        hasPassedcheckPoint = false; // rest boolean that states that kart has gone far enough
        kartDirection = 8; // direction of kart    
        kartSpeed = 0; // speed/Displacement to zero
        kartLap = 0; // rest lap position      
        
        SoundEffect.START.play();
    }
    
    public void paint( Graphics g, RaceTrackJPanel panel )
    {
        spriteImages[ kartDirection ].paintIcon( panel, g, kartLocation[0], kartLocation[1]);
    }
}