//importing libraries
import java.io.IOException;
import java.net.Socket;

//chat bot initializer initialises the chat bot
public class ChatBotInit {

    //boolean flag checks if bot is on or not
    private static Boolean botOn = false;

    //when this function is called, it shuts down the bot and sets all flags accordingly
    public static void isExit(){
        ChatBot.botExit();
        botOn = false;
    }

    //this function runs the bot. it takes in host and port to create a socket
    private static void botRun(String host, int port) {
        try{
            Socket socket = new Socket(host, port);
            ChatBot chatBot = new ChatBot(socket);
            new Thread(chatBot).start();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this function is called to start the bot. bot then checks if an instance of itself already exists on the server. if it does, it doesn't start
    public static void startBot(String host, int port){
        if(!botOn){
            botOn = true;
            botRun(host, port);
        }
    }
}
