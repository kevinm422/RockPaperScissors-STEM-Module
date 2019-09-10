package rps;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

public class Constants {

	public final static int TOTAL_NUMBER_OF_GAMES = 10000;

	public final static String GUI_ICON = "gui_icon.png";
	public final static String LOG_FILE = "log";
	public final static String COLOR_FILE = "colors";
	public final static String ROSTER_FILE = "tournament_roster";
	public final static String OLD_FILES_FILEPATH = "../old_player_files/";
	public final static String PREVIOUS_WINNER_FILE = "previous_winner";

	public final static String PLAYERS_FILEPATH = "../Players/";
	public final static String FILES_FILEPATH = "";
	public final static int DEFAULT_THREAD_SLEEP_TIME = 2000;
	
	public enum PlayersAndTie {
		PLAYER_1 {
			@Override
			public PlayersAndTie other() {
				return values()[1];
			}
		}, PLAYER_2 {
			@Override
			public PlayersAndTie other() {
				return values()[0];
			}
		}, TIE {
			@Override
			public PlayersAndTie other() {
				return values()[2];
			}
		};

		public abstract PlayersAndTie other();
	}

	public enum RoundResult {
		WIN, LOSS, TIE;
	}
	
	public static boolean isNameInRoster(String name) {
		
		boolean nameInRoster = false;
		try {
			Scanner sc = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.ROSTER_FILE));

			while(sc.hasNext()) {
				if(sc.next().equals(name)) {
					nameInRoster = true;
				}
			}
			sc.close();
		} catch (IOException ex) {
			Constants.log("Cannot read roster: " + Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
		}
		return nameInRoster;
	}

	public static void addNameToRoster(String playerName) {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(Constants.FILES_FILEPATH + Constants.ROSTER_FILE, true)));
			pw.println(playerName);
			if (pw != null) {
				pw.close();
			}
		} catch (IOException fnfe) {
			log("Cannot write to players_file to add " + playerName + ".");
		}
	}

	public static Image getIcon() {
		URL url = ClassLoader.getSystemResource(Constants.GUI_ICON);
		Toolkit kit = Toolkit.getDefaultToolkit();
		return kit.createImage(url);

	}

	public static void createRosterIfMissing() {
		File roster = new File(Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
		if(!roster.exists()) {
			try {				
				roster.createNewFile();
			} catch (IOException e) {
				Constants.log("Couldn't write or read roster file! " + Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
			} // if file already exists will do nothing 
		}
		roster = null;
	}

	public static void log(String s) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(Constants.FILES_FILEPATH + Constants.LOG_FILE, true));			
			pw.println(new Timestamp(new Date().getTime()) + ": " + s);
			pw.close();
		} catch (IOException fnfe) {
			fnfe.printStackTrace();
		}
	}
}
