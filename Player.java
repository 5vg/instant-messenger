//imports random functionality
import java.util.Random;

public abstract class Player {
    //creates variables
    private Tile tile;
    private int x;
    private int y;

    //assigns tile to player upon creation
    public Player(Tile tile){
        this.tile = tile;
    }

    //getters and setters for tile, and x and y coordinates
    public Tile getTile(){
        return tile;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return y;
    }

    //move function which is the same for both players
    public void move(char dir){
        int[] newPos;
        newPos = DoDMap.playerMovement(x, y, dir, tile);
        setX(newPos[0]);
        setY(newPos[1]);
    }

    //place player function which is used at the beginning of a game to set initial position of both players
    public void placePlayer() {
        boolean placed = false;
        int maxY = DoDMap.getMapY();
        int maxX = DoDMap.getMapX();
        int chosenX = 0;
        int chosenY = 0;
        Random random = new Random();
        //tries to find a random X and Y coordinate so that it meets the criteria to have placed set to true
        while (!placed) {
            chosenX = random.nextInt(maxX);
            chosenY = random.nextInt(maxY);
            boolean onGold = DoDMap.checkPlayerOnGold(chosenX, chosenY);
            //check if on gold
            boolean inWall = DoDMap.checkPlayerInWall(chosenX, chosenY);
            //check if in wall
            if (!onGold && !inWall) {
                placed = true;
            }
        }
        //sets x and y and changed initial tile
        x = chosenX;
        y = chosenY;
        DoDMap.changeTile(x, y, tile);
    }
}
