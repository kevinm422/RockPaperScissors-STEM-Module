
import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import rps.Constants;

public class ColorScheme {

	// Rock Paper Scissors GUI
	public static Color left_column = new Color(0xD5AD36); // gold
	public static Color centerColumnBackground = new Color(0x000080);	// NAVY
	public static Color right_column = new Color(0x1B73D3); // blue
	public static Color column_border = new Color(0xC0C0C0); // Silver
	public static Color playerScoreText = Color.black;
	public static Color tieColor = Color.white;
	
	// Bar Graph
	public static Color bargraphlabeltext = Color.white;
	public static Color won = Color.green;
	public static Color tied = Color.yellow;
	public static Color lost = Color.red;
	public static Color handPanelDarkToggle = new Color(0x999999); // Silver

	// Student GUI
	public static Color compile_flash = new Color(0xf4c0c0); // compile_failed_alert
	public static Color code_area_background = Color.white; 

	// Instructor GUI
	public static Color compiled = Color.green;
	public static Color modified = Color.yellow;
	public static Color failedCompile = Color.red;

	// Buttons
	public static Color button_text = new Color(0xC0C0C0); // Silver
	public static Color button_background = new Color(0x1B73D3); // Blue


	public static void loadColorScheme() {

		try {

			Scanner s = new Scanner(new FileReader(Constants.FILES_FILEPATH + Constants.COLOR_FILE));

			while(s.hasNextLine()) {
				String colorLabel = s.nextLine();
				if(colorLabel.length() > 1) {
					if(colorLabel.charAt(0) == '#') {

						colorLabel = colorLabel.substring(2);

						String color = s.nextLine();
						int r = Integer.valueOf( color.substring( 2, 4 ), 16 );
						int g = Integer.valueOf( color.substring( 4, 6 ), 16 );
						int b = Integer.valueOf( color.substring( 6, 8 ), 16 );

						switch(colorLabel) {
						case "LeftColumn":
							left_column = new Color(r, g, b);
							break;
						case "RightColumn":
							right_column = new Color(r, g, b);
							break;
						case "PlayerScoreText":
							playerScoreText = new Color(r, g, b);
							break;
						case "TieColor":
							tieColor = new Color(r, g, b);
							break;
						case "BarGraphLabelText":
							bargraphlabeltext = new Color(r, g, b);
							break;
						case "Won":
							won = new Color(r, g, b);
							break;
						case "Tied":
							tied = new Color(r, g, b);
							break;
						case "Lost":
							lost = new Color(r, g, b);
							break;
						case "Compiled":
							compiled = new Color(r, g, b);
							break;
						case "Modified":
							modified = new Color(r, g, b);
							break;
						case "FailedCompile":
							failedCompile = new Color(r, g, b);
							break;
						case "ButtonText":
							button_text = new Color(r, g, b);
							break;
						case "Dark":
							handPanelDarkToggle = new Color(r, g, b);
							break;
						case "ButtonBackground":
							button_background = new Color(r, g, b);
							break;
						case "CenterColumnBackground":
							centerColumnBackground = new Color(r, g, b);
							break;
						}
					}
				}
			}
			s.close();

		} catch(IOException e) {
			Constants.log("Couldn't open color file: " + Constants.FILES_FILEPATH + Constants.COLOR_FILE);
		}
	}
}
