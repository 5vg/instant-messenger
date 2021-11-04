import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ChatServer {

    //an array of ConnectionHandlers is made to keep track of who is connected to the server
    private static ConnectionHandler[] connected = {null};

    //default port set
    private static int port = 14001;

    //server variable is made
    private static ServerSocket server = null;

    //getter for the ConnectionHandler array
    public static ConnectionHandler[] getConnected() {
        return connected;
    }

    //setter for the ConnectionHandler array (sets it when updated)
    public static void connectedUpdate(ConnectionHandler[] newConnected){
        connected = newConnected;
    }

    //sends system message when "exit" is entered into the terminal to all clients to disconnect
    public static void isExit() throws IOException {
        if (server != null){
            sysMessage("The server is closing, please type /exit or close your client.");
            server.close();
        }
    }

    //how the actual message is sent - goes through all connected via for loop and sends message
    private static void sysMessage(String msg){
        for(int i = 1; i < connected.length; i++){
            connected[i].sendMessage(msg);
        }
    }

    //main function
    public static void main(String[] args) {
        try {
            //looks for -csp argument and sets port if present
            for(int i=0; i<args.length; i++){
                if(args[i].equals("-csp")){
                    port = Integer.parseInt(args[i+1]);
                }
            }
            //if the -csp argument is entered incorrectly, uses default port (14001)
        }catch (Exception e){
            System.out.println("Port passed incorrectly, using default port");
        }

        try {
            //makes new server and outputs port to terminal
            server = new ServerSocket(port);
            System.out.println("Using port: " + port);
            //exit monitor is started and a new thread is made
            //exit monitor checks through user input in the server terminal for the keyword 'exit' and turns off server if that is found
            ExitMonitor exitMonitor = new ExitMonitor();
            new Thread(exitMonitor).start();
            //noinspection InfiniteLoopStatement
            while (true) {
                //the infinite while loop looks for new connections, accepts them and starts ConnectionHandler after adding the new connection to the connected array
                Socket client = server.accept();
                System.out.println("New client connected " + client);
                ConnectionHandler clientSocket = new ConnectionHandler(client);
                connected = Arrays.copyOf(connected, connected.length + 1);
                connected[connected.length - 1] = clientSocket;
                //threads handle each client separately
                new Thread(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Error occurred, exiting...");
        }
    }
}