

import java.util.Random;
import java.util.Scanner;

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
     * This is the object that encapsulates the data regarding the game
     *     such as the range of numbers that could hold the unknown number and
     *     the amount of guesses left.
     * */
    static NumberGuesser numberGuesser;

    /**
     * This is a loop that determines whether the player want to play or not.
     * It allows consecutive games to be played and quits when the player types 'N'.
     * It calls the gameSetUp method when the player types 'Y' on the command prompt.
     * @param args command line arguments
     * */
    public static void main(String[] args) {
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
                System.out.println("Congratulations! You won!");
                System.out.println("Win count: " + numberOfWins + "\n");
                return;
            }
            numberGuesser.decrementGuess();
        }

        System.out.println("Sorry, but you have run out of guesses.\n");
        System.out.println("The correct number was " + numberGuesser.getChosenNumber());

    }

}