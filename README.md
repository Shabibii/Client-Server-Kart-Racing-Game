# Client Server Kart Racing Game
 A client-server application, enabling racing against each other from two different workstations.
 
In the command prompt, the directory has to be changed to the "src'' folder.
The files are already compiled. However, if needed, the files can be compiled using the "javac *.java" command in the same directory.

First)
Run the server from the command prompt window.
The server can be run using the following parameters:

java Server
java Server HostName
java Server HostName Port

Second)
Open another command prompt window and connect the first client.
The client can be run using the following parameters:

java RaceTrack
java RaceTrack HostName
java RaceTrack HostName Port

Third)
Open another command prompt window and connect the second client.

Due to the constraint of not executing the program on seperate workstations, the karts cannot be controlled at the same time.
However, the testing on seperate terminals indicates that this part is implemented appropriately.

Other notes:
The server manually waits for at least two clients to be connected.
A maximum of 2 clients are allowed into the server. 
Any attempt to connect to server after two clients have connected will be unsuccessful. 

Packets are sent out continuously till data is recieved. Packet data is then analysed
and IP addresses and ports are extracted. These data is then used to identify the clients

In the event of closing a client, the server will instruct the other client to be shut down to avoid wasted resources that sending data to an offline client would amount to.
