/**
 *
 * @author 1819364
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread
{
    
    public static int port = 7334; // server port number    
    private final static int BUFFER = 1024; // buffer size   
    static byte[] buf = new byte[BUFFER];  // buffer space    
    private static DatagramSocket socket; // network socket    
    private static ArrayList<InetAddress> clientAddresses; // list of client addresses    
    private static ArrayList<Integer> clientPorts; // list of client ports    
    private static ArrayList<String> existingClients; // list of client ID's

    public static void main(String args[]) 
    {
        // Java Server
        // Java Server port
        try 
        {
            if(args.length > 0) port = Integer.getInteger(args[0]);
            socket = new DatagramSocket(port);
            clientAddresses = new ArrayList<InetAddress>(); // list of  client addresses
            clientPorts = new ArrayList<Integer>(); // list of client ports
            existingClients = new ArrayList<String>(); // list of existing clients
            // Run server
            RunServer();
        } 
        catch (IOException | InterruptedException ex) 
        {
            // Print out error message
            System.out.println(ex.getMessage());
        }

    }
    
    private static String[] GetClientMessage() throws IOException, InterruptedException
    {
        // Return array in format [Client id, client message]
        Arrays.fill(buf, (byte)0);

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();
        String id = clientAddress.toString() + ":" + clientPort;

        // Check if client is an old one
        if (!existingClients.contains(id)) 
        {
            // Note client details
            existingClients.add(id);
            clientPorts.add(clientPort);
            clientAddresses.add(clientAddress);
            
            System.out.println(id + " Joined the network");
            
            int idIntValue = existingClients.indexOf(id);
            String message = String.valueOf(idIntValue);
            
            if(idIntValue < 2)
            {
                SentToClient(id, message); // send only to specific client
            }
            else SentToClient(id, "exit"); // reject client
        }

        String clientMessage = new String(buf, buf.length).trim();

        if(!clientMessage.isEmpty())
        {
            System.out.println("Client: " + clientMessage);
            return new String[]{id, clientMessage};
        }
        else return null;
    }
    
    public static void SentToClient(String id, String message) throws IOException, InterruptedException
    {
         if(message.isEmpty()) return;
         
         System.out.println("Server: " + message);

         byte[] data = (message).getBytes();
         for (int i=0; i < existingClients.size(); i++) 
         {
            boolean isClient = existingClients.get(i) == null ? id == null : existingClients.get(i).equals(id);
            
            if(isClient)
            {
                InetAddress clientAddress = clientAddresses.get(i);
                int clientPort = clientPorts.get(i);

                DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
                socket.send(packet);
                break;
            }
          }          
    }
    
    public static boolean GetAndRespondToClient() throws IOException, InterruptedException
    {
        // Get client message
        String[] clientMessage = GetClientMessage();

        if(clientMessage == null) return true; 
        if(clientMessage[1].isEmpty()) return true;
        
        if (clientMessage[1].equals("exit"))
        {
            System.out.println("Server is now shutting down ");
            SentToClient(existingClients.get(0), clientMessage[1]);
            SentToClient(existingClients.get(1), clientMessage[1]);
            return false;
        }
        else if (clientMessage[1].equals("start"))
        {
            System.out.println("Both clients have joined the game ");
            SentToClient(existingClients.get(0), clientMessage[1]);
        }
        else
        {
            // Kart, kartDirection, kartX, kartY, Speed, Lap
            String[] args = clientMessage[1].split(" ");
            int clientIndex = args[0].equals("0") ? 1 : 0;
            SentToClient(existingClients.get(clientIndex), clientMessage[1]);
        }
        return true;
    }
    
    public static void RunServer() throws IOException, InterruptedException 
    {
        System.out.println("Server Running..");
        
        while (true) 
        {
            try 
            {
                if(!GetAndRespondToClient()) break;
            } 
            catch (Exception e) {}
        }
    }
}