import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//DoD client is initialised by DoDInit
public class DoDClient implements Runnable{

    //sets variables
    private final ConnectionHandler player;
    private final Socket socket;
    private boolean exit = false;

    //upon initialisation sets the player and the socket of the client
    public DoDClient(ConnectionHandler player, Socket socket) {
        this.player = player;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //creates output stream allowing the client to send messages
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String dodClientTag = "[DoDClient]: ";
            //welcomes user to DoD
            out.println(dodClientTag + "Welcome to DoD");
            out.println(dodClientTag + "This is a simplified version of DoD which only lets you use the small map");
            //creates DoD game
            Game game = new Game(socket);
            game.playGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
