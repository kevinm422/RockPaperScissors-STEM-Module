
import javax.swing.*;
import rps.Constants;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

public class CreatePlayer implements ActionListener {

	private RSyntaxTextArea textArea;

	public CreatePlayer(RSyntaxTextArea t) {
		textArea = t;
	}

	public void actionPerformed(ActionEvent e) {

		Student.allowCreate(false); // don't allow double clicks

		JDialog enterNameFrame = new JDialog();
		enterNameFrame.setResizable(false);
		enterNameFrame.setTitle("Enter team name:");
		enterNameFrame.setAlwaysOnTop(true);
		JTextField nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(360,0));

		enterNameFrame.add(nameField, BorderLayout.WEST);

		Button submitButton = new Button("Enter");
		nameField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					submitButton.doClick();
					return;
				}

				if(nameField.getText().length() >= 15) {
					enterNameFrame.setTitle("Names must be between 1 and 15 characters!");
					nameField.setText(nameField.getText().substring(0,14));
					return;
				}

				String original = nameField.getText();
				String sanitized = original.replaceAll("[^a-zA-Z0-9]", "");
				if(original.compareTo(sanitized) != 0) {
					enterNameFrame.setTitle("Names can only contain alphanumerics!");
					return;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) { }

			@Override
			public void keyTyped(KeyEvent e) { }
		});

		submitButton.addActionListener(new SubmitNameButtonListener(enterNameFrame, nameField));
		enterNameFrame.add(submitButton, BorderLayout.EAST);
		enterNameFrame.pack();
		DifficultyDialog.showOnScreen(0, enterNameFrame);

		enterNameFrame.setVisible(true);
	}

	private class SubmitNameButtonListener implements ActionListener {

		private JDialog enterNameFrame = null;
		private JTextField nameField = null;
		private int submitsRemainingToReuseName = 5;

		public SubmitNameButtonListener(JDialog f, JTextField nameField) {
			this.enterNameFrame = f;
			this.nameField = nameField;
		}

		public void actionPerformed(ActionEvent ae) {
			String name = nameField.getText();

			// Remove non alphanumerics
			name = name.replaceAll("[^a-zA-Z0-9]", "");

			// Verify name length
			if(name.length() > 15 || name.length() < 1) {
				enterNameFrame.setTitle("Names must be between 1 and 15 characters!");

				// check if name starts with a letter
			} else if (!name.matches("^[a-zA-Z].*$")) { 
				enterNameFrame.setTitle("Names must start with a letter!");
			} else if(new File(Constants.PLAYERS_FILEPATH + name + ".java").exists()) {
				enterNameFrame.setTitle("\"" + name + "\" is already in use!");
				if(submitsRemainingToReuseName-- <= 0) {
					loadExistingPlayer(name);
				}
			} else {
				if(Student.getName() == null) {
					createNewPlayer(name);
				} else {
					renamePlayer(name);
				}
			}
		}

		private void createNewPlayer(String name) {

			Student.setName(name);

			final String STARTING_CODE = "import static rps.Moves.Move.*;\nimport rps.Moves.Move;\n\npublic class %CLASSNAME% extends Player { // brace A\n\n\tpublic Move getMove() { // brace B\n\t//vvvvvvvvvvvvvvvvvvvvvvvv WORK HERE vvvvvvvvvvvvvvvvvvvvvvvv\n\n\n\n\n\n\n\n\n\t\treturn ROCK;\n\t//^^^^^^^^^^^^^^^^^^^^^^^^^ WORK HERE ^^^^^^^^^^^^^^^^^^^^^^^^^\n\t} // don't delete - Matches with brace B above\n} // don't delete - Matches with brace A above\n";
			String code = STARTING_CODE.replace("%CLASSNAME%", name);

			writeFile(Constants.PLAYERS_FILEPATH + name + ".java", code);

			textArea.setText(code);
			finalizeCreation();
		}

		private void loadExistingPlayer(String name) {
			Student.setName(name);

			// load code from open file
			String code = loadFile(Constants.PLAYERS_FILEPATH + name + ".java");

			textArea.setText(code);

			finalizeCreation();
		}

		private void renamePlayer(String name) {
			String code = textArea.getText();

			code = code.replace(Student.getName(), name);

			Student.setName(name);

			writeFile(Constants.PLAYERS_FILEPATH + name + ".java", code);

			textArea.setText(code);
			finalizeCreation();
		}

		private String loadFile(String filename) {
			String fileContents = "";
			Scanner sc = null;
			try {
				sc = new Scanner(new FileReader(filename));
				while(sc.hasNext()) {
					fileContents += sc.nextLine() + "\n";
				}
				sc.close();

			} catch (IOException e) {
				Constants.log("Couldn't load file " + filename + "\n" + e.getMessage());	
			}
			return fileContents;
		}

		private void writeFile(String filepath, String fileContents) {
			try {
				PrintWriter pw = new PrintWriter(new File(filepath));
				pw.print(fileContents);
				if (pw != null) {
					pw.close();
				}
			} catch (FileNotFoundException fnfe) {
				Constants.log("Could not create " + filepath);
				JOptionPane.showMessageDialog(null, "Could not write " + filepath + ". Check file permissions.", "Alert!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}

		private void finalizeCreation() {
			Student.setCreateButtonText("Change Name");
			Student.allowCreate(true);

			textArea.setEditable(true);
			textArea.requestFocus();
			Student.allowTest(true);
			Student.allowSubmit(true);
			enterNameFrame.dispose();
		}
	}
}
