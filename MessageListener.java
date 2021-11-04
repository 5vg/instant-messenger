import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//MessageListener is created with every chat client as a separate thread which listens for messages on the input
//stream and outputs them to the terminal
public class MessageListener implements Runnable{

    //creates initial variables
    private static Boolean exit = false;
    private final Socket clientSocket;
    private final String name;

    //initializer sets the name of the user and the socket of the client
    public MessageListener(Socket clientSocket, String name) {
        this.clientSocket = clientSocket;
        this.name = name;
    }

    //tag removal removes the user's tag from the console when a new message is received
    public void tagRemoval(){
        for(int i=0; i<name.length() + 9; i++){
            System.out.print("\b");
        }
    }

    //tag writer writes the user's tag to the console following a new message
    public void tagWriter(){
        System.out.print("(YOU)[" + name + "]: ");
    }

    //exit function
    public static void isExit(){
        exit = true;
    }

    @Override
    public void run() {
        try {
            //creates the InputStreamReader and the BufferedReader to take in messages
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String msg = "";
            while (!exit){
                //while the exit variable is false, loops infinitely
                //reads message and checks if null or a special command to close the bot
                //if either of those things, ignores it
                msg = in.readLine();
                if(msg != null && !msg.contains("//botClose")){
                    //if the message is to be shown, removes tag, shows message and adds tag back
                    tagRemoval();
                    System.out.println(msg);
                    tagWriter();
                }
            }
        } catch (IOException ignored) {
        }
    }

}
