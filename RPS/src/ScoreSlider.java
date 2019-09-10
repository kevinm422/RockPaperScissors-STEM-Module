
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class ScoreSlider extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] score; // ties, player 1, player 2
	private final int X_MARGIN_LEFT = 30, X_MARGIN_RIGHT = 50, Y_MARGIN = 15, SMALL_RECTANGLE_HEIGHT = 60, LARGE_RECTANGLE_HEIGHT = 90;
	private final int BORDER_WIDTH = 5;
	
	public ScoreSlider(int[] s) {
		score = s;
	}
	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		
		double x = this.getWidth() / 2;

		// solve for slider x position
		if(score[1] + score[2] != 0) {
			x = (double)(score[1] - score[2]) / (double)(score[1] + score[2]);
			x = x * (this.getWidth()-60)/2;
			x += (this.getWidth())/2;
		}

		// small silver rectangle 
		g.setColor(ColorScheme.button_text);
		g.fill(new Rectangle2D.Double(X_MARGIN_LEFT, Y_MARGIN + 2 * BORDER_WIDTH, this.getWidth() - X_MARGIN_RIGHT, Y_MARGIN + SMALL_RECTANGLE_HEIGHT + 5 * BORDER_WIDTH));
		if(score[1] > score[2]) {
			// big silver rectangle 
			g.setColor(ColorScheme.button_text);
			g.fill(new Rectangle2D.Double(X_MARGIN_LEFT, Y_MARGIN, (int)x-20, Y_MARGIN + LARGE_RECTANGLE_HEIGHT + 3 * BORDER_WIDTH));

			// large rectangle
			g.setColor(ColorScheme.left_column);
			g.fill(new Rectangle2D.Double(X_MARGIN_LEFT + BORDER_WIDTH, Y_MARGIN + BORDER_WIDTH, (int)x - 30, Y_MARGIN + LARGE_RECTANGLE_HEIGHT + BORDER_WIDTH));
			
			// small rectangle
			g.setColor(ColorScheme.right_column);
			g.fill(new Rectangle2D.Double((int)x, Y_MARGIN + 3 * BORDER_WIDTH, this.getWidth()-(int)x - 30, Y_MARGIN + SMALL_RECTANGLE_HEIGHT + 3 * BORDER_WIDTH));
		} else if(score[1] < score[2]) {
			// big silver rectangle 
			g.setColor(ColorScheme.button_text);
			g.fill(new Rectangle2D.Double((int)x, Y_MARGIN, this.getWidth()-(int)x - 20, Y_MARGIN + LARGE_RECTANGLE_HEIGHT + 3 * BORDER_WIDTH));

			// small rectangle
			g.setColor(ColorScheme.left_column);
			g.fill(new Rectangle2D.Double(X_MARGIN_LEFT + BORDER_WIDTH, Y_MARGIN + 3 * BORDER_WIDTH, (int)x-30, Y_MARGIN + SMALL_RECTANGLE_HEIGHT + 3 * BORDER_WIDTH));
			
			// large rectangle
			g.setColor(ColorScheme.right_column);
			g.fill(new Rectangle2D.Double((int)x, Y_MARGIN + BORDER_WIDTH, this.getWidth()-(int)x - 30, Y_MARGIN + LARGE_RECTANGLE_HEIGHT + BORDER_WIDTH));
		} else { // TIED
			g.setColor(ColorScheme.left_column);
			g.fill(new Rectangle2D.Double(X_MARGIN_LEFT + BORDER_WIDTH, Y_MARGIN + 3 * BORDER_WIDTH, (int)x-30, Y_MARGIN + 3 * BORDER_WIDTH + SMALL_RECTANGLE_HEIGHT));
			g.setColor(ColorScheme.right_column);
			g.fill(new Rectangle2D.Double((int)x, Y_MARGIN + 3 * BORDER_WIDTH, this.getWidth()-(int)x - 30, Y_MARGIN + 3 * BORDER_WIDTH + SMALL_RECTANGLE_HEIGHT));
		}

		// draw center line
		g.setColor(ColorScheme.tieColor);
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		g.drawLine(this.getWidth() / 2, Y_MARGIN - 2 * BORDER_WIDTH, this.getWidth() / 2, Y_MARGIN + LARGE_RECTANGLE_HEIGHT + 8 * BORDER_WIDTH);
	}
}