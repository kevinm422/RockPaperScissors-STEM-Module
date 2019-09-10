
import static rps.Moves.Move.*;


import rps.Constants.*;
import rps.Moves.Move;


public abstract class Player {

	private PlayersAndTie player;

	// user code change their playerNumber -> no advantage
	// done this way to prevent needing a constructor in the user's code
	public void setPlayerNumber(PlayersAndTie playerNumber) {
		this.player = playerNumber;
	}

	/**
	 * Returns the current gameNumber
	 * @return
	 */
	protected int gameNumber() {
		return RockPaperScissors.getCurrentGameNumber();
	}

	/**
	 * 
	 * @return
	 */
	public abstract Move getMove();
	/**
	 * 
	 * @param index
	 * @return 	0 - rock, 1 - paper, 2 - scissors
	 */ 
	protected Move getMyMove(int index) {
		index = RockPaperScissors.getValidIndex(index);

		try {
			return RockPaperScissors.getGameMove(index, player);
		} catch(Exception e) {

		}
		return ERROR;
	}

	protected Move getTheirMove(int index) {
		index = RockPaperScissors.getValidIndex(index);

		try {
			return RockPaperScissors.getGameMove(index, player.other());
		} catch(Exception e) {

		}
		return ERROR;

	}

	protected Move theirLastPlay() {

		if(gameNumber() == 0) {
			return ERROR;
		}
		return getTheirMove(gameNumber()-1);
	}

	protected Move myLastPlay() {
		if(gameNumber() == 0) {
			return ERROR;
		}
		return getMyMove(gameNumber()-1);
	}

	public String getName() {
		return this.getClass().getName();
	}

	public String getFilename() {
		return this.getClass().getName() + ".java";
	}

	protected int winsBetween(int a, int b) {
		int wins = 0;

		a = RockPaperScissors.getValidIndex(a);
		b = RockPaperScissors.getValidIndex(b);

		try {
			if(b < a) {
				int c = a;
				a = b;
				b = c;
			}
			for(int i = a; i < b; i++) {
				if(RockPaperScissors.getGameResult(i) == player) {
					wins++;
				}
			}

		} catch(Exception e) {
			rps.Constants.log("Exception in winsBetween a:" + a + " b: " + b + " game: " + gameNumber());
		}
		return wins;
	}
	protected int lossesBetween(int a, int b) {
		int losses = 0;

		a = RockPaperScissors.getValidIndex(a);
		b = RockPaperScissors.getValidIndex(b);

		try {
			if(b < a) {
				int c = a;
				a = b;
				b = c;
			}
			for(int i = a; i < b; i++) {
				if(RockPaperScissors.getGameResult(i) == player.other()) {
					losses++;
				}
			}

		} catch(Exception e) {
			rps.Constants.log("Exception in lossesBetween a:" + a + " b: " + b + " game: " + gameNumber());
		}
		return losses;
	}
	protected int tiesBetween(int a, int b) {
		int ties = 0;

		a = RockPaperScissors.getValidIndex(a);
		b = RockPaperScissors.getValidIndex(b);

		try {
			if(b < a) {
				int c = a;
				a = b;
				b = c;
			}
			for(int i = a; i < b; i++) {
				if(RockPaperScissors.getGameResult(i) == player.other()) {
					ties++;
				}
			}

		} catch(Exception e) {
			rps.Constants.log("Exception in tiesBetween a:" + a + " b: " + b + " game: " + gameNumber());
		}
		return ties;
	}

	protected int myWinPercentage(int a, int b) {
		if(b < a) {
			int c = a;
			a = b;
			b = c;
		}
		int wins = winsBetween(a, b);

		return (int)(100*(double)(wins / (double)((b - a))));
	}
	protected int theirWinPercentage(int a, int b) {
		if(b < a) {
			int c = a;
			a = b;
			b = c;
		}
		int losses = lossesBetween(a, b);

		return (int)(100*(double)(losses / (double)((b - a))));
	}


	// 1 - win, 2 - lose, 0 tie
	protected boolean wonLastGame() {
		if(gameNumber() == 0) {
			return false;
		}

		return RockPaperScissors.getGameResult(gameNumber()-1) == player;

	}

	protected boolean tiedLastGame() {
		if(gameNumber() == 0) {
			return false;
		}

		return RockPaperScissors.getGameResult(gameNumber()-1) == PlayersAndTie.TIE;
	}
	protected boolean lostLastGame() {
		if(gameNumber() == 0) {
			return false;
		}
		return RockPaperScissors.getGameResult(gameNumber()-1) == player.other();
	}

	protected Move cyclePlay(Move m) {
		return m.cycle();
	}

	protected Move cyclePlay() {
		return cyclePlay(myLastPlay());
	}

	protected Move theirMostThrown(int a, int b) {
		int rock = 0, paper = 0, scissors = 0;

		a = RockPaperScissors.getValidIndex(a);
		b = RockPaperScissors.getValidIndex(b);

		if(b < a) {
			int c = a;
			a = b;
			b = c;
		}

		for(int i = a; i < b; i++) {
			switch(RockPaperScissors.getGameMove(i, player.other())) {
			case ROCK:
				rock++;
				break;
			case PAPER:
				paper++;
				break;
			case SCISSORS:
				scissors++;
				break;
			default:
				break;
			}
		}

		if(rock > paper && rock > scissors) {
			return ROCK;
		}
		if(paper > scissors) {
			return PAPER;
		}
		return SCISSORS;
	}

	protected Move myMostThrown(int a, int b) {
		int rock = 0, paper = 0, scissors = 0;

		a = RockPaperScissors.getValidIndex(a);
		b = RockPaperScissors.getValidIndex(b);

		if(b < a) {
			int c = a;
			a = b;
			b = c;
		}

		for(int i = a; i < b; i++) {
			switch(RockPaperScissors.getGameMove(i, player)) {
			case ROCK:
				rock++;
				break;
			case PAPER:
				paper++;
				break;
			case SCISSORS:
				scissors++;
				break;
			default:
				break;
			}
		}

		if(rock > paper && rock > scissors) {
			return ROCK;
		}
		if(paper > scissors) {
			return PAPER;
		}
		return SCISSORS;
	}
}
