import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//human player is a subclass of player
public class HumanPlayer extends Player{

    private static Socket socket;
    private static PrintWriter out;

    //sets tile
    public HumanPlayer(Tile tile, Socket inSocket) throws IOException {
        super(tile);
        socket = inSocket;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    }

    //calls Map.humanQuit() when user types in QUIT
    public void humanExit(){
        DoDMap.humanQuit();
        //exit
    }

    //calls Map.pickup when user types in PICKUP
    public void humanPickup(){
        DoDMap.pickup();
        //gold pickup
    }

    //calls Map.getGoldToWin(), gets a number and tells the player how much gold they need to win on the map.
    //is called when user types in HELLO
    public void humanHello(){
        int gold = DoDMap.getGoldToWin();
        out.println("Gold to win: "+ gold);
    }

    //calls Map.getGold to get the number of gold the user currently has. Then outputs that number
    public void humanGold(){
        int gold = DoDMap.getGold();
        out.println("Gold owned: " + gold);
    }

    //calls Map.humanLook() with coordinates of user to allow the user to see their part of the map
    public void humanLook(){
        DoDMap.humanLook(getX(), getY());
    }

}
