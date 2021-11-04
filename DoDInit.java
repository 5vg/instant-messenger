import java.io.IOException;
import java.net.Socket;

//incomplete class - DoD initializer
public class DoDInit {

    //initial variables set
    private static ConnectionHandler whoIsPlaying;
    private static Boolean dodExists = false;

    //getter for dodExists
    public static Boolean getDodExists(){
        return dodExists;
    }

    //dodEnd function
    public static void dodEnd(){
        dodExists = false;
        whoIsPlaying = null;
    }

    //dodRun starts a new socket and creates a new DoDClient thread
    public static void dodRun(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        DoDClient doDClient = new DoDClient(whoIsPlaying, socket);
        new Thread(doDClient).start();
    }

    //startDoD checks if DoD is already used in the server and only creates a new one if there is none
    public static void startDoD(Socket socket, String host, int port) throws IOException {
        if(!dodExists){
            dodExists = true;
            dodRun(host, port);
        }
    }



}
