import java.util.*;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
class Guess {
	private static HashSet<Integer> possibleAnswer;
	private static HashSet<Integer> allAnswers;
	private static HashMap<Integer, Integer> eliminationList;
	private static Random random = new Random();
	private static int myGuess = 0;
	static boolean isStart = false;
	static int make_guess(int hits, int strikes) {
		initialSetting();			// Set up at the first time
		if (myGuess != 0){			// Check if there is a guess is wrong
			String[] guess = String.valueOf(myGuess).split("");		// To get each element of the guess
			if (strikes == 0){												// If there is no strike
				if (hits == 0){												// And no hit
					for (int i = 0; i < possibleAnswer.size(); i++) {		// Remove all the number that contain 1 of elements of the previous guess
						String[] answer = String.valueOf(getNumber(i, possibleAnswer)).split("");
						for (String element : answer) {
							if (element.equals(guess[0]) || element.equals(guess[1]) || element.equals(guess[2]) || element.equals(guess[3])) {
								possibleAnswer.remove(getNumber(i, possibleAnswer));
								i--;
								break;
							}
						}
					}
				}
				else {														// Remove all the number that contain 1 element which in the same index with the previous guess
					for (int i = 0; i < possibleAnswer.size(); i++) {
						String[] answer = String.valueOf(getNumber(i, possibleAnswer)).split("");
						if (answer[0].equals(guess[0]) || answer[1].equals(guess[1]) || answer[2].equals(guess[2]) || answer[3].equals(guess[3])) {
							possibleAnswer.remove(getNumber(i, possibleAnswer));
							i--;
						}
					}
				}
			}
			if (hits!= 0 || strikes != 0) {									// Remove all the impossible answers
				for (int i = 0; i < possibleAnswer.size(); i++) {
					if (!isPossibleAnswer(strikes, hits, i)) {
						possibleAnswer.remove(getNumber(i, possibleAnswer));							// Remove after guessing
						i--;
					}
				}
			}
		}
		if (myGuess != 0) {													// If there is a previous guess
			// Store the min elimination count of each answer
			for (Integer integer : allAnswers) {
				eliminationList.put(integer, getMinElimination(integer));
			}
			
			// Get the max 
			int maxElimination = eliminationList.get(Collections.max(eliminationList.entrySet(), Map.Entry.comparingByValue()).getKey());
			
			// Set the guess as the key that has the max value
			myGuess = Collections.max(eliminationList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			
			// Check if there is an element in possible answers also has the same value with max
			for (Integer integer : possibleAnswer) {
				if (eliminationList.get(integer) == maxElimination) {	// If yes, Set the guess 
					myGuess = integer;
					break;
				}
			}
			
			// Remove all used answers
			allAnswers.remove(myGuess);
			possibleAnswer.remove(myGuess);
			eliminationList.clear();
		}
		else {																// If there is now the first guess
			String[] guess;
			while (true) {
				myGuess = random.nextInt(9000) + 1000;
				guess = String.valueOf(myGuess).split("");
				boolean isDifferent = true;
				for (int i = 0; i < guess.length; i++) {                        // Generate the number that not contain the same element with the others
					for (int ii = 0; ii < guess.length; ii++) {
						if (ii != i && guess[i].equals(guess[ii])) {
							isDifferent = false;
							break;
						}
					}
					if (!isDifferent) {
						break;
					}
				}
				if (isDifferent) {
					break;
				}
			}
			allAnswers.remove(myGuess);
			possibleAnswer.remove((myGuess));								// Remove after guessing
		}
		/*
		 * IMPLEMENT YOUR GUESS STRATEGY HERE
		 */
		return myGuess;
	}

	/**
	 * Get min number of answers will be eliminated if it meet all cases 
	 * @param myGuess int
	 * @return int
	 */
	private static int getMinElimination(int myGuess){
		int[] s = {0,0,0,0,0,1,1,1,1,2,2,2,3,3,4};
		int[] h = {0,1,2,3,4,0,1,2,3,0,1,2,0,1,0};
		int eliminatedNumber = 0;
		int min = 10000;
		for (int i = 0; i< s.length; i++){
			for (Integer index : possibleAnswer){
				if (!isPossible(s[i], h[i], myGuess, index)) {
					eliminatedNumber++;
				}
			}
			if (eliminatedNumber < min){
				min = eliminatedNumber;
			}
			eliminatedNumber= 0;
		}
		return min;
	}

	/**
	 * Check if this guess is match with the answer in the case (hits and strikes)
	 * @param strikes int
	 * @param hits int
	 * @param target int
	 * @param guess int
	 * @return boolean
	 */
	private static boolean isPossible(int strikes,int hits,int target,int guess) {
		int strike = 0, hit = 0;
		int[] numbers = new int[10];
		for(int i = 0; i < 4; i++) {
			int targetDigit = (int) (target / Math.pow(10, i) % 10); //Get prob answer digit
			int guessDigit = (int) (guess / Math.pow(10, i) % 10); //Get guess digit
			if (targetDigit == guessDigit) {
				strike++;
			} else {
				if (numbers[targetDigit] < 0) hit++;
				if (numbers[guessDigit] > 0) hit++;
				numbers[targetDigit]++;
				numbers[guessDigit]--;
			}
		}
		return (strike == strikes) && (hit == hits);
	}

	/**
	 * Get a number from hash
	 * @param index Integer
	 * @param possibleAnswer HashSet<Integer>
	 * @return Integer
	 */
	private static Integer getNumber(int index, HashSet<Integer> possibleAnswer){ 			// Get number from hash
		int i = 0;
		for (Integer number : possibleAnswer) {
			if (i == index){
				return number;
			}
			i++;
		}
		return null;
	}

	/**
	 * Initial setting
	 */
	private static void initialSetting(){
		if (!isStart){
			myGuess = 0;
			eliminationList = new HashMap<>();
			allAnswers = new HashSet<>();
			possibleAnswer = new HashSet<>();
			for (int i =0; i < 9000; i++){
				possibleAnswer.add(i+1000);
				allAnswers.add(i+1000);
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
		String[] guess = Integer.toString(getNumber(index, possibleAnswer)).split("");
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
