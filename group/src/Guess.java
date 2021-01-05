import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
class Guess {
	private static ArrayList<Integer> possibleAnswer = new ArrayList<Integer>();
	private static Random random = new Random();
	private static int myGuess = 0;
	private static boolean isStart = false;
	static int make_guess(int hits, int strikes) {
		initialSetting();
		if (myGuess != 0){
			String[] guess = String.valueOf(myGuess).split("");
			if (strikes == 0){
				for (int i = 0; i < possibleAnswer.size(); i++){
					String[] answer = String.valueOf(possibleAnswer.get(i)).split("");
					if (answer[0].equals(guess[0]) || answer[1].equals(guess[1]) || answer[2].equals(guess[2])|| answer[3].equals(guess[3])){
						possibleAnswer.remove(i);
						i--;
					}
				}
			}
			for (int i = 0; i < possibleAnswer.size(); i++) {
				if (!isPossibleAnswer(strikes, hits, i)) {
					possibleAnswer.remove(i);
					i--;
				}
			}
		}
		int rand = random.nextInt(possibleAnswer.size());
		myGuess = possibleAnswer.get(rand);
		possibleAnswer.remove(rand);
		/*
		 * IMPLEMENT YOUR GUESS STRATEGY HERE
		 */
		return myGuess;
	}

	private static void initialSetting(){
		if (!isStart){
			for (int i =0; i < 8999; i++){
				possibleAnswer.add(i+1000);
			}
			isStart = true;
		}
	}

	private static boolean isPossibleAnswer(int strikes, int hits, int index){
		String[] target = Integer.toString(myGuess).split("");
		String[] guess = Integer.toString(possibleAnswer.get(index)).split("");
		int hit=0;
		int strike=0;

		// process strikes
		for (int i=0; i<4; i++) {
			if (guess[i].equals(target[i])) {
				strike++;
				target[i] = "x";
				guess[i] = "x";
			}
		}
		// process hits
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
		return hit == hits && strikes == strike;
	}
}