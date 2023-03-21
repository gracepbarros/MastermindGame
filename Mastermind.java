/*
Name: Grace K. P. Barros            Student Number: 100385031
Course/Section: CPSC 1150-6
Instructor: Bryan Green             Date:           November 29 - 2022
Assignment/Lab: Assignment#5        Last Modified:  December 2 - 2022 11:00pm

Purpose:  Creation of a version of Mastermind game - with numbers!
*/

import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Mastermind {
    /** A variable to see the answer before the guesses!
     * If is assigned as true, it will always print the answer before ask for a guess.
     */
    static final Boolean CHEAT_MODE = true;

    static Scanner input = new Scanner(System.in);

    /**Main method that calls all others methods
     *It prints identification, ask for the size and initialize the game.
     * @param args pattern taught in class.
     */
    public static void main (String[] args) {
        printIdentification();
        //Call a method that guarantee the input from user is either an integer and inside range (which is [2,10])
        int size = askIntInsideRange("how many numbers you wanna guess",2,10);
        int[] secret = generateSecret(size); //generate the "secret"
        play(secret); //keep asking if user wants to continue playing or not


    } //Main method that calls all others methods

    /**
     Print student identification.
     It includes assignment/lab name, student name, course, section, and student number.
     */
    private static void printIdentification()
    {
        System.out.println("### Assignment 5: Mastermind   Author: Grace Karine P. Barros  ###");
        System.out.println("### Course/Section - CPSC1150-6 ###");
        System.out.println("### St.number - 100385031 ###\n");
    } // printIdentification

    /**
     Ask for an integer inside range and return it or ask again until it is provided.
     It combines the askIntForUser and verifyRange methods, so it is possible
     to ask for an integer inside range using only one method.
     It keeps asking input from user while user do not provide an integer inside range,
     always validating if it is an integer and if it is inside range limits.
     @param what name of what is being asked to be used at question (for user clarification).
     @param bottomLimit minimum number allowed inside range.
     @param topLimit maximum number allowed inside range.
     @return first integer in range that users provides.
     */
    private static int askIntInsideRange(String what,int bottomLimit, int topLimit){
        int answer;

        do{
            System.out.printf("Please enter the %s, which is an integer in the range [%d, %d]:  %n",what, bottomLimit, topLimit );
            while (!input.hasNextInt()){

                System.out.printf("Sorry that is not an integer, please enter the %s.", what);
                input.nextLine();
            }
            answer = input.nextInt();

        }while( answer > topLimit || answer < bottomLimit);

        return answer;
    }
    /** Generates a random digit.
     * Digits can be from 0 to 9.
     * @return the random int number.
     */
    private static int randomDigit(){
        return (int) (Math.random() *10);
    }

    /** Generates an array of distinct integers.
     *
     * @param size how long the array will be. Must be inside range [2,10].
     * @return the array of distinct integers.
     */
    private static int[] generateSecret (int size){
        int[] secret = new int[size]; //Creating array (filled of zeros)
        int index = 0; //starting index for fill it with random numbers
        while(index<size){
            int number = randomDigit();
            if(search(secret,number,index) == -1) {
                secret[index] = number;
                index++; //only increases index inside if to make sure digits are distinct
            }
        }
        return secret;
    }

    /**For each index in the portion of the array between index 0 and end. Check if there is element equal the target.
     * Search through array until the element is either found or not.
     * If is found return the index of occurrence.
     * If is not return the sentinel -1.
     * @param list array where target will be looked for.
     * @param target which element it is being looked for
     * @param end until when search will go for
     * @return The index of first element found.
     */
    private static int search(int[] list, int target, int end){
        int notFound = -1;
        for(int index = 0;index<end;index++){
            if(list[index] == target) { //if founds the target returns it
                return index;
            }
        }
        return notFound; //just arrive here after go for all array and not find target. Then return -1.
    }

    /**Ask for a sequence of determined size.
     * Keep asking for sequences until size is satisfactory.
     * @param size the size it is necessary.
     * @return first correct size sequence that user type.
     */
    private static String askForSequence(int size){
        String seq;
        do {
            System.out.printf("Please enter the %d digits secret:%n", size);
            seq = input.next();
        } while (seq.length() != size); //ask for another input if it is not the same size
        return seq;
    }

    /** Compares a user answer with secret and prints out the analysis.
     * It prints out how many digits are in the right place and how many are right, but in the wrong place.
     * It returns true if guess is a match with secret.
     * @param secret int[] in the sequence that needed to be guessed.
     * @return true if right positioned elements are the same size of array. Which means guess is correct.
     *
     */
    private static boolean guessTheSecret(int[] secret) {
        final int LEN = secret.length;
        String guess = askForSequence(LEN);

        int rightPosition = 0; //How many numbers are in the right position
        int rightNumber = 0; //How many numbers are there, but not in the right position


        if(!isDistinct(guess)) {
            System.out.println("Answer invalid! Sequence must be distinct numbers.");
            return false;
        }

        for(int index = 0;index<LEN;index++){
            int number = Integer.parseInt(guess.substring(index,index+1)); //getting the number of user guess
            int position = search(secret, number,LEN); //getting the position of that number of correct answer
            if ( position == index){
                 rightPosition += 1; //if number is found in the same position index is: it is a match!
            }
            else if( position != -1){
                rightNumber += 1; //if number is found, but position doesn't match, it is important as well.
            }
        }
        System.out.printf("There are %d numbers in the right position%n",rightPosition);
        System.out.printf("There are %d numbers right but in the wrong position%n",rightNumber);

        return rightPosition==LEN; //return true if right positioned elements are the same size of array. Which means guess is correct.
    }

    /** Verifies if chars in a string are distinct and all numbers.
     * @param sequence that will be verified.
     * @return boolean saying weather is distinct and all numbers or not.
     */
    private static boolean isDistinct(String sequence){
        for(int index = 0;index<sequence.length();index++) {
            char number = sequence.charAt(index); //grabs only one char - starting by the first
            if( Character.isDigit(number) ) {
                for(int sub = index+1;sub<sequence.length();sub++){
                    if(sequence.charAt(sub) == number) //looks if this char is among all others chars of sequence.
                        return false;
                }
            }
            else
                return false;
        }
        return true;//just arrive here after go for sequence and not find same digits or not-numbers. So sequence is distinct and all numbers.
    }

    /** Plays mastermind with user until it input STOP key or have too many attempts (limit is 12).
     * If global variable CHEAT_MODE is true, prints answer before attempts.
     * Keep asking for inputs while necessary.
     * It provides two tips! Sum of digits if attempt more than 4.
     * Two first digits if attempts more than 9 (and secret more than 6 digits long!).
     * Prints the result (winner, give up or fail) and the answer at the end.
     * @param secret int[] array with random digits that need to be guessed.
     */
    private static void play(int[] secret){
        char option = '\0';
        int attempts = 0;
        if(CHEAT_MODE){
            System.out.println("Cheat mode ON!!");
            printSequence(secret);
        }
        do{
            boolean winner = guessTheSecret(secret);
            attempts++;
            if(winner){
                System.out.printf("Congrats! You won with %d attempts%n",attempts);
                break;
            }
            if(attempts >= 5){
                System.out.printf("There goes a tip: sum of the %d digits is %d%n",secret.length,listSum(secret));
            }
            if(attempts >=10 && secret.length>6){
                System.out.printf("There goes another tip: first two digits are %d%d%n",secret[0],secret[1]);
            }
            if(attempts==12){
                System.out.println("Limit attempts reached! You loose!");
                break;
            }
            System.out.println("Press 'S' if you give up and want to stop\nPress any other key if you want to Continue guessing");
            option = toUpperCase((input.next()).charAt(0));

        }while(option != 'S');

        if(option == 'S')
            System.out.printf("You give up after %d attempts...%n",attempts);

        System.out.print("Correct sequence:  ");
        printSequence(secret);
    }

    /** Print elements of an array in the same line, and separated by comma.
     * @param sequence int[] array that will be nicely printed
     */
    private static void printSequence(int[] sequence){
        for(int index=0;index<sequence.length-1;index++){
            System.out.print(sequence[index]+ ", ");
        }
        System.out.println(sequence[sequence.length-1]);
    }

    /**Sum all elements of an int list and return it.
     * @param list list with int elements
     * @return the sum of all elements on it.
     */
    private static int listSum(int[] list){
        int sum = 0;
        for(int index=0;index<list.length;index++){
            sum += list[index];
        }
        return sum;
    }
}
