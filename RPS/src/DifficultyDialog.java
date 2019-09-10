
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rps.Constants;

public class DifficultyDialog {

	public enum Difficulty {
		EASY, MEDIUM, HARD, PREVIOUSWINNER, ERROR;
	}

	private static String previousWinner;
	private static JDialog jd = null;
	private static Difficulty selectedDifficulty = Difficulty.ERROR;

	public static Difficulty getAIDifficulty() {
		selectedDifficulty = Difficulty.ERROR; // allows to x out of dialog without running match
		showDialog(loadButtonOptions());
		return selectedDifficulty;
	}

	public static String getPreviousWinnerName() {
		return previousWinner;
	}

	private static Button[] loadButtonOptions() {
		Button[] difficultyOptions;
		if(previousWinnerExists()) {
			difficultyOptions = new Button[4];
			difficultyOptions[3] = new Button(previousWinner);
			difficultyOptions[3].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedDifficulty = Difficulty.PREVIOUSWINNER;
					jd.dispose();
				}
			});	
		} else {
			difficultyOptions = new Button[3];
		}
		difficultyOptions[0] = new Button("Easy");
		difficultyOptions[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedDifficulty = Difficulty.EASY;
				jd.dispose();

			}
		});
		difficultyOptions[1] = new Button("Medium");
		difficultyOptions[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedDifficulty = Difficulty.MEDIUM;
				jd.dispose();
			}
		});
		difficultyOptions[2] = new Button("Hard");
		difficultyOptions[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedDifficulty = Difficulty.HARD;
				jd.dispose();
			}
		});

		return difficultyOptions;
	}

	private static void showDialog(Button[] difficultyOptions) {

		JLabel ll = new JLabel();

		try {
			final ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(Constants.GUI_ICON));
			ll.setIcon(icon);
		} catch(NullPointerException e) {
			System.err.println("Unable to load difficulty dialog icon");
		}

		jd = new JDialog();
		jd.setResizable(false);
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel tf = new JLabel("Select opponent's difficulty:");
		panel.add(ll);
		panel.add(tf);

		for(int i = 0; i < difficultyOptions.length; i++) {
			buttonPanel.add(difficultyOptions[i]);	
		}

		if(difficultyOptions.length == 3) {
			jd.setSize(300,130);
		} else {
			jd.setSize(350,130);
		}

		showOnScreen(0, jd);
		jd.add(panel, BorderLayout.NORTH);
		jd.add(buttonPanel, BorderLayout.SOUTH);
		jd.setModal(true);
		jd.setVisible(true);

	}

	public static void showOnScreen( int screen, JDialog frame ) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if( screen > 0 && screen < 2 ) {
			int x = frame.getWidth()/2;
			int y = frame.getHeight()/2;
			frame.setLocation((gd[screen].getDefaultConfiguration().getBounds().width * 3/2) - x,   (gd[screen].getDefaultConfiguration().getBounds().height *1/2) - y);
		} else if( gd.length > 0 ) {
			frame.setLocationRelativeTo(null);
		} else {
			throw new RuntimeException( "No Screens Found" );
		}
	}

	private static boolean previousWinnerExists() {
		try {
			Scanner sc = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.PREVIOUS_WINNER_FILE));
			previousWinner = sc.next();
			sc.close();
			return true;
		} catch (IOException ex) {
			// this is possible if this file hasn't been created yet
		}
		return false;
	}

	public static void main(String[] args) {
		// test dialog
		System.out.println(DifficultyDialog.getAIDifficulty());
	}
}
