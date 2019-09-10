

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

/**
 * @author martin
 * Draws a graph representing play history and results.
 */
public class BarGraph extends JComponent implements MouseMotionListener {

	private static final long serialVersionUID = 1L;

	// [RPS][WTL] rockW, rockT, rockL, paperW, paperT, paperL, scissorsW, scissorsT, scissorsL
	private int [][] stats_RPS_WinTieLossTotal = null;  	
	private Rectangle2D.Double rockWin, rockTie, rockLose, paperWin, paperTie, paperLose, scissorsWin, scissorsTie, scissorsLose;
	
	public BarGraph(int[][] s) {
		
		ToolTipManager.sharedInstance().setInitialDelay(0);

		stats_RPS_WinTieLossTotal = s;

		addMouseMotionListener(this);

		rockWin = new Rectangle2D.Double();
		rockTie = new Rectangle2D.Double();
		rockLose = new Rectangle2D.Double();
		paperWin = new Rectangle2D.Double();
		paperTie = new Rectangle2D.Double();
		paperLose = new Rectangle2D.Double();
		scissorsWin = new Rectangle2D.Double();
		scissorsTie = new Rectangle2D.Double();
		scissorsLose = new Rectangle2D.Double();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		int columnWidth = this.getWidth() / 3;
		int panelHeight = this.getHeight();

		// Labels (Rock/Paper/Scissors)
		g2.setColor(ColorScheme.bargraphlabeltext);

		Font font = new Font("Arial", Font.PLAIN, 24);
		FontMetrics fontMetrics = g2.getFontMetrics(font);
		g2.setFont(font);

		String columnLabelRPS = "Rock: " + NumberFormat.getNumberInstance(Locale.US).format(stats_RPS_WinTieLossTotal[0][3]);
		int labelXPos = (columnWidth - fontMetrics.stringWidth(columnLabelRPS)) / 2;
		g2.drawString(columnLabelRPS, labelXPos, panelHeight- 5);

		columnLabelRPS = "Paper: " + NumberFormat.getNumberInstance(Locale.US).format(stats_RPS_WinTieLossTotal[1][3]);
		labelXPos = (columnWidth - fontMetrics.stringWidth(columnLabelRPS)) / 2;
		g2.drawString(columnLabelRPS, columnWidth+labelXPos, panelHeight - 5);

		columnLabelRPS = "Scissors: " + NumberFormat.getNumberInstance(Locale.US).format(stats_RPS_WinTieLossTotal[2][3]);
		labelXPos = (columnWidth - fontMetrics.stringWidth(columnLabelRPS)) / 2;
		g2.drawString(columnLabelRPS, columnWidth*2+labelXPos, panelHeight - 5);


		g2.drawLine(0, panelHeight - 25, this.getWidth(), panelHeight- 25);

		
		
		// draw vertical lines to split columns
		for(int i = 1; i < 3; i++) {
			g2.drawLine(columnWidth*i, this.getHeight(), columnWidth*i, 0);			
		}

		// Bar Graphs
		// ROCK
		g2.setColor(ColorScheme.won);
		double winHeight = (double)panelHeight * (double)stats_RPS_WinTieLossTotal[0][0] / (double)RockPaperScissors.getCurrentGameNumber();
		rockWin.setRect(new Rectangle2D.Double(columnWidth/4, panelHeight - 25 - winHeight, columnWidth/2, winHeight));
		g2.fill(rockWin);

		g2.setColor(ColorScheme.tied);
		double tieHeight = (double)panelHeight *  (double)stats_RPS_WinTieLossTotal[0][1] / (double)RockPaperScissors.getCurrentGameNumber();
		rockTie.setRect(new Rectangle2D.Double(columnWidth/4, panelHeight - 25 - winHeight - tieHeight, columnWidth/2, tieHeight));
		g2.fill(rockTie);

		g2.setColor(ColorScheme.lost);
		double lossHeight = (double)panelHeight *  (double)stats_RPS_WinTieLossTotal[0][2] / (double)RockPaperScissors.getCurrentGameNumber();
		rockLose.setRect(new Rectangle2D.Double(columnWidth/4, panelHeight - 25 - winHeight - tieHeight - lossHeight, columnWidth/2, lossHeight));
		g2.fill(rockLose);

		// PAPER
		g2.setColor(ColorScheme.won);
		winHeight = (double)panelHeight * (double)stats_RPS_WinTieLossTotal[1][0] / (double)RockPaperScissors.getCurrentGameNumber();
		paperWin.setRect(new Rectangle2D.Double(columnWidth+columnWidth/4, panelHeight - 25 - winHeight, columnWidth/2, winHeight));
		g2.fill(paperWin);

		g2.setColor(ColorScheme.tied);
		tieHeight = (double)panelHeight *  (double)stats_RPS_WinTieLossTotal[1][1] / (double)RockPaperScissors.getCurrentGameNumber();
		paperTie.setRect(new Rectangle2D.Double(columnWidth+columnWidth/4, panelHeight - 25 - winHeight - tieHeight, columnWidth/2, tieHeight));
		g2.fill(paperTie);

		g2.setColor(ColorScheme.lost);
		lossHeight = (double)panelHeight *  (double)stats_RPS_WinTieLossTotal[1][2] / (double)RockPaperScissors.getCurrentGameNumber();
		paperLose.setRect(new Rectangle2D.Double(columnWidth+columnWidth/4, panelHeight - 25 - winHeight - tieHeight - lossHeight, columnWidth/2, lossHeight));
		g2.fill(paperLose);

		// SCISSORS
		g2.setColor(ColorScheme.won);
		winHeight = (double)panelHeight * (double)stats_RPS_WinTieLossTotal[2][0] / (double)RockPaperScissors.getCurrentGameNumber();
		scissorsWin.setRect(new Rectangle2D.Double(columnWidth*2+columnWidth/4, panelHeight - 25 - winHeight, columnWidth/2, winHeight));
		g2.fill(scissorsWin);

		g2.setColor(ColorScheme.tied);
		tieHeight = (double)panelHeight *  (double)stats_RPS_WinTieLossTotal[2][1] / (double)RockPaperScissors.getCurrentGameNumber();
		scissorsTie.setRect(new Rectangle2D.Double(columnWidth*2+columnWidth/4, panelHeight - 25 - winHeight - tieHeight, columnWidth/2, tieHeight));
		g2.fill(scissorsTie);

		g2.setColor(ColorScheme.lost);
		lossHeight = (double)panelHeight *  (double)stats_RPS_WinTieLossTotal[2][2] / (double)RockPaperScissors.getCurrentGameNumber();
		scissorsLose.setRect(new Rectangle2D.Double(columnWidth*2+columnWidth/4, panelHeight - 25 - winHeight - tieHeight - lossHeight, columnWidth/2, lossHeight));
		g2.fill(scissorsLose);
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public Point getToolTipLocation(MouseEvent e) {
		return new Point(e.getX(), e.getY()-20);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {

		getToolTipLocation(e);

		ToolTipManager.sharedInstance().setEnabled(true);
		
		if(rockWin.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[0][0] + " Rock Wins!");
			return;
		} else if(rockTie.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[0][1] + " Rock Ties!");
			return;
		} else if(rockLose.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[0][2] + " Rock Losses!");
			return;
		} else if(paperWin.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[1][0] + " Paper Wins!");
			return;
		} else if(paperTie.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[1][1] + " Paper Ties!");
			return;
		} else if(paperLose.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[1][2] + " Paper Losses!");
			return;
		} else if(scissorsWin.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[2][0] + " Scissor Wins!");
			return;
		} else if(scissorsTie.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[2][1] + " Scissor Ties!");
			return;
		} else if(scissorsLose.contains(e.getPoint())) {
			this.setToolTipText(stats_RPS_WinTieLossTotal[2][2] + " Scissors Losses!");
			return;
		}
		
		ToolTipManager.sharedInstance().setEnabled(false);
	}
}