
import rps.Constants;
import java.io.BufferedReader;
import java.io.FileReader;
public class CodeLength {

	private int lines = 0, characters = 0;

	public CodeLength(String name) {


		if(name.compareTo("AI.java") == 0) {
			lines = 50;
			characters = 150;
			return;
		}

		String filename = verifyFilenameExtension(name);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(Constants.PLAYERS_FILEPATH + filename));
			String line;
			while ((line = reader.readLine()) != null) {
				characters += line.length();
				lines++;
			}
			reader.close();
		} catch (Exception e) {
			Constants.log("Couldn't read lines/characters in file " + Constants.PLAYERS_FILEPATH + filename);
		}

	}

	public int getLines() {
		return lines;
	}
	public int getCharacters() {
		return characters;
	}

	private String verifyFilenameExtension(String name) {
		if(name.endsWith(".java")) {
			return name;			
		}
		return name + ".java";
	}

}
