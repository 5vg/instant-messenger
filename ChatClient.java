import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    //default host and port variables are set
    private static String host = "localhost";
    private static int port = 14001;

    //main function starts the client
    public static void main(String[] args) {
        //for loop checks arguments and then sets host and port accordingly
        try{
            for(int i=0; i<args.length; i++){
                if(args[i].equals("-ccp")){
                    port = Integer.parseInt(args[i+1]);
                }else if(args[i].equals("-cca")){
                    host = args[i+1];
                }
            }
            //if things were entered incorrectly, attempts to start with default settings
        }catch(Exception e){
            System.out.println("Port/IP passed incorrectly. Attempting to use default settings...");
        }

        try {
            //opens scanner and allows user to enter the name they will use in the server
            Scanner scanner = new Scanner(System.in);
            String line = "";
            System.out.print("Please enter a name to use in the server: ");
            String name = scanner.nextLine();
            //once name is set, attempts to connect to server
            System.out.println("Attempting to connect to " + host + ":" + port);
            Socket socket = new Socket(host, port);
            //once connected, informs user
            System.out.println("Connected successfully. You can now send and receive messages.");
            //creates PrintWriter to allow for output to stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //creates message listener to allow for simultaneous message receiving and sending (multi-threaded client solution)
            MessageListener messageListener = new MessageListener(socket, name);
            new Thread(messageListener).start();
            //creates booleans to be used to keep track if DoD or bot are active on the server
            boolean botExist = false;
            boolean botJustMade = false;
            boolean dodExist = false;
            boolean dodJustMade = false;
            while (true) {
                //botJustMade and dodJustMade are solutions to the user tag being sent twice right after initialising bot or DoD
                if(botJustMade){
                    botJustMade = false;
                }else if(dodJustMade){
                    dodJustMade = false;
                }else{
                    //outputs user tag
                    System.out.print("(YOU)[" + name + "]: ");
                }

                //saves user input as String line
                line = scanner.nextLine();
                //checks input for commands such as /exit, /botactivate, etc.
                // /exit disconnects the client and closes the bot
                if(line.equals("/exit")){
                    if(botExist){
                        ChatBotInit.isExit();
                        out.println("//botClose");
                    }
                    break;
                    // /botactivate starts the bot and sets flags accordingly
                }else if(line.equalsIgnoreCase("/botactivate")){
                    if(!botExist){
                        ChatBotInit.startBot(host, port);
                        botExist = true;
                        botJustMade = true;
                    }
                    // /botkick removes bot from the server and sets flags accordingly
                }else if(line.equalsIgnoreCase("/botkick")){
                    if(botExist){
                        ChatBotInit.isExit();
                        out.println("//botClose");
                        botExist = false;
                    }

                    // /playdod starts DoD. This is very very unfinished but a user can play it to completion
                }else if(line.equalsIgnoreCase("/playdod")){
                    if(!dodExist){
                        DoDInit.startDoD(socket, host, port);
                        dodExist = true;
                        dodJustMade = true;
                    }
                }
                //if no special commands were sent, the message is outputted
                out.println("[" + name + "]: " + line);
            }
            //when the while loop breaks (due to the user entering /exit) everything is closed and turned off
            scanner.close();
            out.close();
            socket.close();
            MessageListener.isExit();
            //error detection
        } catch (IOException e) {
            System.out.println("An error has been detected, exiting...");
        }
    }
}
