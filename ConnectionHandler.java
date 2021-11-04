import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

//ConnectionHandler uses Runnable for multi-threading
public class ConnectionHandler implements Runnable {

    //creates initial variables
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Boolean exception = false;

    private final Socket clientSocket;

    //upon initialisation, sets socket
    public ConnectionHandler(Socket socket) {
        this.clientSocket = socket;
    }

    //getter for socket
    public Socket getClientSocket() {
        return clientSocket;
    }

    //receivedMessage function loops through connected array from ChatServer and outputs every new message to each client
    private void receivedMessage(String msg){
        ConnectionHandler[] clientList = ChatServer.getConnected();
        for(int i = 1; i < clientList.length; i++){
            if(clientList[i].getClientSocket() != clientSocket){
                clientList[i].sendMessage(msg);
            }
        }
    }

    //sendMessage function sends the actual message to the client allowing it to be seen there
    public void sendMessage(String msg) {
        out.println(msg);
    }


    @Override
    public void run() {
        try {
            //output and input of the ConnectionHandler is set
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(isr);
            String line;
            //reads message from input stream and makes sure it's not null
            while((line = in.readLine()) != null){
                //sends the message via receivedMessage
                receivedMessage(line);
            }
            //exception happens when the client disconnects
        } catch (IOException e) {
            exception = true;
            System.out.println("Client with port " + clientSocket.getPort() + " has disconnected");
        } finally {
            try {
                //if out and in exist, closes them
                if (out != null) {
                    out.close();
                }
                if (in != null)
                    in.close();
                //if there hasn't been an exception, notify server of disconnection
                if(!exception){
                    System.out.println("Client with port " + clientSocket.getPort() + " has disconnected");
                }
                //removes self from the connected list in ChatServer and updates it
                ConnectionHandler[] clientList = ChatServer.getConnected();
                ConnectionHandler[] tempClientList = {null};
                for(int i=1; i<clientList.length; i++){
                    if(clientList[i].getClientSocket() != clientSocket){
                        tempClientList = Arrays.copyOf(tempClientList, tempClientList.length + 1);
                        tempClientList[tempClientList.length - 1] = clientList[i];
                    }
                }
                ChatServer.connectedUpdate(tempClientList);
                //closes socket
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}