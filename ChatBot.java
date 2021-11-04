//imported libraries

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

//chat bot is run on a separate thread
public class ChatBot implements Runnable{

    //variables store the bot's socket and a flag which stores the state of the bot
    private static Boolean exit = false;
    private final Socket botSocket;

    //upon creation, bot is given a socket and this is set
    public ChatBot(Socket socket){
        this.botSocket = socket;
    }

    //when botExit() is called, the flag 'exit' is set to true
    public static void botExit(){
        exit = true;
    }

    @Override
    //when the bot is initialised, this runs
    public void run() {
        //variables to be used for bot's functionality are set here
        BufferedReader in = null;
        String botTag = "[BOT]: ";
        String[] magicBallResponses = {"As I see it, yes.", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
        "Concentrate and ask again.", "Don’t count on it.", "It is certain.", "It is decidedly so.", "Most likely.",
        "My reply is no.", "My sources say no.", "Outlook not so good.", "Outlook good.", "Reply hazy, try again.",
        "Signs point to yes.", "Very doubtful.", "Without a doubt.", "Yes.", "Yes – definitely.", "You may rely on it."};
        String[] jokes = {"What's a vampire's least favourite food? Steak", "What's the difference between a ginger and a brick? A brick gets laid",
                "What did the the drummer call his twin daughters? Anna one, Anna two!", "What does a baby computer call his father? Data",
                "Why don’t they play poker in the jungle? Too many cheetahs!", "What time did the man go to the dentist? Tooth hurt-y!",
        "How does Moses make his tea? Hebrews it!", "You know what the loudest pet you can get is? A trumpet", "What did the buffalo say when his son left? Bison!",
        "I’m thinking about removing my spine. I feel like it’s only holding me back.", "I used to hate facial hair… … but then it grew on me.",
        "My wife is mad that I have no sense of direction. So I packed up, and right.", "How do you make Holy Water? You boil the hell out of it.",
        "I bought some shoes from a drug dealer. I don't know what he laced them with, but I was tripping all day.",
        "The secret service isn't allowed to yell 'Get Down' anymore when the President is about to be attacked. Now they have to yell 'Donald Duck'",
        "If you see a robbery at an Apple store, does that make you an iWitness?", "Why did the invisible man turn down the job offer? He couldn't see himself doing it",
        "What has two butts and kills people? An Assassin"};
        Random rand = new Random();
        //the following is what the bot uses to run
        try {
            //input and output are set for the bot and connected to the streams
            in = new BufferedReader(new InputStreamReader(botSocket.getInputStream()));
            PrintWriter out = new PrintWriter(botSocket.getOutputStream(), true);
            //bot introduces itself when started
            out.println(botTag + "Hi, I'm the bot around here. If you don't know how to use me, type /help to see what I can do");
            String msg = "";
            while (!exit){
                //bot scans every line coming in to check for bot commands
                msg = in.readLine();
                //the help command explains what the bot can do
                if(msg.contains("/help")){
                    out.println(botTag + "I can currently do 3 things: ");
                    out.println(botTag + "You can type /8ball followed by a question to get an answer from the magic 8 ball");
                    out.println(botTag + "You can type /joke to hear a joke");
                    out.println(botTag + "Or, you can type /date to get the current date and time");
                    //the 8 ball command generates an 8ball response to a question
                    //to do this, it picks a random number and then uses the responses to output a response to the chat
                }else if(msg.contains("/8ball")){
                    out.println(botTag + magicBallResponses[rand.nextInt(20)]);
                    //similarly to 8ball, the bot tells a joke
                }else if(msg.contains("/joke")){
                    out.println(botTag + jokes[rand.nextInt(17)]);
                    //the bot can also send the current date and time into the chat
                }else if(msg.contains("/date")){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    out.println(botTag + "The current date and time is: " + formatter.format(date));
                }
            }
            //error handling
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
