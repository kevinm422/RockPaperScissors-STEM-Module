
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import rps.Constants;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class TestCode implements ActionListener {

	private JTextArea errorTextArea;
	private RSyntaxTextArea codeTextArea;

	public TestCode(RSyntaxTextArea rsta, JTextArea eta) {
		codeTextArea = rsta;
		errorTextArea = eta;
	}

	public void actionPerformed(ActionEvent e) {
		String name = Student.getName();
		String source = codeTextArea.getText();

		try {
			// Save source in .java file.
			File root = new File(Constants.PLAYERS_FILEPATH); // .. is "rps"
			File sourceFile = new File(root, name + ".java");
			sourceFile.getParentFile().mkdirs();
			Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));

			String compileResults = CompileCode.compile(name + ".java");
			if(compileResults.equals("")) {

				errorTextArea.setText("");

				// Load and instantiate compiled class.
				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
				Class<?> cls = Class.forName(name, true, classLoader);
				Object instance = cls.newInstance();

				DifficultyDialog.Difficulty difficulty = DifficultyDialog.getAIDifficulty();

				if(difficulty != DifficultyDialog.Difficulty.ERROR) {
					if(difficulty != DifficultyDialog.Difficulty.PREVIOUSWINNER) {
						Player p1 = (Player)instance;
						Player p2 = new AI(difficulty);
						p1.setPlayerNumber(Constants.PlayersAndTie.PLAYER_1);
						p2.setPlayerNumber(Constants.PlayersAndTie.PLAYER_2);
						Playable game = new RockPaperScissors(p1, p2);
						game.playGame();
					} else {
						try {
							classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
							cls = Class.forName(DifficultyDialog.getPreviousWinnerName(), true, classLoader);

							Player p1 = (Player)instance;
							p1.setPlayerNumber(Constants.PlayersAndTie.PLAYER_1);
							Player insanePlayer = (Player)cls.newInstance(); // Should print "world".
							insanePlayer.setPlayerNumber(Constants.PlayersAndTie.PLAYER_2);


							Playable game = new RockPaperScissors(p1, insanePlayer);
							game.playGame();
						} catch(Exception cnfe) {
							JOptionPane.showMessageDialog(null, "Failed to create opponent " +  DifficultyDialog.getPreviousWinnerName(), "Opponent Creation Error", JOptionPane.ERROR_MESSAGE);
							File insane_player_file = new File(Constants.FILES_FILEPATH + Constants.PREVIOUS_WINNER_FILE);   
							insane_player_file.delete();
						}
					}
				}
			} else {

				// flash window
				Thread t = new Thread(new WindowFlasher());
				t.start();
				errorTextArea.setText(compileResults);
			}
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | IOException ex) {
			ex.printStackTrace();
		}
	}

	class WindowFlasher implements Runnable {

		@Override
		public void run() {
			codeTextArea.setBackground(ColorScheme.compile_flash);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			codeTextArea.setBackground(ColorScheme.code_area_background);					
		}

	}

}
