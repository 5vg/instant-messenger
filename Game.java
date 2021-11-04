import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Game {
    //creates a map reference
    private DoDMap map;
    private static Socket socket;

    //initializer function
    public Game(Socket socketIn) throws IOException {
        //creates new strings
        socket = socketIn;
        String thisMap = null;
        //this stores the file path of the map
        thisMap = "src/small_map.txt";
        //creates new map and passes in the filepath to the map selected
        map = new DoDMap(thisMap, socket);
    }

    //is called by main after class Game is initialized
    public void playGame() throws IOException {
        //creates variables
        String userInput;
        int turnCounter=0;
        int botCounter=0;



        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //creates new human player and places them in a random location
        HumanPlayer human = new HumanPlayer(new Tile('P'), socket);
        human.placePlayer();

        //creates new bot player and places them in a random location
        BotPlayer bot = new BotPlayer(new Tile('B'));
        bot.placePlayer();
        //makes sure that the starting location of the bot player and human player aren't the same
        while (bot.getX() == human.getX() && bot.getY() == human.getY()) {
            bot.placePlayer();
        }
        //creates a new scanner to allow for user input
        Scanner input = new Scanner(System.in);
        //notifies the user that the game has begun
        out.println("You can now begin entering operations");
        //while(true) loop allows for constant input
        while (true) {
            //checks if bot wins at the start of every turn
            map.checkBotWin();
            //turn counter keeps track of whose turn it is
            turnCounter++;
            //turn selected by taking remainder of a division by 2
            //user always goes first
            if(turnCounter%2 == 1){
                //takes input and puts it into an array which is split by whitespaces
                userInput = in.readLine();
                String[] userArr;
                userArr = userInput.split(" ");
                //checks what the user ahs entered and runs the appropriate function from Map
                try{
                    if (userArr[1].equalsIgnoreCase("HELLO")) {
                        human.humanHello();
                    }else if(userArr[1].equalsIgnoreCase("GOLD")){
                        human.humanGold();
                    }else if(userArr[1].equalsIgnoreCase("MOVE")){
                        //makes sure there was a direction entered after MOVE
                        try{
                            human.move(userArr[2].charAt(0));
                        }catch(Exception e){
                            System.out.println("FAIL.");
                        }
                    }else if(userArr[1].equalsIgnoreCase("PICKUP")){
                        human.humanPickup();
                    }else if(userArr[1].equalsIgnoreCase("LOOK")){
                        human.humanLook();
                    }else if(userArr[1].equalsIgnoreCase("QUIT")){
                        human.humanExit();
                        // if action not found, prints FAIL.
                    }else{
                        out.println("FAIL.");
                    }
                }catch(Exception e){
                    out.println("FAIL.");
                }

                //bot's turn
            }else{
                //bot counter keeps track of which action the bot is to perform
                botCounter++;
                if(botCounter%2 == 1){
                    //every other turn bot moves
                    bot.botLook();
                }else{
                    //and every other turn bot looks
                    bot.botMove();
                }

            }

        }
    }
}
