import java.io.*;
import java.net.Socket;

public class DoDMap {
    //imports things to be used in code
        //creates variables to be used in code. All are static as Map is created once and only once during runtime
        private static Tile[][] map;
        private static int goldToWin;
        private static int playerGold = 0;
        private static String name;
        private static int y = 0;
        private static int x = 0;
        private static char botOnTile;
        private static char playerOnTile;
        private static PrintWriter out;

        //creates map
        public DoDMap(String thisMap, Socket socket) throws IOException {
            //creates variables to be used during map creation
            out = new PrintWriter(socket.getOutputStream(), true);
            String currentLine;
            String lineArr[];
            int stringLen;
            int counter = 0;
            //reads map name and number of gold required to win and stores them
            try {
                BufferedReader br = new BufferedReader(new FileReader(thisMap));
                while (counter<2) {
                    currentLine = br.readLine();
                    lineArr = currentLine.split(" ");
                    stringLen = lineArr.length;
                    counter++;
                    if(counter == 1){
                        name = "";
                        for(int i=1; i<stringLen; i++) {
                            name += lineArr[i] + " ";
                        }
                        out.print(name + "\n");
                    }else if (counter == 2) {
                        goldToWin = Integer.parseInt(lineArr[1]);
                    }
                }
                //resets counter
                counter = 0;
                //while loop until it reaches last line
                while((currentLine = br.readLine()) != null){
                    if(counter == 0){
                        //splits individual characters
                        lineArr = currentLine.split("");
                        stringLen = lineArr.length;
                        //horizontal size of map is set here
                        x = stringLen;
                    }
                    //counter counts up
                    counter++;
                }
                //y is the largest number the counter reached and is therefore the vertical size of the map
                y = counter;
                //closes the reader
                br.close();
                //creates a new map of tiles with 2 extra on both horizontal and vertical.
                //this allows for wall padding - to allow the LOOK function to be exactly 5x5 even if
                //the user is near the edge of the map
                map = new Tile[y+2][x+2];
                //creates a second buffered reader which will help populate the currently empty map
                BufferedReader br2 = new BufferedReader(new FileReader(thisMap));
                //first the map is filled with purely walls
                for(int i=0; i<map.length; i++){
                    for(int j=0; j<map[i].length; j++){
                        map[i][j] = new Tile('#');
                    }
                }
                //the reader skips the first two lines which have already been stored
                for(int i=0; i<2; i++){
                    br2.readLine();
                }
                //reads the line, separates the line character by character and changes the tile to represent that
                //specific tile on the map
                for(int i=0; i<y; i++){
                    currentLine = br2.readLine();
                    lineArr = currentLine.split("");
                    for(int j=0; j<x; j++){
                        map[i+1][j+1] = new Tile(lineArr[j].charAt(0));
                    }
                }
                //closes reader
                br2.close();
                //catches for different errors such as if the file doesn't exist
            }catch (FileNotFoundException e){
                System.err.println("This file doesn't exist. Exiting...");
                System.exit(0);
            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                System.err.println("The program encountered an error: " + e);
                System.err.println("Exiting...");
                System.exit(0);
            }
        }

        //getter functions
        public static String getName(){
            return name;
        }

        public static int getGold(){
            return playerGold;
        }

        public static int getGoldToWin(){
            return goldToWin;
        }

        public static int getMapY(){
            return y+2;
        }

        public static int getMapX(){
            return x+2;
        }

        //checking functions which check if a tile is present at certain coordinates
        public static boolean checkPlayerOnGold(int x, int y) {
            return map[y][x].getTile() == 'G';
            //check if player is on gold tile
        }

        public static boolean checkPlayerInWall(int x, int y){
            return map[y][x].getTile() == '#';
            //check if player in wall
        }

        public static boolean checkPlayerOnExit(int x, int y) {
            return map[y][x].getTile() == 'E';
        }

        //function which changes the first tiles to those of the user and the bot
        public static void changeTile(int x, int y,Tile tile){
            if(tile.getTile() == 'P'){
                playerOnTile = map[y][x].getTile();
            }else if(tile.getTile() == 'B'){
                botOnTile = map[y][x].getTile();
            }
            map[y][x] = tile;
        }

        //look function for user - done in the map class as it allows for instant access to the map's tiles
        public static void humanLook(int x, int y){
            //creates a string and prints it
            String s = "";
            //nested foir loop with ranges to make the output 5x5
            for(int i=y-2; i<y+3; i++){
                for(int j=x-2; j<x+3; j++){
                    s+= map[i][j].getTile();
                }
                s += "\n";
            }
            out.println(s);
        }

        //checks if the bot has won by checking if either the player or the bot is on each other's tile
        public void checkBotWin(){
            if(botOnTile == 'P' || playerOnTile == 'B'){
                out.println("LOSE");
                System.exit(0);
            }
        }

        //player movement - takes in the coordinates, direction and tile type of player
        public static int[] playerMovement(int x, int y, char dir, Tile tile) {
            //creates variables
            int[] returning;
            boolean possible = false;
            int nextY = y;
            int nextX = x;
            //checks if direction would end up putting the player into a wall, if so - considered impossible
            if (dir == 'N') {
                possible = !checkPlayerInWall(x, y - 1);
                nextY = y - 1;
            } else if (dir == 'E') {
                possible = !checkPlayerInWall(x + 1, y);
                nextX = x + 1;
            } else if (dir == 'S') {
                possible = !checkPlayerInWall(x, y + 1);
                nextY = y + 1;
            } else if (dir == 'W') {
                possible = !checkPlayerInWall(x - 1, y);
                nextX = x - 1;
            }
            // if the move is possible, performs switching of tiles on the map
            if (possible) {
                if (tile.getTile() == 'P') {
                    map[y][x] = new Tile(playerOnTile);
                    playerOnTile = map[nextY][nextX].getTile();
                    map[nextY][nextX] = tile;
                    returning = new int[]{nextX, nextY};
                    return returning;
                } else if (tile.getTile() == 'B') {
                    map[y][x] = new Tile(botOnTile);
                    botOnTile = map[nextY][nextX].getTile();
                    map[nextY][nextX] = tile;
                    //possible move
                    returning = new int[]{nextX, nextY};
                    return returning;
                }
                //if the move isn't possible - prints 'FAIL.' (if human user) and returns same coordinates as the player had
                //at the beginning
            }else {
                if(tile.getTile() == 'P'){
                    out.println("FAIL.");
                }
                returning = new int[]{x, y};
                return returning;
            }
            returning = new int[]{x, y};
            return returning;
        }

        //pickup function - checks if the player is on the G tile and acts accordingly
        public static void pickup(){
            boolean possible =  playerOnTile == 'G';
            if(possible){
                playerGold++;
                out.println("SUCCESS. Gold Owned: "+ playerGold);
                //resets tile to nothing after gold picked up
                playerOnTile = '.';
            }else{
                out.println("FAIL. Gold Owned: "+ playerGold);
            }
        }

        //QUIT function - checks if user meets condition to win, if yes - prints congratulations and exits program
        public static void humanQuit(){
            if(playerOnTile == 'E' && playerGold >= goldToWin){
                out.println("WIN");
                out.println("Congratulations! You have beaten " + name);
                //if not - prints lose and exists program
            }else{
                out.println("LOSE");
            }
            System.exit(0);
        }

        //the bot's look command
        public static int[] botLook(int x, int y){
            int[] returning;
            returning = new int[]{0, 0};
            boolean found = false;
            //different from the human look command as it doesn't print and searches for a specific tile (P).
            // if found -saves coordinates and returns. if not found - returns 0, 0
            String s = "";
            for(int i=y-2; i<y+3; i++){
                for(int j=x-2; j<x+3; j++){
                    if(map[i][j].getTile() == 'P'){
                        returning = new int[]{j, i};
                        found = true;
                    }
                    s+= map[i][j].getTile();
                }
                s += "\n";
            }
            return returning;
        }

    }
