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
	private static ArrayList<Integer> max = new ArrayList<>();
	private static Random random = new Random();
	private static int myGuess = 0;
	static boolean isStart = false;
	static int make_guess(int hits, int strikes) {
		initialSetting();		// Set up at the first time
		if (myGuess != 0) {		// If there is a previous guess
			// eliminate all the impossible answers
			eliminateImpossibleAnswers(hits, strikes);

			// Set the next guess
			setNextGuess();
		}
		else {		// If there is now the first guess
			// Set the first guess
			setFirstGuess();
		}
		// Remove all used answers
		removeGuessFromList();

		return myGuess;
	}

	/**
	 * Eliminate all impossible answers based on the given hits and strikes
	 * @param hits int
	 * @param strikes int
	 */
	private static void eliminateImpossibleAnswers(int hits, int strikes){
		for (int i = 0; i < possibleAnswer.size(); i++) {
			if (!isPossible(strikes, hits, myGuess, getElementByIndex(i,possibleAnswer))) {				// Check if this is a possible answer
				possibleAnswer.remove(getElementByIndex(i, possibleAnswer));							// And remove it if it is not
				i--;
			}
		}
	}

	/**
	 * Remove the wrong guess out of possible answers list
	 */
	private static void removeGuessFromList(){
		allAnswers.remove(myGuess);
		possibleAnswer.remove(myGuess);
		eliminationList.clear();
	}

	/**
	 * Set the next guess
	 */
	private static void setNextGuess(){
		if (possibleAnswer.size() > 1000){						// If the size of the possible answer is too big
			for (Integer integer : possibleAnswer) {
				eliminationList.put(integer, getMinElimination(integer));
			}
		}
		else {													// If the size of the possible answer is too small
			for (Integer integer : allAnswers) {
				eliminationList.put(integer, getMinElimination(integer));
			}
		}
		// Get the max value
		int maxElimination = eliminationList.get(Collections.max(eliminationList.entrySet(), Map.Entry.comparingByValue()).getKey());
		// Set the guess as the key that has the max value
		myGuess = Collections.max(eliminationList.entrySet(), Map.Entry.comparingByValue()).getKey();

		// Clear and store the new items have the same max value
		max.clear();
		// Check if there is any element left in possible answers also has the same value with max
		for (Integer integer : possibleAnswer) {
			if (eliminationList.get(integer) == maxElimination) {    // If yes, store it to the max list
				max.add(integer);
			}
		}
		if (max.size() > 0) {										// If the list contains more than 0 element
			myGuess = max.get(random.nextInt(max.size()));			// Guess will be the random number in list
			if (max.size() > 1){									// If there are more than 1 element in list
				setGuessFromMaxList();								// Set guess as the most relevant possible number
			}
		}

	}

	/**
	 * Set the guess as an element which has the most relations (strikes) with the others.
	 */
	private static void setGuessFromMaxList(){
		int maxRelations = 0;
		for (Integer chosenValue: max){								// Loop from the max list
			int counter = 0;
			for (Integer answer : possibleAnswer) {				// Compare with each possible answer
				int[] strikesAndHits = getStrikesAndHits(answer, chosenValue);				// Get strikes and hits
				if ((strikesAndHits[0] == 2 && strikesAndHits[1] > 0) || strikesAndHits[0] == 3){
					counter++;
				}
			}
			if (counter > maxRelations){					// If it has the highest number of relations
				maxRelations = counter;						// Make it to be a next guess
				myGuess = chosenValue;
			}
		}
	}

	/**
	 * Get Strikes and hits based on a target and a guess
	 * @param answer int
	 * @param chosenValue int
	 * @return int[]
	 */
	private static int[] getStrikesAndHits(int answer, int chosenValue){
		int strike = 0, hit = 0;
		int[] numbers = new int[10];
		for(int i = 0; i < 4; i++) {
			int targetDigit = (int) (answer / Math.pow(10, i) % 10); //Get prob answer digit
			int guessDigit = (int) (chosenValue / Math.pow(10, i) % 10); //Get guess digit
			if (targetDigit == guessDigit) {					// Compare and get strike
				strike++;
			} else {											// Get hits
				if (numbers[targetDigit] < 0) hit++;
				if (numbers[guessDigit] > 0) hit++;
				numbers[targetDigit]++;
				numbers[guessDigit]--;
			}
		}
		return new int[]{strike, hit};
	}

	/**
	 *	Set the first guess ( getting a number which has unique elements
	 */
	private static void setFirstGuess(){
		String[] guess;
		while (true) {
			myGuess = random.nextInt(9000) + 1000;
			guess = String.valueOf(myGuess).split("");
			boolean isDifferent = true;
			for (int i = 0; i < guess.length; i++) {                        // Generate the number that not contain the same element with the others
				for (int ii = 0; ii < guess.length; ii++) {
					if (ii != i && guess[i].equals(guess[ii])) {			// If the guess does not contain the unique element
						isDifferent = false;
						break;
					}
				}
				if (!isDifferent) {					// Break and re-generate another number
					break;
				}
			}
			if (isDifferent) {				// If there is a guess contains all distinct elements
				break;
			}
		}
	}

	/**
	 * Get min number of answers will be eliminated if it meet all cases
	 * @param myGuess int
	 * @return int
	 */
	private static int getMinElimination(int myGuess){
		int[] strikeCases = {0,0,0,0,0,1,1,1,1,2,2,2,3,3,4};
		int[] hitCases = {0,1,2,3,4,0,1,2,3,0,1,2,0,1,0};
		int eliminatedNumber = 0;
		int min = 10000;

		// Go through every cases
		for (int i = 0; i< strikeCases.length; i++){
			for (Integer index : possibleAnswer){			// With each element in possible answer
				if (!isPossible(strikeCases[i], hitCases[i], myGuess, index)) {		// Checking and get the number of eliminations
					eliminatedNumber++;
				}
			}
			if (eliminatedNumber < min){					// Get the min number of eliminations if this number is set as a guess
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
		String[] t = String.valueOf(target).split("");
		String[] g = String.valueOf(guess).split("");
		if (strikes == 0 && hits > 0){				// If there is no strikes
			if (t[0].equals(g[0]) || t[1].equals(g[1]) || t[2].equals(g[2]) || t[3].equals(g[3])){
				return false;
			}
		}
		if (strikes == 0 && hits == 0){				// If there is a miss
			for (String element : g) {
				if (element.equals(t[0]) || element.equals(t[1]) || element.equals(t[2]) || element.equals(t[3])) {
					return false;
				}
			}
		}
		for(int i = 0; i < 4; i++) {
			int targetDigit = (int) (target / Math.pow(10, i) % 10); //Get prob answer digit
			int guessDigit = (int) (guess / Math.pow(10, i) % 10); //Get guess digit
			if (targetDigit == guessDigit) {
				strike++;											// Count strikes
			} else {
				if (numbers[targetDigit] < 0) hit++;				// Count hits
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
	private static Integer getElementByIndex(int index, HashSet<Integer> possibleAnswer){ 			// Get number from hash
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
	 * Initial setting before starting the game
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



}
