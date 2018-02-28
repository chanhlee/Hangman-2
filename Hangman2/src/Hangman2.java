/*
* Hangman2.java
* Author: [Chanhee Lee]
* Submission Date: [10/16/16]
*
* Purpose: To allow the user to play a game of Hangman 2.0 (maximum 20 games, should they choose to continue to play) 
* whereby the rules follow the same rules of traditional Hangman but the users have to specify what spaces they 
* want to check their guesses on. The number of spaces will vary depending on the difficulty
*
* Statement of Academic Honesty:
*
* The following code represents my own work. I have neither
* received nor given inappropriate assistance. I have not copied
* or modified code from any source other than the course webpage
* or the course textbook. I recognize that any unauthorized
* assistance or plagiarism will be handled in accordance with
* the University of Georgia's Academic Honesty Policy and the
* policies of this course. I recognize that my work is based
* on an assignment created by the Department of Computer
* Science at the University of Georgia. Any publishing
* or posting of source code for this project is strictly
* prohibited unless you have written consent from the Department
* of Computer Science at the University of Georgia.
*/
import java.util.Scanner;

public class Hangman2 {
	private static final boolean testingMode = true; 
	
	public static void main(String[] args) {
		//Variables and Scanner
		Scanner keyboard = new Scanner(System.in);
		Scanner keyboard1 = new Scanner(System.in);
		char guessChar, difficulty; 
		int wordLength, guessRemaining, j = 0;
		double requiredNumOfSpaceGuess;
		String difficultyStr, secretWord, word, guessSpaces, guessStr; 
		boolean play = true; 
		
		//Part 1: Get a Difficulty Level
		while ((j < 20) && (play == true)){		//Only executes game when they have played less than 20 times and the user has indicated that they want to play again
			while (true){						//j is number of days they can play 
				System.out.println("Enter your difficulty: Easy (e), Intermediate (i), or Hard (h)");
				difficultyStr = keyboard.next();
				difficulty = difficultyStr.charAt(0);
				if ((difficulty != 'e') && (difficulty != 'i') && (difficulty != 'h')){
					System.out.println("Invalid Difficulty. Try Again...");	//Prints error if difficulty is not one of the options
				}	
				else {
					break;						//Breaks if input is one of the difficulty options
				}
			}
			//Part 1.5: Setting Variables for Difficulty 
			if (difficulty == 'e' || difficulty == 'E') {
				guessRemaining = 15;
				requiredNumOfSpaceGuess = 4.0;
			}
			else if (difficulty == 'i' || difficulty == 'I'){
				guessRemaining = 12;
				requiredNumOfSpaceGuess = 3.0;
			}
			else if (difficulty == 'h' || difficulty == 'H'){
				guessRemaining = 10;
				requiredNumOfSpaceGuess = 2.0;
			}
			else {
				guessRemaining = 0;
				requiredNumOfSpaceGuess = 0.0;
			}
	
			//Part 2: Getting the Secret Word
			secretWord = RandomWord.newWord();
			word = secretWord;
			
			//Part 2.5: Displaying word (secret word as well in testing mode)
			if (testingMode == true)				//Always true for the purpose of the lab, but is there to hide the secret word if testing mode is off
				System.out.println("The secret word is: " + secretWord);
			
			wordLength = word.length();			
			word = "";								//empties word variable
			while (wordLength > 0){					//changes word to all "-"
				word += "-";
				wordLength--;
			}
			System.out.println("The word is: " + word);
			
			//Part 3: Guesses and Updates 
			while (word != secretWord && guessRemaining > 0)	//If word isn't secretWord and there are guesses remaining, the program will keep asking for a guess
			{
				System.out.print("Please enter the letter you want to guess: ");
				guessStr = keyboard.next();
				guessChar = guessStr.charAt(0);
				boolean validInput = true;
				if (!Character.isLetter(guessChar)){			//Error traps inputs that are not letters
					validInput = false;
					System.out.println("Invalid Input.");
				}
				
			//Special Command "solve" which allows users to guess the entire word
				if (guessStr.equalsIgnoreCase("solve") && validInput == true){		//Program checks of the next word entered is the secret word
					System.out.print("Please solve the answer: ");
					word = keyboard.next();
					if (word.equals(secretWord))
						break; 
					else {
						System.out.println("This is not the secret word.");
						guessRemaining--;
						System.out.println("Guesses Remaining: " + guessRemaining);
					}
				}
	
			//Continues if a letter has been guessed as usual instead of a "solve" command
				if (!guessStr.equalsIgnoreCase("solve") && validInput == true){
					System.out.println("Please enter the spaces you want to check (separated by spaces)");
					guessSpaces = keyboard1.nextLine(); //includes spaces in between 
					//Checks each space(aka. index) entered by users which are separated by spaces
					boolean wordChanged = false;
					validInput = true; ///////////boolean
					double numOfSpaceGuess = guessSpaces.length()/2.0; 		//assigns the numOfSpaceGuess as guessSpaces without ' '
					int indexOfSpaces = 0;									//index of guessSpaces
					int specificGuessIndex = Character.getNumericValue(guessSpaces.charAt(indexOfSpaces));	//converts single number in string, guessSpaces, to an integer to be able to be used as an index

					while (indexOfSpaces < guessSpaces.length()){			//when index is less than length of guessSpace
						specificGuessIndex = Character.getNumericValue(guessSpaces.charAt(indexOfSpaces));
						if (specificGuessIndex > secretWord.length() || //Invalid Input IF 1. guess index exceeds max index of word
							 specificGuessIndex < 0 || //2. If the input is not a positive number
							(numOfSpaceGuess != (double) requiredNumOfSpaceGuess && numOfSpaceGuess != (double) requiredNumOfSpaceGuess - 0.5)|| //3. number of spaces guessed does not match the number of space guesses required
							(guessSpaces.charAt(guessSpaces.length()-1) != ' ' && guessSpaces.length() == requiredNumOfSpaceGuess*2)){ //if the last character of guessSpaces is not a space when it is after the last guess made. 
							//Input becomes invalid and it is printed																							//In other words, if user inputs a number or letter after the last index, program prints an error
							validInput =  false;
							System.out.println("Your input is not valid. Try again.");
							break;					//breaks out of this while loop to ask for another letter when invalid input is entered
						}
						else if (secretWord.charAt(specificGuessIndex) == guessChar){ 		//if the letter at the given space is right, the "-" at that space is replaced with guessChar
							word = word.substring(0, specificGuessIndex) + guessChar + word.substring(specificGuessIndex+1);
							wordChanged = true;								//if wordChanged is true, then the guess was correct
						}
						indexOfSpaces += 2; 								//skips the space within the string with the guessSpaces, gets the index of the next specificGuessIndex
					}	
					
					while (validInput == true) {								//only executes these statements when a valid input has been entered
						if (wordChanged == true){								//if word has changed, guess was right, wordChanged is true
							System.out.println("Your guess is in the word!");
							System.out.println("The updated word is: " + word);
							System.out.println("Guess Remaining: " + guessRemaining);
							break;
						}
						else if (wordChanged == false){							//if word remains the same, guess is wrong, wordChanged is false
							System.out.println("Your letter was not found in the spaces provided.");
							guessRemaining--;
							System.out.println("Guess Remaining: " + guessRemaining);
							break;
						}
					}
				}
			}		
		//Win or Loss once secret word is guessed or reamining guess has run out	
			if (word.equals(secretWord)){
				System.out.println("You win!");
				System.out.println("You have guessed the word! Congratulations");
			}								
			else {
				System.out.println("You have failed to guess the word... :(");
			} 								
			
			while (j < 20){
				System.out.println("Would you like to play again? Yes(y) or No(n)");
				String yOrN = keyboard.next();
				if (yOrN.equalsIgnoreCase("y")){
					play = true;
					break;
				}
				else if (yOrN.equalsIgnoreCase("n")){
					play = false;
					System.exit(0);
				}	
				else 
					System.out.println("Invalid Input.");
			}
		j++;
		if (j == 20){
			System.out.println("You have reached the maximum limit of the amount of games you can play.");
			keyboard.close();
			keyboard1.close();
			System.exit(0);
		}							
		}
	}	
}
