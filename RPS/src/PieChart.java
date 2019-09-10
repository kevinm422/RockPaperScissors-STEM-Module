
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * Creates a Pie Chart with swing components
 * @author martin
 *
 */
public class PieChart extends JComponent {

	private static final long serialVersionUID = 1L;
	// ties, player 1, player 2, numGames
	private Part[] slices = { new Part(0, ColorScheme.tieColor), new Part(0,  ColorScheme.left_column), new Part(0, ColorScheme.right_column) }; 
	private int[] score_Ties_p1Wins_p2Wins;

	PieChart(int[] score) {
		this.score_Ties_p1Wins_p2Wins = score;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		for(int i = 0; i < 3; i++) {
			slices[i].value = (double)(((double)score_Ties_p1Wins_p2Wins[i] / (double)RockPaperScissors.getCurrentGameNumber())*100);
		}
		Rectangle2D.Double area = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
	
		// background circle
		g2.setColor(ColorScheme.button_text);
		g2.fill(new Ellipse2D.Double(5, 5, (int)area.getWidth()-10, (int)area.getHeight()-10));
		drawPie((Graphics2D) g, area, slices);

		Toolkit.getDefaultToolkit().sync();
	}
	
	private void drawPie(Graphics2D g, Rectangle2D.Double area, Part[] slices) {
		double total = 0.0D;
		for (int i = 0; i < slices.length; i++) {
			total += slices[i].value;
		} 
		double curValue = 0.0D;
		int startAngle = 0;
		for (int i = 0; i < slices.length; i++) {
			startAngle = (int) (curValue * 360 / total);
			int arcAngle = (int) (slices[i].value * 360 / total);

			g.setColor(slices[i].color);

			g.fillArc(10, 10, (int)area.getWidth()-20, (int)area.getHeight()-20, startAngle, arcAngle);

			curValue += slices[i].value;
		} 
	}	
	
	private class Part {
		double value;
		Color color;

		public Part(double value, Color color) {
			this.value = value;
			this.color = color;
		}
	}
}