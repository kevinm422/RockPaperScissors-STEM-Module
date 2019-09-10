
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JOptionPane;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import rps.Constants;

public class SubmitCode implements ActionListener {

	private RSyntaxTextArea codeArea;

	public SubmitCode(RSyntaxTextArea c) {
		codeArea = c;
	}
	public void actionPerformed(ActionEvent e) {

		if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog (null, "Are you sure you're ready to submit?\nSubmitting closes the program.","Confirm Submit", JOptionPane.YES_NO_OPTION)) {

			String name = Student.getName();

			Constants.createRosterIfMissing();

			if(!Constants.isNameInRoster(name)) {

				saveFile(name);

				// COMPILE FILE
				String compileResults = CompileCode.compile(name + ".java");
				if(compileResults.equals("")) {
					
					// ADD NAME TO ROSTER
					Constants.addNameToRoster(name);
					showSubmitConfirmation();

				} else {
					JOptionPane.showMessageDialog(null, "Failed to submit. Code did not compile.", "Submit error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				showSubmitConfirmation(); // name was already in roster
			}
		}
	}
	
	private void saveFile(String name) {
		// SAVE FILE
		String source = codeArea.getText();

		File root = new File(Constants.PLAYERS_FILEPATH); // .. is "rps"
		File sourceFile = new File(root, name + ".java");
		sourceFile.getParentFile().mkdirs();

		try {
			Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e1) {
			Constants.log("Couldn't write " + name + ".java on submit.");
		}
	}
	
	private void showSubmitConfirmation() {
		JOptionPane.showMessageDialog(null, "You're entered into the tournament!\nGood luck!", "Submit Successful", JOptionPane.PLAIN_MESSAGE);			
		System.exit(0);
	}
}
