/**
 *
 * @author 1819364
 */
import javax.swing.*;
import java.net.DatagramSocket;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RaceTrack extends JFrame 
{
   private static final long serialVersionUID = -2860419872081392909L;
    
   public static int port; // client assigned kart
   public static int assignedKart; // client assigned kart
   static DatagramSocket socket; // client socket
   public static String host; // server host
   public static RaceTrack window; // game window
        
	public static void main(String[] args) throws Exception
	{    
         host = args.length == 1 ? args[0] : "127.0.0.1";
         port = args.length == 2 ? Integer.parseInt(args[1]) : 7334;

         // Initialize Socket
         socket = new DatagramSocket();

         // Set up threads for recieving and sending client data
         new Thread(new Receiver()).start();
         new Thread(new Sender()).start();
	}

   public static void LoadForm() 
   {
      try 
      {
            // Set window 
            window = new RaceTrack();

            // Set window size 850 pixels by 650 pixels
            window.setSize(850, 650);
            window.setResizable(false);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            window.addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent e) {
                  try {
                     Sender.sendMessage("exit"); // send exit message
                  } catch (Exception e1) {
                     e1.printStackTrace(); // catch exception 
                  } 
               }
   
           });
            
            // Call frame with game information
            RaceTrackInfoFrame labelFrame = new RaceTrackInfoFrame(RaceTrack.assignedKart); // create object labelFrame
            labelFrame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE ); // note: HIDE_ON_CLOSE, so other frame exists
            labelFrame.setSize( 310, 325 ); // set frame size
            labelFrame.setResizable( false );
            labelFrame.setVisible( true ); // display frame   
      } catch (Exception e) {
         e.printStackTrace(); // catch exception 
      }
   }

   // Constructor
   public RaceTrack() throws Exception 
	{  
      // Retrieve panel from RaceTrackJPanel    
      RaceTrackJPanel panel = new RaceTrackJPanel();
         
      // Display panel 
      panel.setFocusable(true);
         
      // Fill JFrame with panel
		panel.setSize( WIDTH, HEIGHT );
		this.add( panel );
      setVisible(true);

	} // end constructor
} // end class RaceTrack