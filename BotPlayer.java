//imports random functionality
import java.util.Random;

//creates class BotPlayer that is a subclass of a superclass called 'Player'
public class BotPlayer extends Player{
    //creates  variables to store coordinates
    int playerX;
    int playerY;
    int botX;
    int botY;

    //creation of BotPlayer - sets tile
    public BotPlayer(Tile tile) {
        super(tile);
    }

    //function which the bot calls every other turn - allows it to look
    public void botLook(){
        //creates variables and an integer array
        int[] returnedInfo;
        botX = getX();
        botY = getY();
        //calls function of class map - botLook passing it the X and Y coordinates of the bot's current position
        //the function returns the player's coordinates if the player is within 2 adjacent tiles of the bot
        //if the player wasn't found - returns 0, 0 (which are impossible coordinates for the player to be in)
        returnedInfo = DoDMap.botLook(botX, botY);
        //stores the player's coordinates
        playerX = returnedInfo[0];
        playerY = returnedInfo[1];
    }

    public void botMove(){
        //creates new array and a random initializer
        Random random = new Random();
        char[] movement = {'N', 'E', 'S', 'W'};
        //checks if the player was seen
        if(playerX == 0 && playerY == 0){
            //if not, does a random move
            char dir = movement[random.nextInt(3)];
            move(dir);
            //if yes, finds out where the bot is in relation to the player and attempts to get closer to them
        }else if(playerX<botX){
            move('W');
        }else if(playerX>botX){
            move('E');
        }else if(playerY<botY){
            move('N');
        }else{
            move('S');
        }

    }
}
