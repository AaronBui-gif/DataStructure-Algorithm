import java.util.*;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
class Guess {
	private static LinkedHashSet<Integer> possibleAnswer;
	private static Random random = new Random();
	private static int myGuess = 0;
	static boolean isStart = false;
	static void sortByIndex(int index){
		ArrayList<Integer> sortedAnswer = new ArrayList<>();
		for (int number = 0; number< 10; number++) {
			for (int i = 0; i < possibleAnswer.size(); i++) {
				String string = String.valueOf(getNumber(i, possibleAnswer));
				if (string.split("")[index].equals(String.valueOf(number))){
					sortedAnswer.add (getNumber(i, possibleAnswer));
				}
			}
		}
		possibleAnswer = new LinkedHashSet<Integer>();
		possibleAnswer.addAll(sortedAnswer);
	}

	private static void findAndRemove(String key, int index) {                 // Regular binary search for sorted array instead of split sorted one like the question1
		int startIndex = 0;
		int lastIndex = possibleAnswer.size() - 1;
		int mid;
		while (lastIndex <= startIndex)
		{
			mid = (startIndex + lastIndex) / 2;
			if (String.valueOf(getNumber(mid, possibleAnswer)).split("")[index].equals(key)){
				possibleAnswer.remove(getNumber(mid, possibleAnswer));
				lastIndex = possibleAnswer.size() -1;
				startIndex = 0;
			}
			else if ( Integer.parseInt(String.valueOf(getNumber(mid, possibleAnswer)).split("")[index]) < Integer.parseInt(key)){
				startIndex = mid + 1;
			}
			else {
				lastIndex = mid - 1;
			}
		}
	}

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
					for (int i = 0; i < 4; i++){
						sortByIndex(i);
						findAndRemove(guess[i], i);
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
			int rand = random.nextInt(possibleAnswer.size());
			myGuess = getNumber(rand, possibleAnswer);
			possibleAnswer.remove(myGuess);                                    // Remove after guessing
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
			possibleAnswer.remove((myGuess));								// Remove after guessing
		}
		/*
		 * IMPLEMENT YOUR GUESS STRATEGY HERE
		 */
		return myGuess;
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
			System.out.println("go");
			myGuess = 0;
			possibleAnswer = new LinkedHashSet<>();
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
