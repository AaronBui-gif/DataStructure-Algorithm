import java.util.ArrayList;
import java.util.Random;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
class Guess {
	private static ArrayList<Integer> possibleAnswer = new ArrayList<Integer>();			// Store possible answer
	private static Random random = new Random();
	private static int myGuess = 0;
	private static boolean isStart = false;
	static int make_guess(int hits, int strikes) {
		initialSetting();			// Set up at the first time

		if (myGuess != 0){			// Check if there is a guess is wrong
			String[] guess = String.valueOf(myGuess).split("");		// To get each element of the guess
			if (strikes == 0){												// If there is no strike
				if (hits == 0){												// And no hit
					for (int i = 0; i < possibleAnswer.size(); i++) {		// Remove all the number that contain 1 of elements of the previous guess
						String[] answer = String.valueOf(possibleAnswer.get(i)).split("");
						for (String element : answer) {
							if (element.equals(guess[0]) || element.equals(guess[1]) || element.equals(guess[2]) || element.equals(guess[3])) {
								possibleAnswer.remove(i);
								i--;
								break;
							}
						}
					}
				}
				else {														// Remove all the number that contain 1 element which in the same index with the previous guess
					for (int i = 0; i < possibleAnswer.size(); i++) {
						String[] answer = String.valueOf(possibleAnswer.get(i)).split("");
						if (answer[0].equals(guess[0]) || answer[1].equals(guess[1]) || answer[2].equals(guess[2]) || answer[3].equals(guess[3])) {
							possibleAnswer.remove(i);
							i--;
						}
					}
				}
			}
			if (hits!= 0 || strikes != 0) {									// Remove all the impossible answers
				for (int i = 0; i < possibleAnswer.size(); i++) {
					if (!isPossibleAnswer(strikes, hits, i)) {
						possibleAnswer.remove(i);							// Remove after guessing
						i--;
					}
				}
			}
		}
		if (myGuess != 0) {													// If there is a previous guess
			int rand = random.nextInt(possibleAnswer.size());
			myGuess = possibleAnswer.get(rand);
			possibleAnswer.remove(rand);									// Remove after guessing
		}
		else {																// If there is now the first guess
			String[] guess;
			while (true){
				myGuess = random.nextInt(9000)+1000;
				guess = String.valueOf(myGuess).split("");
				boolean isDifferent = true;
				for (int i = 0; i < guess.length; i++){						// Generate the number that not contain the same element with the others
					for (int ii = 0; ii< guess.length;ii++){
						if (ii != i && guess[i].equals(guess[ii])){
							isDifferent = false;
							break;
						}
					}
					if (!isDifferent){
						break;
					}
				}
				if (isDifferent){
					break;
				}
			}
			possibleAnswer.remove((myGuess - 1000));								// Remove after guessing
		}
		/*
		 * IMPLEMENT YOUR GUESS STRATEGY HERE
		 */
		return myGuess;
	}

	/**
	 * Initial setting
	 */
	private static void initialSetting(){
		if (!isStart){
			for (int i =0; i < 9000; i++){
				possibleAnswer.add(i+1000);
			}
			isStart = true;
		}
	}

	/**
	 * check if there is a possible answer
	 * @param strikes int
	 * @param hits int
	 * @param index int
	 * @return boolean
	 */
	private static boolean isPossibleAnswer(int strikes, int hits, int index){
		String[] target = Integer.toString(myGuess).split("");
		String[] guess = Integer.toString(possibleAnswer.get(index)).split("");
		int hit=0;
		int strike=0;

		// count the strike
		for (int i=0; i<4; i++) {
			if (guess[i].equals(target[i])) {
				strike++;
				target[i] = "x";
				guess[i] = "x";
			}
		}
		// count the hits
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				if (!guess[i].equals("x")) {
					if (guess[i].equals(target[j])) {
						target[j] = "x";
						hit++;
						break;
					}
				}
			}
		}
		return hit == hits && strikes == strike;			// If there is equal number of hits and strikes -> possible answer
	}
}
