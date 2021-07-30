/**
 *
 * @author 1819364
 */
import java.io.IOException;
import java.net.DatagramPacket;

class Receiver implements Runnable 
{
    static byte buffer[] = new byte[1024]; // message buffer
    
    public static String Recieve()
    {
        try 
        {
            // Initalize packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            // Recieve data from network into packet
            RaceTrack.socket.receive(packet);
            // Convert pack data to string
            String serverMessage = new String(packet.getData(), 0, packet.getLength());
            // Return recieved message
            return serverMessage;

        } catch (IOException e) 
        {
            System.err.println(e.getMessage());  // catch exception
        }
        
        return null;
    }
    
     private static boolean RespondToServer(String serverMessage)
     {
        System.out.println("Server: " + serverMessage);
        if(serverMessage.equals("0"))
        {
            // Allocate Aru kart
            RaceTrack.assignedKart = 0;
            System.out.println("Allocated Aru kart");
            RaceTrack.LoadForm();
        }
        else if(serverMessage.equals("1"))
        {
            // Allocate Opp kart
            RaceTrack.assignedKart = 1;
            System.out.println("Allocated Opp kart");

            try {
                Sender.sendMessage("start");// send start signal
            } catch (Exception e) {
                // failed to send start message
            }
           
            RaceTrack.LoadForm();
        }
        else if(serverMessage.equals("start"))
        {
             // Start game
            Kart.serverActive = true;
        }
        else if(serverMessage.equals("exit"))
        {
             // Close application and game
             System.out.println("Server closing...");
             // Close game window
             RaceTrack.window.dispose();
             // Forcefully close application
             System.exit(0);
             return false; // stop running
        }
        else
        {
            String[] args = serverMessage.split(" ");
            if(args != null && args.length == 6)
            {
                Kart.kartAru.UpdateKartDetails(args);
                Kart.kartOpp.UpdateKartDetails(args);
            }
        }       
        return true;  // continue running game 
    }
     
    public Receiver() {}

    public static boolean RecieveAndRespond()
    {
        String r = Recieve();

        try {
            return RespondToServer(r);
        } catch (Exception e) {
            System.err.println(e.getMessage());  // catch exception 
            return false;
        }
    }

    @Override
    public void run() {

        while (RecieveAndRespond()) {}
    }
}