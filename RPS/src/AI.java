
import static rps.Moves.Move.*;

import rps.Moves.Move;

/**
 * Code for students to test against and be included to fill a tournament bracket.
 * @author martin
 *
 */
public class AI extends Player {

	DifficultyDialog.Difficulty difficulty;

	public AI(DifficultyDialog.Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Move getMove() {

		switch(difficulty) {

		case EASY:
			return PAPER;
		case MEDIUM:
			if(wonLastGame()) {
				return myLastPlay();
			} else {
				return cyclePlay(cyclePlay());
			}
		case HARD:
			if(gameNumber() < 100) {
				if(wonLastGame()) {
					return myLastPlay();
				} else {
					return cyclePlay(cyclePlay());
				}
			}
			if(gameNumber() < 200) {
				return SCISSORS;
			}
			if(gameNumber() < 300) {
				return ROCK;
			}
			if(gameNumber() < 400) {			
				return PAPER;
			}
			if(gameNumber() < 500) {
				return cyclePlay(cyclePlay());
			}
			if(gameNumber() < 600) {
				return SCISSORS;
			}
			if(gameNumber() < 700) {
				return PAPER;
			}
			if(gameNumber() < 800) {
				return ROCK;
			}
			if(gameNumber() < 900) {
				return cyclePlay();
			}
			for(int i = 0; i < gameNumber()-3; i++) {
				if(getTheirMove(i).equals(getTheirMove(gameNumber()-2))) {
					if(getMyMove(i).equals(getMyMove(gameNumber()-2))) {
						if(getTheirMove(i+1).equals(getTheirMove(gameNumber()-1))) {
							if(getMyMove(i+1).equals(getMyMove(gameNumber()-1))) {
								if(getTheirMove(i+2).equals(getTheirMove(gameNumber()))) {
									if(getMyMove(i+2).equals(getMyMove(gameNumber()))) {
										return cyclePlay(getTheirMove(i+3));
									}
								}
							}
						}
					}
				}
			}
		default:
			return ROCK;
		}
	}

	@Override
	public String getName() {
		return "AI (" + difficulty.toString() + ")";
	}
}
