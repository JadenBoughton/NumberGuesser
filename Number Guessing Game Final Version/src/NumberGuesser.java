

import java.util.Random;

public class NumberGuesser {

    /**
     * The lowest possible number the player can guess correctly
     * */
    public int lowerBound;

    /**
     * The highest possible number the player can guess correctly
     * */
    public int upperBound;

    /**
     * The random integer within the bounds that the player has to guess
     * */
    private final int chosenNumber;

    /**
     * The amount of guesses the player has
     * */
    private int numberOfGuesses;

    /**
     * Assigns the keyboard input for the upper and lower bounds as well as
     *     generating a random integer between the range and the default guess amount.
     *
     * Also, if the guess number is 0, then it's changed to 1.
     * @param lowerBound smallest number that the player could guess correctly
     * @param upperBound largest number that the player could guess correctly
     * @param numberGenerator the random object is kept for the entire runtime to maintain consistency
     * */
    public NumberGuesser(int lowerBound, int upperBound, Random numberGenerator){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.numberOfGuesses = (int) Math.ceil(Math.log(upperBound - lowerBound)/Math.log(2));
        this.chosenNumber = numberGenerator.nextInt((upperBound - lowerBound) + 1) + lowerBound;

        if(this.numberOfGuesses == 0){this.numberOfGuesses = 1;}
    }

    /**
     * @return the remaining guesses
     * */
    public int getRemainingGuesses(){
        return numberOfGuesses;
    }

    /**
     * Decreases the guesses by one each turn
     * */
    public void decrementGuess(){
        numberOfGuesses--;
    }

    /**
     * Changes the number of guesses
     * */
    public void changeNumberOfGuesses(int newAmount){
        numberOfGuesses = newAmount;
    }

    /**
     * @return the random number for comparison purposes
     * */
    public int getChosenNumber(){
        return chosenNumber;
    }

}
