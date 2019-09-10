import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import rps.Constants;

import java.awt.event.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.io.*;
import java.util.*;
import java.awt.*;

public class Instructor {


	private volatile Button createTournamentButton;
	private Button compileButton;
	private Button addPlayer;
	private Button cleanSession;
	private JPanel name_panel;
	private JPanel buttons;
	private JFrame frame;

	private volatile ArrayList<PlayerRow> playerRows;

	public static void main(String[] args) {
		new Instructor();
	}

	private Instructor() {
		init();
	}

	private void init() {

		frame = new JFrame("RPS Instructor Panel");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(960, 1080); // half screen

		frame.setIconImage(Constants.getIcon());

		playerRows = new ArrayList<PlayerRow>();

		loadButtons();
		createNamePanel();

		frame.add(new JScrollPane(name_panel), BorderLayout.CENTER);
		frame.add(buttons, BorderLayout.SOUTH);

		new DetectFileChange().start();

		frame.setVisible(true);
	}

	public class DetectFileChange extends Thread {

		public void run() {

			updatePlayerList();

			try {
				WatchService watchService = FileSystems.getDefault().newWatchService();

				Path sourcecodeFilePath = Paths.get(Constants.FILES_FILEPATH);
				Path playerRosterFilePath = Paths.get(Constants.PLAYERS_FILEPATH);

				sourcecodeFilePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
				playerRosterFilePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

				WatchKey key;
				while (true) {
					key = watchService.take();
					for (WatchEvent<?> event : key.pollEvents()) {

						if(event.context().toString().equals(Constants.ROSTER_FILE)) {
							updatePlayerList();
						}
						for(PlayerRow r : playerRows) {
							if(event.context().toString().equals(r.getName() + ".java")) {
								r.setButtonColor(ColorScheme.modified);
							}
						}
					}
					key.reset();
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void updatePlayerList() {

			Constants.createRosterIfMissing();

			createPlayerRowsFromRoster();

			name_panel.removeAll();
			for(PlayerRow i : playerRows) {
				name_panel.add(i);
				i.repaint();
			}

			compileButton.setText("Compile " + playerRows.size() + " Players");
			name_panel.revalidate();
			name_panel.repaint();
		}
		private void createPlayerRowsFromRoster() {
			try {
				Scanner s = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.ROSTER_FILE));
				playerRows.clear();
				while(s.hasNextLine()){
					String line = s.nextLine();
					if(!line.equals("")) {
						playerRows.add(new PlayerRow(line));
					}
				}
				s.close();		
			} catch(IOException e) {
				Constants.log("Couldn't open " + Constants.ROSTER_FILE);
			}
		}
	}


	public class PlayerRow extends JPanel {

		private static final long serialVersionUID = 1L;
		private String playerName;
		private Button remove_button;
		private volatile Button open_source_button;

		public PlayerRow(String name) {
			playerName = name;

			setPreferredSize(new Dimension(frame.getWidth(), 70));
			setMaximumSize( getPreferredSize() );
			setLayout(new BorderLayout());

			remove_button = new Button("X");
			remove_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a) {
					try {
						// READ EXISTING
						Scanner s = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.ROSTER_FILE));

						String file_content = "";

						while(s.hasNextLine()){
							String line = s.nextLine();
							if(!line.equals(open_source_button.getText())) {
								file_content += line + "\n";
							}
						}
						s.close();

						// WRITE NEW WITHOUT REMOVED PLAYER
						PrintWriter pw = new PrintWriter(new File(Constants.FILES_FILEPATH + Constants.ROSTER_FILE));
						pw.write(file_content);
						if (pw != null) pw.close();	

					} catch(FileNotFoundException e) {
						Constants.log("Couldn't read/write to roster file: " + Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
					}
				}
			});

			open_source_button = new Button(playerName);
			open_source_button.setFont(new Font("Arial", Font.PLAIN, 40));
			open_source_button.setBorder(new LineBorder(ColorScheme.column_border, 3));
			open_source_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						// opens file in OS's default file editor
						java.awt.Desktop.getDesktop().open(new File(Constants.PLAYERS_FILEPATH + playerName + ".java"));
					} catch (IOException ioe) {
						Constants.log("Unable to open " + Constants.PLAYERS_FILEPATH + playerName + ".java with default editor.");
					}
				}
			});

			add(open_source_button, BorderLayout.CENTER);
			add(remove_button, BorderLayout.EAST);
		}

		public String getName() {
			return playerName;
		}

		private void setButtonColor(Color c) {
			open_source_button.setBorder(new LineBorder(c, 10));
		}


	}

	private void loadButtons() {

		buttons = new JPanel();
		buttons.setLayout(new GridLayout(0,1));

		createAddPlayerButton();
		createCompileButton();
		createCreateTournamentButton();

		createClearSessionButton();
	}

	private void createNamePanel() {
		name_panel = new JPanel();
		name_panel.setBackground(ColorScheme.right_column);
		name_panel.setLayout(new BoxLayout(name_panel, BoxLayout.Y_AXIS));
	}

	private void createAddPlayerButton() {

		addPlayer = new Button("Add Player");
		addPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JFileChooser fc = new JFileChooser(new File(Constants.PLAYERS_FILEPATH));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".java", "java", "text");
		fc.setFileFilter(filter);

		addPlayer.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String name = file.getName().split(".java")[0];

					boolean nameAlreadyExistsInRoster = false;
					for(PlayerRow r : playerRows) {
						if(r.getName().equals(name)) {
							nameAlreadyExistsInRoster = true;
						}
					}

					if(!nameAlreadyExistsInRoster) {
						if(file.getName().indexOf(".java") > 1) { // ensure it's a java file
							if(!new File(Constants.PLAYERS_FILEPATH + name + ".java").exists()) {							
								copyFileToPlayersFilePath(file);						
							}
							// Check again that file is there before adding to roster
							if(new File(Constants.PLAYERS_FILEPATH + name + ".java").exists()) {		
								Constants.addNameToRoster(name);	
							}
						}
					}
				}
			}

			private void copyFileToPlayersFilePath(File file) {
				try {
					Path src = FileSystems.getDefault().getPath(file.getPath());
					Path dst = FileSystems.getDefault().getPath(Constants.PLAYERS_FILEPATH, file.getName());
					Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					Constants.log("Wasn't able to copy " + file.getPath() + " to " + Constants.PLAYERS_FILEPATH);
				}
			}
		});
		buttons.add(addPlayer);
	}

	private void createCompileButton() {

		compileButton = new Button("Compile " + playerRows.size() + " Players");
		compileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		compileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				createTournamentButton.setEnabled(true);

				for(PlayerRow r : playerRows) {
					String compileResults = CompileCode.compile(r.getName() + ".java");
					if(compileResults.equals("")) {
						r.setButtonColor(ColorScheme.compiled);
					} else {
						popupFailedToCompileAlert(r.getName(), compileResults);
						r.setButtonColor(ColorScheme.failedCompile);
						createTournamentButton.setEnabled(false);
					}
				}
			}
		});
		buttons.add(compileButton);
	}
	
	private void createCreateTournamentButton() {

		createTournamentButton = new Button("Start Tournament");
		createTournamentButton.setEnabled(false);
		createTournamentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createTournamentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// prevents instructor frame from popping up between games (RPS is a dialog)
				frame.setVisible(false); 
				
				// create list of names
				ArrayList<String> players = new ArrayList<String>();
				for(PlayerRow row : playerRows) {
					players.add(row.getName());
				}
				new BracketTournament(players);
			}
		});

		buttons.add(createTournamentButton);
	}

	private void createClearSessionButton() {

		cleanSession = new Button("Clear Session");
		cleanSession.setEnabled(true);
		cleanSession.setAlignmentX(Component.CENTER_ALIGNMENT);
		cleanSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "This will move all player files and clear the tournament roster.","Clear Session?", JOptionPane.YES_NO_OPTION)) {

					JFileChooser chooser = new JFileChooser(); 
					chooser.setCurrentDirectory(new java.io.File(Constants.OLD_FILES_FILEPATH));
					chooser.setDialogTitle("Select destination for old player files.");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setAcceptAllFileFilterUsed(false);

					// if chose a destination for old source files
					if (chooser.showDialog(frame, "Select") == JFileChooser.APPROVE_OPTION) { 

						deleteRoster();

						// Read name of previous winner to keep that .java file
						String previousWinner = getPreviousWinner();

						// Move all .java and class files
						String destination = chooser.getSelectedFile().toString();
						if(destination.charAt(destination.length() - 1 ) != '/') {
							destination += "/";
						}

						// move *.java files to specified path
						File dir1 = new File(Constants.PLAYERS_FILEPATH);
						if(dir1.isDirectory()) {

							File[] content = dir1.listFiles();
							for(int i = 0; i < content.length; i++) {

								if(!content[i].getName().equals(previousWinner + ".class") && !content[i].getName().equals(previousWinner + ".java") ) {
									String newName = destination + content[i].getName();
									if(content[i].renameTo(new File(newName))) {
										content[i].delete();
									}
								} 
							}
						}
					}
				} // clicked cancel on destination
			}
			
			private String getPreviousWinner() {
				try {
					Scanner sc = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.PREVIOUS_WINNER_FILE));
					String winner = sc.next();
					sc.close();
					return winner;
				} catch (IOException ex) {
					Constants.log("Cannot read insane player file." + Constants.FILES_FILEPATH + Constants.PREVIOUS_WINNER_FILE);
				}
				return "";
			}
			
			private void deleteRoster() {
				File file = new File(Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
				if(!file.delete()){
					Constants.log("Failed to delete roster file when clearing session: " + Constants.FILES_FILEPATH + Constants.ROSTER_FILE);
				}
			}
		});

		buttons.add(cleanSession);
	}

	private void popupFailedToCompileAlert(String name, String errorMessage) {
		JTextArea compilerErrorsTextArea = new JTextArea();
		compilerErrorsTextArea.setSize(400, 300);
		compilerErrorsTextArea.setFont(new Font("Serif", Font.ITALIC, 16));
		compilerErrorsTextArea.setLineWrap(true);
		compilerErrorsTextArea.setEditable(false);
		compilerErrorsTextArea.setText(errorMessage);
		compilerErrorsTextArea.setWrapStyleWord(true);

		JFrame error_window = new JFrame(name + " - Compiler Errors:");
		error_window.setSize(400, 300);
		error_window.setLocation(960, 100);
		error_window.add(compilerErrorsTextArea);

		error_window.setVisible(true);
	}
}
