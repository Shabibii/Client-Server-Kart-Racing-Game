/**
 *
 * @author 1819364
 */
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Sender implements Runnable 
{
    private static InetAddress address;

    public static boolean sendMessage(String message) throws Exception 
    {
        if(message.isEmpty()) return true; // stop function if message is empty
        
        // Convert string to bytes
        byte buf[] = message.getBytes(); 

        // Send packet to server
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, RaceTrack.port);
        RaceTrack.socket.send(packet);
        
        return !message.equals("exit"); // is exit message or not
    }

    @Override
    public void run() 
    {
        // Get server address
        try {
            address = InetAddress.getByName(RaceTrack.host);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
            System.out.println("Could not initalize address: " + RaceTrack.host);
        }

        boolean connected = false;
        
        // Wait for client to succesfully send packet
        while(!connected)
        {
            try 
            {
                // Send greeting message
                sendMessage("Hi");
                // Set connection status to true
                connected = true;

            } catch (Exception e) {}
        }
        System.out.println("Client sender configured successfully");
    }
}
