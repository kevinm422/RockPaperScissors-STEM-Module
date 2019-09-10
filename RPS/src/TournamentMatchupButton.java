
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import rps.Constants;


public class TournamentMatchupButton extends Button {

	private static final long serialVersionUID = 1L;
	private static ArrayList<TournamentMatchupButton> listOfMatchupButtons = new ArrayList<TournamentMatchupButton>();
	private static int buttonWidth, buttonHeight;

	private Player firstPlayerInMatchup = null;

	public TournamentMatchupButton() {
		setText("___________<br>vs<br>___________");
		format();
		setEnabled(false);
	}

	public TournamentMatchupButton(Player p1, Player p2) {
		setText(p1.getName() + "<br>vs<br>" + p2.getName());

		listOfMatchupButtons.add(this);

		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TournamentMatchupButton.this.setEnabled(false);
				p1.setPlayerNumber(Constants.PlayersAndTie.PLAYER_1);
				p2.setPlayerNumber(Constants.PlayersAndTie.PLAYER_2);
				Playable game = new RockPaperScissors(p1, p2);
				Player winner = game.playGame();

				updateButtonsAfterMatch(TournamentMatchupButton.this, winner);
			}
		});
		format();
		setEnabled(true);
	}

	private void format() {
		setSize(buttonWidth, buttonHeight);
		formatButton(this);
	}


	public static void updateButtonsAfterMatch(TournamentMatchupButton buttonLastPlayed, Player winner) {
		int indexOfLastGame = listOfMatchupButtons.indexOf(buttonLastPlayed);	
		if(indexOfLastGame != 0) {

			int parent_index = (int)Math.floor(indexOfLastGame-1)/2;

			TournamentMatchupButton parent = listOfMatchupButtons.get(parent_index);	
			
			// Add winner to parent button
			if(parent.firstPlayerInMatchup == null) {
				parent.firstPlayerInMatchup = winner;
				parent.setText(parent.firstPlayerInMatchup.getName() + "<br>vs<br>___________");
			} else {
				parent.setText(parent.firstPlayerInMatchup.getName() + "<br>vs<br>" + winner.getName());
				parent.setEnabled(true);
				parent.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						parent.firstPlayerInMatchup.setPlayerNumber(Constants.PlayersAndTie.PLAYER_1);
						winner.setPlayerNumber(Constants.PlayersAndTie.PLAYER_2);
						Playable game = new RockPaperScissors(parent.firstPlayerInMatchup, winner);
						Player thisGameWinner = game.playGame();
						parent.setEnabled(false);
						updateButtonsAfterMatch(parent, thisGameWinner);
					}
				});
			}
		} else {
			BracketTournament.tournamentOver(winner);
		}	
	}

	public static void setupButtons(ArrayList<Player> list_of_players, Dimension size) {

		int columns = (int)(Math.log(list_of_players.size()) / Math.log(2));
		int rows = list_of_players.size() / 2;
		double button_height = .8 * size.getHeight()/rows;
		double button_width = .8 * size.getWidth()/columns;
		setButtonSize(button_width, button_height);

		// CREATE FIRST ROUND MATCHUPS
		int i = 0;
		while(i+1 < list_of_players.size()) {
			TournamentMatchupButton b = new TournamentMatchupButton(list_of_players.get(i), list_of_players.get(i+1));
			b.setLocation((int)(.1 * button_width), (int)((size.getHeight()/rows)*(i/2)+(.1 * button_height)));
			i += 2;
		}

		// CREATE OTHER ROUNDS
		rows = rows/2;
		ArrayList<TournamentMatchupButton> buttonsincolumn = new ArrayList<TournamentMatchupButton>();
		for(int k = 1; k < columns; k++) {
			for(int j = 0; j < rows; j++) {
				TournamentMatchupButton b = new TournamentMatchupButton();
				b.setLocation((int)((k * (size.getWidth()/columns)) + (.1 * button_width)), (int)(((size.getHeight()/rows)*j)+(size.getHeight()/(2*rows))-(button_height/2)));
				buttonsincolumn.add(b);
			}
			addColumn(buttonsincolumn);
			buttonsincolumn.clear();
			rows = rows/2;
		}

		BracketTournament.addButtonsToPanel(listOfMatchupButtons);
	}

//	private static void printButtons() {
//		for(TournamentMatchupButton b : listOfMatchupButtons) {
//			System.out.println(b.toString());
//		}
//	}
	
	public String toString() {
		return "index: " + listOfMatchupButtons.indexOf(TournamentMatchupButton.this) + " text " + this.getText();
	}

	public static void clearButtons() {
		listOfMatchupButtons.clear();
		listOfMatchupButtons = new ArrayList<TournamentMatchupButton>();
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
	}

	public void setText(String s) {
		super.setText("<html><h1 align=center>" + s + "</h1></html>");
	}

	public static void setButtonSize(double w, double h) {
		buttonWidth = (int)w;
		buttonHeight = (int)h;
	}
	public static void addColumn(ArrayList<TournamentMatchupButton> col) {
		listOfMatchupButtons.addAll(0, col);
	}

}
