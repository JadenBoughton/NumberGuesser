

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Stream;

/**
 * A class that runs the number guessing game.
 * The secret to winning is by guessing
 * according to the binary search algorithm
 * @author Jaden Boughton
 * @version 1.0
 * */

public class Main {

    /**
     * Generates random numbers for the NumberGuesser object.
     * The reason why it's global is to avoid generating the same sequence
     *     of random numbers for consecutive games.
     * */
    static Random numberGenerator = new Random();

    /**
     * Counts how many times a player has won during the program's runtime.
     * However, it does not save over different run times.
     * That will be for a later version.
     * */
    static int numberOfWins = 0;

    /**
     * Counts the total number of games played.
     * */
    static int totalGamesPlayed = 0;

    /**
     * This object encapsulates the data regarding the game
     *     such as the range of numbers that could hold the unknown number and
     *     the amount of guesses left.
     * */
    static NumberGuesser numberGuesser;

    /**
     * This object is the global declaration of the file so
     *     that it can be accessed anywhere in the program.
     * */
    static File player;

    /**
     * Makes the data loaded from the text file global.
     * */
    static ArrayList<String> playerData;

    /**
     * determines which features are available
     * true means the player can login, but can't print account data
     * false means the player can't login again, but can print account data
     * */
    static boolean menuChange = true;

    /**
     * Utilizes menu-driven design
     * Creates a text file, listing all possible players if one doesn't already exist
     *
     * */
    public static void main(String[] args) throws IOException {
        String nameOfPlayerListFile = "playerList.txt";
        File playerListFile = new File(nameOfPlayerListFile);
        playerListFile.createNewFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(playerListFile));

        ArrayList<String> playerList = loadArrayList(new ArrayList<>(), bufferedReader);


        String nameOfFile;

        boolean continueLooping = true;
        while(continueLooping) {
            if (menuChange) {
                System.out.println("Hi there! What would you like to do? " +
                                   "\npress 'A' to create account " +
                                   "\npress 'L' to login" +
                                   "\npress 'P' to play without saving your data " +
                                   "\npress 'E' to exit");
            } else {
                System.out.println("Hi there! What would you like to do? " +
                                   "\npress 'D' to display account " +
                                   "\npress 'P' to play " +
                                   "\npress 'O' to logout");
            }
            Scanner scnr = new Scanner(System.in);
            String action = scnr.next();
            if (menuChange && action.equals("A")) {
                System.out.println("Whats your name?");
                String name = scnr.next();
                nameOfFile = name + ".txt";

                player = new File(nameOfFile);
                FileWriter fileWriter = new FileWriter(player);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                playerData = new ArrayList<>();
                playerData.add(name);

                System.out.println("What's your password?");
                String password = scnr.next();
                playerData.add(password);
                playerData.add(String.valueOf(numberOfWins));
                playerData.add(String.valueOf(totalGamesPlayed));
                uploadData(playerData, bufferedWriter);

                playerList.add(nameOfFile);
                bufferedWriter.close();

                logoutProcedure(playerList, nameOfPlayerListFile);
                System.out.print("Now login to your account to play. ");

            } else if (menuChange && action.equals("L")) {

                boolean correctLoginInfo = false;
                while (!correctLoginInfo) {
                    System.out.println("What's your name?");
                    String name = scnr.next();
                    nameOfFile = name + ".txt";
                    System.out.println("What's your password");
                    String password = scnr.next();

                    if (playerExists(playerList, nameOfFile)) {
                        File file = new File(nameOfFile);
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader1 = new BufferedReader(fileReader);
                        playerData = loadArrayList(new ArrayList<>(),
                                bufferedReader1);
                        if (correctPassword(playerData, password)) {
                            player = new File(nameOfFile);
                            menuChange = false;
                            startGame();
                            correctLoginInfo = true;
                            playerData.set(2, numberOfWins + "");
                            playerData.set(3, totalGamesPlayed + "");
                            logoutProcedure(playerData, nameOfFile);
                            bufferedReader1.close();
                        } else {
                            System.out.println("Password incorrect. Please try again");
                        }

                    } else {
                        System.out.println("Name does not match. Please try again");
                    }

                }

            } else if (action.equals("P")) {

                startGame();
            } else if (menuChange && action.equals("E")) {
                continueLooping = false;
            } else if (!menuChange && action.equals("D")) {
                displayData();
            } else if (!menuChange && action.equals("O")) {
                playerData = null;
                menuChange = true;
            } else{
                System.out.println("Sorry. That's not one of the options!");
            }


        }

    }

    public static void displayData(){
        System.out.println("\nName: " + playerData.get(0));
        System.out.println("Total Games Played: " + playerData.get(3));
        System.out.println("Total Wins: " + playerData.get(2) + "\n");
    }

    public static void logoutProcedure(ArrayList<String> dataSave, String nameOfFile){
        try {
            File playerFile = new File(nameOfFile);
            FileWriter saveData = new FileWriter(playerFile, false);
            BufferedWriter bigDataSave = new BufferedWriter(saveData);
            uploadData(dataSave, bigDataSave);
            bigDataSave.close();
        }catch(IOException e){
            System.out.println("file does not exist");
        }
    }

    public static boolean correctPassword(ArrayList<String> playerArray, String password){
        return playerArray.get(1).equals(password);
    }

    public static boolean playerExists(ArrayList<String> playerList, String nameOfFile){
        for(String checker : playerList){
            if(Objects.equals(checker, nameOfFile)){
                return true;
            }
        }
        return false;
    }

    public static void uploadData(ArrayList<String> playerArray, BufferedWriter bufferedWriter){
        try {
            for (String playerDatum : playerArray) {
                bufferedWriter.write(playerDatum+"\n");
            }
        }catch(IOException e){
            System.out.println("uploadData has failed!");
        }
    }

    public static ArrayList<String> loadArrayList(ArrayList<String> playerArray, BufferedReader bufferedReader){
        try {
            String player = bufferedReader.readLine();
            while (player != null) {
                playerArray.add(player);
                player = bufferedReader.readLine();
            }
            if(playerArray.size() >= 2) numberOfWins = Integer.parseInt(playerArray.get(2));
            else numberOfWins = 0;
            if(playerArray.size() >= 3) totalGamesPlayed = Integer.parseInt(playerArray.get(3));
            else totalGamesPlayed = 0;
            return playerArray;
        }catch(IOException e){
            System.out.println("readLine failed!");
            return playerArray;
        }catch(Exception e){
            System.out.println("Only numbers");
            numberOfWins = 0;
            return playerArray;
        }
    }

    /**
     * This is a loop that determines whether the player want to play or not.
     * It allows consecutive games to be played and quits when the player types 'N'.
     * It calls the gameSetUp method when the player types 'Y' on the command prompt.
     * */
    public static void startGame(){
        System.out.println("Hi there! Want to guess a number?\npress 'Y' for yes and 'N' for no");
        while(true) {
            Scanner scnr = new Scanner(System.in);
            String action = scnr.next();
            if(action.equals("N")){
                break;
            }else if(action.equals("Y")){
                gameSetUp();
            }
            System.out.println("\nWant to try again?\npress 'Y' for yes and 'N' for no");
        }
    }

    /**
     * This class takes in the keyboard input for what a range numbers could be.
     * It also instantiates the NumberGuesser object with this data as well as
     *     the globally-declared random object.
     * Additionally, it allows for the player to change the number of guesses
     *     from the default, which is the log base 2 of the upper bound minus the lower one.
     * */
    public static void gameSetUp(){
        Scanner scnr = new Scanner(System.in);

        System.out.println("\nPlease type in the lower bound");
        int lowerBound = scnr.nextInt();

        System.out.println("\nPlease type in the upper bound");
        int upperBound = scnr.nextInt();

        while(upperBound <= lowerBound){
            System.out.println("Wrong! It needs to be higher than your lower bound! \nPlease type in the upper bound");
            upperBound = scnr.nextInt();
        }
        numberGuesser = new NumberGuesser(lowerBound, upperBound, numberGenerator);

        System.out.println("\nDo you need more guesses? \nIt would be quite an accomplishment if you said no and won!");
        System.out.println("type 'Y' for yes or any other character for no");
        String action = scnr.next();
        if(action.equals("Y")){
            System.out.println("How many?");
            int change = scnr.nextInt();
            numberGuesser.changeNumberOfGuesses(change);
        }
        System.out.println();
        playGame(numberGuesser);

    }

    /**
     * This class runs the sequence of the game
     *
     * @param numberGuesser the game info object
     * */
    public static void playGame(NumberGuesser numberGuesser){
        while(numberGuesser.getRemainingGuesses() > 0){
            System.out.println("You have " + numberGuesser.getRemainingGuesses() + " guesses. \n" +
                    "Your range is between " + numberGuesser.lowerBound + " and " + numberGuesser.upperBound +
                    "\nPlease input your guess below.");
            Scanner scnr = new Scanner(System.in);
            int guess = scnr.nextInt();
            if(guess > numberGuesser.getChosenNumber()){
                System.out.println("Little lower next time\n");
            }else if(guess < numberGuesser.getChosenNumber()){
                System.out.println("Needs to be higher\n");
            }else{
                numberOfWins++;
                totalGamesPlayed++;
                System.out.println("Congratulations! You won!");
                System.out.println("Win count: " + numberOfWins + "\n");
                return;
            }
            numberGuesser.decrementGuess();
        }
        totalGamesPlayed++;
        System.out.println("Sorry, but you have run out of guesses.\n");
        System.out.println("The correct number was " + numberGuesser.getChosenNumber());

    }

}