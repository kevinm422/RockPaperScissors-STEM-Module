
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import rps.Constants;

/**
 * Creates a grid with game results on mouse over.
 * @author martin
 *
 */
public class Grid extends JComponent implements MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage gridImage;
	private Graphics2D gridGraphics;
	private int box_width, box_height, x_margin, y_margin;
	private int side_length; // prevents hard coding 100 (for 10,000 game)

	public Grid(Dimension size) {
		
		side_length = (int) Math.sqrt(Constants.TOTAL_NUMBER_OF_GAMES);
		addMouseMotionListener(this);
		
		ToolTipManager.sharedInstance().setInitialDelay(0);
		
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();

		box_width = width / side_length;
		box_height = height / side_length;

		x_margin = (width - (box_width * side_length)) / 2;
		y_margin = (height - (box_height * side_length)) / 4;

		gridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		gridGraphics = gridImage.createGraphics();

		gridGraphics.setColor(ColorScheme.centerColumnBackground);
		gridGraphics.fillRect(0, 0, gridImage.getWidth(), gridImage.getHeight());
	}

	public void addSquareToBufferedImage(Constants.PlayersAndTie winner) {
		switch(winner) {
		case PLAYER_1:
			gridGraphics.setColor(ColorScheme.left_column);
			break;
		case PLAYER_2:
			gridGraphics.setColor(ColorScheme.right_column);
			break;
		case TIE:
			gridGraphics.setColor(ColorScheme.tieColor);
			break;
		}

		int x = (int) ((RockPaperScissors.getCurrentGameNumber() % side_length) * box_width);
		int y = Math.floorDiv(RockPaperScissors.getCurrentGameNumber(), side_length) * box_height;
		gridGraphics.fillRect(x_margin + x, y_margin + y, box_width, box_height);

	}

	public void paintComponent(Graphics g) {
//		g.setColor(Color.red);
//		g.fillRect(0,0, width, height);
		g.drawImage(gridImage, 0, 0, null);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public Point getToolTipLocation(MouseEvent e) {
		return new Point(e.getX(), e.getY()-50);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		getToolTipLocation(e);

		ToolTipManager.sharedInstance().setEnabled(true);

		int col = (e.getX() - 15) / box_width;
		int row = (e.getY() - 0) / box_height;
		int index = row * side_length + col;

		if(index < RockPaperScissors.getCurrentGameNumber() && index >= 0) {
			String text = "<html>";
			text += "<b>Game:</b> " + index;
			switch(RockPaperScissors.getGameResult(index)) {
			case PLAYER_1:
				text += "<br><b style=\"color:green;\">" + RockPaperScissors.getPlayerName(rps.Constants.PlayersAndTie.PLAYER_1) + "'s move " + RockPaperScissors.getGameMove(index, rps.Constants.PlayersAndTie.PLAYER_1).toString() + "</b>";
				text += "<br>" + RockPaperScissors.getPlayerName(rps.Constants.PlayersAndTie.PLAYER_2) + "'s move " + RockPaperScissors.getGameMove(index, rps.Constants.PlayersAndTie.PLAYER_2).toString();
				break;
			case PLAYER_2:
				text += "<br>" + RockPaperScissors.getPlayerName(rps.Constants.PlayersAndTie.PLAYER_1) + "'s move " + RockPaperScissors.getGameMove(index, rps.Constants.PlayersAndTie.PLAYER_1).toString();
				text += "<br><b style=\"color:green;\">" + RockPaperScissors.getPlayerName(rps.Constants.PlayersAndTie.PLAYER_2) + "'s move " + RockPaperScissors.getGameMove(index, rps.Constants.PlayersAndTie.PLAYER_2).toString() + "</b>";
				break;
			case TIE:
				text += "<br>" + RockPaperScissors.getPlayerName(rps.Constants.PlayersAndTie.PLAYER_1) + "'s move " + RockPaperScissors.getGameMove(index, rps.Constants.PlayersAndTie.PLAYER_1).toString();
				text += "<br>" + RockPaperScissors.getPlayerName(rps.Constants.PlayersAndTie.PLAYER_2) + "'s move " + RockPaperScissors.getGameMove(index, rps.Constants.PlayersAndTie.PLAYER_2).toString();
				break;
			default:
				break;
			}
			
			text += "</html>";
			this.setToolTipText(text);	
			return;
		}

		ToolTipManager.sharedInstance().setEnabled(false);

	}
}
