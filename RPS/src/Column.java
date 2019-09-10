


import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public abstract class Column extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JLabel scoreLabel;
	private String labelName;
	
	protected void setupLabel(String playerName) {
		
		labelName = playerName;
		
		setBorder(new LineBorder(ColorScheme.column_border, 5));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		
		scoreLabel = new JLabel(labelName + ": 0");
		scoreLabel.setForeground(ColorScheme.playerScoreText);
		
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		scoreLabel.setFont(new Font("Arial", Font.PLAIN, 45));
		add(scoreLabel);
	}

	public void updateLabelTotal(int winsOrTies) {
		scoreLabel.setText(labelName + ": " + addCommaToNumber(winsOrTies));
	}
	
	private String addCommaToNumber(int i) {
		return NumberFormat.getNumberInstance(Locale.US).format(i);
	}
	
	
	

	
}
