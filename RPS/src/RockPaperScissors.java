
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

import rps.Moves.Move;
import rps.Constants;
import rps.Constants.PlayersAndTie;;

public class RockPaperScissors extends JDialog implements Playable {

	// GUI Components
	private volatile static int currentGameNumber;
	private static PlayerColumn p1Column, p2Column;
	private static CenterColumn centerColumn;

	private final static int STATS_WIN_INDEX = 0;
	private final static int STATS_TIE_INDEX = 1;
	private final static int STATS_LOSS_INDEX = 2;
	private final static int STATS_TOTAL_INDEX = 3;
	// used for bar graphs
	private static int[][] p1Stats, p2Stats; // [RPS][WTL] rockW, rockT, rockL, paperW, paperT, paperL, scissorsW, scissorsT, scissorsL

	private static final long serialVersionUID = 4L;

	private static Player winner = null;
	private static Constants.PlayersAndTie[] gameResults;
	private static Move[] p1MoveArray; // [player][game]
	private static Move[] p2MoveArray; // [player][game]

	private static int[] scoreboard_tiesP1WinsP2Wins;
	private static Player player1;
	private static Player player2;


	public static void main(String[] args) {
		if(args.length == 2) {
			try {
				DifficultyDialog.Difficulty difficulty = DifficultyDialog.Difficulty.EASY;
				switch(Integer.parseInt(args[1])) {
				case 1:
					difficulty = DifficultyDialog.Difficulty.EASY;
					break;
				case 2:
					difficulty = DifficultyDialog.Difficulty.MEDIUM;
					break;
				case 3:
					difficulty = DifficultyDialog.Difficulty.HARD;
					break;
				}
				RockPaperScissors rps = new RockPaperScissors((Player)Class.forName(args[0]).newInstance(), new AI(difficulty));
				rps.playGame();
			} catch (NumberFormatException | InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
			}
		} else {
			Player p1 = new AI(DifficultyDialog.Difficulty.HARD);
			Player p2 = new AI(DifficultyDialog.Difficulty.EASY);
			p1.setPlayerNumber(rps.Constants.PlayersAndTie.PLAYER_1);
			p2.setPlayerNumber(rps.Constants.PlayersAndTie.PLAYER_2);
			RockPaperScissors rps =	new RockPaperScissors(p1, p2);
			rps.playGame();
		}
	}

	public RockPaperScissors(Player p1, Player p2) {
		player1 = p1;
		player2 = p2;
	}

	@Override
	public Player playGame() {
		initializeVariables();
		initGUI();

		return winner;
	}

	public static void playRound() {

		// GET MOVES		
		Move p1Move = getMoveFromPlayer(player1);
		p1MoveArray[currentGameNumber] = p1Move;

		Move p2Move = getMoveFromPlayer(player2);
		p2MoveArray[currentGameNumber] = p2Move;

		// DETERMINE WINNER
		Constants.PlayersAndTie result = getRoundWinner(p1Move, p2Move);

		// UPDATE STATS
		gameResults[currentGameNumber] = result;
		updateScoreStats(result, p1Move, p2Move);

		// have to do this every round--don't have to paint every round
		centerColumn.addGridSquareToImage(result);

		// DRAW GUI		
		if(RPS_Thread.threadSleepTime != 0 && currentGameNumber != Constants.TOTAL_NUMBER_OF_GAMES) {
			p1Column.setMoveOnHandImagePanel(p1Move);
			p2Column.setMoveOnHandImagePanel(p2Move);

			updateGUI();
		}

		// INCREMENT GAME NUMBER
		currentGameNumber++;
	}

	public synchronized static int getCurrentGameNumber() {
		return currentGameNumber;
	}

	public static String getPlayerName(PlayersAndTie p) {
		switch(p) {
		case PLAYER_1:
			return player1.getName();
		case PLAYER_2:
			return player2.getName();
		default:
			return "";
		}
	}

	public static int getValidIndex(int i) {
		int gameNum = getCurrentGameNumber();
		if(i < 0) {
			i = 0;
		} else if(i > gameNum - 1) {
			i = currentGameNumber - 1;
		}
		return i;
	}

	public static Move getGameMove(int i, PlayersAndTie p) {
		if(currentGameNumber != 0) {
			i = getValidIndex(i);
			switch(p) {
			case PLAYER_1:
				return p1MoveArray[i];
			case PLAYER_2:
				return p2MoveArray[i];
			default:
				return Move.ERROR;
			}
		}
		return Move.ERROR;
	}

	public static Constants.PlayersAndTie getGameResult(int i) {
		if(currentGameNumber != 0) {
			getValidIndex(i);

			return gameResults[i];
		}
		return Constants.PlayersAndTie.TIE;
	}

	private static void setWinnerAfterMatch() {
		if(scoreboard_tiesP1WinsP2Wins[1] > scoreboard_tiesP1WinsP2Wins[2]) {
			winner = player1;
		} else if(scoreboard_tiesP1WinsP2Wins[2] > scoreboard_tiesP1WinsP2Wins[1]) {
			winner = player2;
		} else if(scoreboard_tiesP1WinsP2Wins[1] == scoreboard_tiesP1WinsP2Wins[2]) {
			winner = breakTie();
		}
	}

	private static void gameOver() {
		centerColumn.showExitButton();
		updateHandImagesAfterMatch();

		updateGUI();
	}

	private static void initializeVariables() {
		currentGameNumber = 0;
		winner = null;
		p1Stats = new int[3][4];
		p2Stats = new int[3][4]; // [RPS][WTL] rockW, rockT, rockL, paperW, paperT, paperL, scissorsW, scissorsT, scissorsL
		p1MoveArray = new Move[Constants.TOTAL_NUMBER_OF_GAMES]; // [player][game]
		p2MoveArray = new Move[Constants.TOTAL_NUMBER_OF_GAMES]; // [player][game]

		gameResults = new Constants.PlayersAndTie[Constants.TOTAL_NUMBER_OF_GAMES];

		scoreboard_tiesP1WinsP2Wins = new int[]{ 0, 0, 0 }; // ties, player 1, player 2
	}

	private static void updateHandImagesAfterMatch() {
		if(winner == player1) {
			p1Column.setResultOnHandImagePanel(Constants.RoundResult.WIN);
			p2Column.setResultOnHandImagePanel(Constants.RoundResult.LOSS);
		} else if(winner == player2) {
			p1Column.setResultOnHandImagePanel(Constants.RoundResult.LOSS);
			p2Column.setResultOnHandImagePanel(Constants.RoundResult.WIN);
		}
	}



	private static Player breakTie() {

		CodeLength p1CodeLength = new CodeLength(player1.getFilename());
		CodeLength p2CodeLength = new CodeLength(player2.getFilename());

		int p1Lines = p1CodeLength.getLines();
		int p2Lines = p2CodeLength.getLines();


		if(p1Lines < p2Lines) {
			String alertMessage = "<html><b style=\"color:green;\">"+player1.getName()+": " + p1Lines + " lines.</b><br>" + player2.getName() + ": " + p2Lines + " lines.</html>";
			TieDialog.show(player1.getName(), alertMessage);
			return player1;
		}
		if(p1Lines > p2Lines) {
			String alertMessage = "<html>"+player1.getName()+": " + p1Lines + " lines.<br><b style=\"color:green;\">" + player2.getName() + ": " + p2Lines + " lines.</b></html>";
			TieDialog.show(player2.getName(), alertMessage);
			return player2;
		}


		int p1Chars = p1CodeLength.getCharacters();
		int p2Chars = p2CodeLength.getCharacters();


		if(p1Chars > p2Chars) {
			String alertMessage = "<html>"+player1.getName()+": " + p1Lines + " lines " + p1Chars + " characters.<br><b style=\"color:green;\">" + player2.getName() + ": " + p2Lines + " lines " + p2Chars + " characters.</b></html>";
			TieDialog.show(player2.getName(), alertMessage);
			return player2;
		}
		String alertMessage = "<html><b style=\"color:green;\">"+player1.getName()+": " + p1Lines + " lines " + p1Chars + " characters.</b><br>" + player2.getName() + ": " + p2Lines + " lines " + p2Chars + " characters.</html>";
		TieDialog.show(player1.getName(), alertMessage);
		return player1;
	}

	private static void updateGUI() {
		p1Column.updateLabelTotal(scoreboard_tiesP1WinsP2Wins[1]);
		p2Column.updateLabelTotal(scoreboard_tiesP1WinsP2Wins[2]);
		centerColumn.updateLabelTotal(scoreboard_tiesP1WinsP2Wins[0]);

		p1Column.repaint();
		p2Column.repaint();

		centerColumn.repaint();
	}

	private void initGUI() {

		setupWindow();

		setIconImage(Constants.getIcon());

		Button exit = new Button("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RockPaperScissors.this.dispose();
			}

		});

		p1Column = new PlayerColumn(Constants.PlayersAndTie.PLAYER_1, player1.getName(), p1Stats);
		centerColumn = new CenterColumn(scoreboard_tiesP1WinsP2Wins, exit);
		p2Column = new PlayerColumn(Constants.PlayersAndTie.PLAYER_2, player2.getName(), p2Stats);

		add(p1Column);
		add(centerColumn);
		add(p2Column);

		setVisible(true); 
	}
	private void setupWindow() {

		setUndecorated(true);

		setSize(new Dimension(1920, 1080));

		setTitle("Rock Paper Scissors");
		setLocation(0, 0);
		setLayout(new GridLayout(1, 3));
		setResizable(false);
		setModal(true);
	}

	private static Move getMoveFromPlayer(Player p) {
		Move playerMove = Move.ERROR;
		try { // protects against bad user code
			playerMove = p.getMove();
		} catch(Throwable e) {
			Constants.log(p.getName() + " threw throwable in getMove() " + e.getMessage());
		}
		return playerMove;
	}

	private static Constants.PlayersAndTie getRoundWinner(Move p1, Move p2) {

		if (p1 == p2) {
			return Constants.PlayersAndTie.TIE;
		} else if (p1 == Move.ROCK && p2 == Move.PAPER) {
			return Constants.PlayersAndTie.PLAYER_2;
		} else if (p1 == Move.PAPER && p2 == Move.ROCK) {
			return Constants.PlayersAndTie.PLAYER_1;
		} else if (p1 == Move.SCISSORS && p2 == Move.ROCK) {
			return Constants.PlayersAndTie.PLAYER_2;
		} else if (p1 == Move.ROCK && p2 == Move.SCISSORS) {
			return Constants.PlayersAndTie.PLAYER_1;
		} else if (p1 == Move.PAPER && p2 == Move.SCISSORS) {
			return Constants.PlayersAndTie.PLAYER_2;
		} else if (p1 == Move.SCISSORS && p2 == Move.PAPER) {
			return Constants.PlayersAndTie.PLAYER_1;
		}

		if(p1 == Move.ERROR) {
			return Constants.PlayersAndTie.PLAYER_2;
		}

		if(p2 == Move.ERROR) {
			return Constants.PlayersAndTie.PLAYER_1;
		}

		return Constants.PlayersAndTie.TIE;
	}

	private static void updateScoreStats(Constants.PlayersAndTie roundWinner, Move p1Move, Move p2Move) {
		switch(roundWinner) {
		case PLAYER_1:

			scoreboard_tiesP1WinsP2Wins[1]++; // p1

			updatePlayerHandStats(p1Stats, p1Move, Constants.RoundResult.WIN);
			updatePlayerHandStats(p2Stats, p2Move, Constants.RoundResult.LOSS);

			break;
		case PLAYER_2:

			scoreboard_tiesP1WinsP2Wins[2]++; // p2

			updatePlayerHandStats(p1Stats, p1Move, Constants.RoundResult.LOSS);
			updatePlayerHandStats(p2Stats, p2Move, Constants.RoundResult.WIN);

			break;
		case TIE:

			scoreboard_tiesP1WinsP2Wins[0]++; // ties

			updatePlayerHandStats(p1Stats, p1Move, Constants.RoundResult.TIE);
			updatePlayerHandStats(p2Stats, p2Move, Constants.RoundResult.TIE);

			break;
		default:
			break;
		}
	}

	private static void updatePlayerHandStats(int[][] stats, Move move, Constants.RoundResult roundResult) {
		try {
			int moveIndex = getStatsIndex(move);

			stats[moveIndex][STATS_TOTAL_INDEX]++;

			switch(roundResult) {
			case LOSS:
				stats[moveIndex][STATS_LOSS_INDEX]++; 
				break;
			case TIE:
				stats[moveIndex][STATS_TIE_INDEX]++;
				break;
			case WIN:
				stats[moveIndex][STATS_WIN_INDEX]++;
				break;
			default:
				break;
			}

		} catch(Exception e) {
			Constants.log("Unable to update stats");
		}
	}

	private static int getStatsIndex(Move move) {
		switch(move) {
		case ERROR:
			return -1;
		case PAPER:
			return 1;
		case ROCK:
			return 0;
		case SCISSORS:
			return 2;
		}
		return -1;
	}

	public static class RPS_Thread extends Thread implements ActionListener {

		public static volatile int threadSleepTime = Constants.DEFAULT_THREAD_SLEEP_TIME;

		private Button startButton;
		private static volatile boolean running = false;

		public RPS_Thread(Button start) {
			running = false;
			startButton = start;
			threadSleepTime = Constants.DEFAULT_THREAD_SLEEP_TIME;
		}

		public void run() {

			while (running && !Thread.currentThread().isInterrupted() && getCurrentGameNumber() < Constants.TOTAL_NUMBER_OF_GAMES) {

				playRound();

				try {
					Thread.sleep(threadSleepTime);
				} catch (InterruptedException e) {
					Constants.log("Interrupted Exception in RPS_Thread (pausing game) " + e.getMessage());
				}
			}

			if(RockPaperScissors.getCurrentGameNumber() >= Constants.TOTAL_NUMBER_OF_GAMES) {
				updateGUI(); // updates grid/graph before pop up if tied
				setWinnerAfterMatch();
				gameOver();
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!running) {
				Thread t = new Thread(this);
				t.start();
				running = true;
				startButton.setText("Pause");
			} else {
				running = false;
				startButton.setText("Start");
			}
		}

		public static boolean isRunning() {
			return running;
		}

		public void setSleepTime(int time) {
			threadSleepTime = time;
		}
	}
}
