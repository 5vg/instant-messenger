import java.io.IOException;
import java.util.Scanner;

//exit monitor is started by ChatServer as a separate thread to monitor terminal inputs and close the server if 'exit'
// is seen
public class ExitMonitor implements Runnable{
    @Override
    public void run() {
        //starts a new scanner
        Scanner scanner = new Scanner(System.in);
        while(true){
            //checks for keyword constantly
            if(scanner.nextLine().equalsIgnoreCase("exit")){
                try {
                    //calls ChatServer.isExit to close down server cleanly
                    ChatServer.isExit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
