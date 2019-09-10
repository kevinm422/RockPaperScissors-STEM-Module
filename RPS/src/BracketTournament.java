
import rps.Constants;
import javax.imageio.ImageIO;
import java.lang.Math;
import java.net.URL;
import java.net.URLClassLoader;
import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BracketTournament extends JFrame {

	private static final long serialVersionUID = 1L;

	private ArrayList<Player> list_of_players = null;
	private static DrawPanel dp;
	private BufferedImage trophy = null;

	public static void main(String[] args) {
		ArrayList<String> names = new ArrayList<String>();

		// Open File
		Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.ROSTER_FILE));
		} catch (IOException e) {
			System.err.println("Cannot read roster." + Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
			System.exit(1);
		}

		while(sc.hasNext()) {
			names.add(sc.next());
		}

		if(sc != null) {
			sc.close();
		}

		BracketTournament t = new BracketTournament(names);
		t.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public BracketTournament(ArrayList<String> names) {
		init(names);
	}

	private void init(ArrayList<String> names) {

		loadPlayers(names);

		setupGUI();

		setIconImage(Constants.getIcon());
		loadTrophy();

		add(getDrawPanel());

		TournamentMatchupButton.setupButtons(list_of_players, new Dimension(dp.getWidth(), dp.getHeight()));

		setVisible(true);
	}

	public static void addButtonsToPanel(ArrayList<TournamentMatchupButton> buttons) {
		for(TournamentMatchupButton b : buttons) {
			dp.add(b);
		}
	}

	private DrawPanel getDrawPanel() {
		dp = new DrawPanel();
		dp.setSize(getWidth(), getHeight());
		dp.setLayout(null);
		dp.add(setupExitButton());
		return dp;
	}

	private void loadPlayers(ArrayList<String> names) {

		list_of_players = new ArrayList<Player>();

		for(String name : names) {
			createPlayerAndAddToList(name);
		}

		addPlayersUntilPowerOfTwo();

		Collections.shuffle(list_of_players, new Random(System.currentTimeMillis()));
	}
	private void createPlayerAndAddToList(String name) {
		try {
			// Load and instantiate compiled class.
			File root = new File(Constants.PLAYERS_FILEPATH); // .. is "rps"
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class<?> cls = Class.forName(name, true, classLoader);

			list_of_players.add((Player)cls.newInstance());

		} catch(Exception cnfe) {
			Constants.log("Couldn't create player class for " + name);
		}
	}
	private void setupGUI() {
		setUndecorated(true);
		setSize(1920, 1080);
		setTitle("RPS Tournament");
		setLocation(0, 0);
		setLayout(null);
	}
	private void addPlayersUntilPowerOfTwo() {
		// make number of players a power of 2
		int powerOfTwo = 2;
		while(list_of_players.size() > powerOfTwo) { // keep looking for next highest
			powerOfTwo *= 2;
		}

		if(list_of_players.size() < powerOfTwo) {

			DifficultyDialog.Difficulty difficulty = DifficultyDialog.getAIDifficulty();

			int list_length = list_of_players.size();

			for(int i = 0; i < powerOfTwo - list_length; i++) {
				Player dp = new AI(difficulty);
				list_of_players.add(dp);
			}
		}
	}


	private void loadTrophy() {
		try {
			URL url = RockPaperScissors.class.getResource("trophy.png");
			trophy = ImageIO.read(url);
		} catch (IOException e) {
			Constants.log("Failed to load trophy image in Tournament: trophy.png");
		}
	}

	private Button setupExitButton() {
		Button exit = new Button("Exit");
		exit.setSize(60, 50);
		exit.setLocation(this.getWidth() - exit.getWidth() - 10, this.getHeight() - exit.getHeight() - 10);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return exit;
	}

	public static void tournamentOver(Player winner) {

		createPreviousWinnerFile(winner.getName());

		String victory = winner.getName() + " wins!";
		Graphics2D g2 = (Graphics2D) dp.getGraphics();
		Font font = new Font("Courier", Font.PLAIN, 76);
		FontMetrics metrics = g2.getFontMetrics(font);
		int x = (dp.getWidth() - metrics.stringWidth(victory)) / 2;
		int y = (dp.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		// Set the font
		g2.setColor(ColorScheme.left_column);
		g2.fill(new Rectangle2D.Double(x-10,y-metrics.getAscent(), metrics.stringWidth(victory)+10, metrics.getHeight()+10));
		g2.setColor(Color.white);
		g2.setFont(font);
		g2.drawString(victory, x, y);
	}

	private static void createPreviousWinnerFile(String winnerName) {
		try {
			PrintWriter pw  = new PrintWriter(new File(Constants.FILES_FILEPATH + Constants.PREVIOUS_WINNER_FILE));

			pw.write(winnerName);

			if (pw != null) {
				pw.close();
			}

		} catch (FileNotFoundException fnfe) {
			Constants.log("Couldn't write previous winner file after " + winnerName + " won: " + Constants.FILES_FILEPATH + Constants.PREVIOUS_WINNER_FILE);
		}
	}

	private class DrawPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public void drawGrid(Graphics2D g2) {
			g2.setStroke(new BasicStroke(10));
			g2.setColor(ColorScheme.left_column);
			int columns = (int)(Math.log(list_of_players.size()) / Math.log(2));
			int rows = list_of_players.size() / 2;

			g2.drawImage(trophy, (columns-1)*(this.getWidth()/columns), 0, this.getWidth()/columns, this.getHeight(), null, null);

			for(int i = 0; i < columns+1; i++) {
				// vertical lines
				g2.draw(new Line2D.Double(((this.getWidth()/columns)*i), 0, (this.getWidth()/columns)*i, this.getHeight()));

				// horizontal lines
				for(int j = 0; j < rows; j++) {
				
					//                              x1                          y1                            x2                              y2
					g2.draw(new Line2D.Double((this.getWidth()/columns)*i, (this.getHeight()/rows)*j, (this.getWidth()/columns)*(i+1), (this.getHeight()/rows)*j));
				}
				rows = rows/2;
			}

			// draw bottom line
			g2.draw(new Line2D.Double(0, this.getHeight(), this.getWidth(), this.getHeight()));
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;

			g2.setColor(ColorScheme.centerColumnBackground);
			g2.fillRect(0, 0, this.getWidth(),this.getHeight());

			drawGrid(g2);
		}
	}


}
