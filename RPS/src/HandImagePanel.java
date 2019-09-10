
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import rps.Constants;
import rps.Moves.Move;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class HandImagePanel extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage[] handsAndResultPNGs = new BufferedImage[5]; // R, P, S, W, L
	private int currentHandImageIndex = 0;

	public HandImagePanel(Constants.PlayersAndTie p) {
		loadImagesBasedOnColumn(p);
	}

	public void drawHand(Move hand) {
		switch(hand) {
		case ERROR:
			currentHandImageIndex = 4;
			break;
		case PAPER:
			currentHandImageIndex = 1;
			break;
		case ROCK:
			currentHandImageIndex = 0;
			break;
		case SCISSORS:
			currentHandImageIndex = 2;
			break;
		}
	}

	public void drawMatchResult(Constants.RoundResult result) {
		switch(result) {
		case LOSS:
			currentHandImageIndex = 4;
			break;
		case WIN:
			currentHandImageIndex = 3;
			break;
		default:
			currentHandImageIndex = 4;
			break;
		}
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D)g;

		if(RockPaperScissors.getCurrentGameNumber() != Constants.TOTAL_NUMBER_OF_GAMES) {
			if(RockPaperScissors.getCurrentGameNumber() % 2 == 0) {
				g2.setColor(ColorScheme.button_text);
			} else {
				g2.setColor(ColorScheme.handPanelDarkToggle);
			}
			g2.fill(new Rectangle(35, this.getHeight()/8-15, this.getWidth()-100, (int)(this.getHeight()/1.3)));
		}

		g2.drawImage(handsAndResultPNGs[currentHandImageIndex], 50, this.getHeight()/8, this.getWidth()-100, (int)(this.getHeight()/1.3), null, null);
	}

	private void loadImagesBasedOnColumn(Constants.PlayersAndTie p) {
		try {
			URL imageURL = null;
			if(p == Constants.PlayersAndTie.PLAYER_1) {

				imageURL = RockPaperScissors.class.getResource("left_rock.jpg");
				handsAndResultPNGs[0] = ImageIO.read(imageURL);

				imageURL = RockPaperScissors.class.getResource("left_paper.jpg");
				handsAndResultPNGs[1] = ImageIO.read(imageURL);

				imageURL = RockPaperScissors.class.getResource("left_scissors.jpg");
				handsAndResultPNGs[2] = ImageIO.read(imageURL);
			} else if(p == Constants.PlayersAndTie.PLAYER_2) {

				imageURL = RockPaperScissors.class.getResource("right_rock.jpg");
				handsAndResultPNGs[0] = ImageIO.read(imageURL);

				imageURL = RockPaperScissors.class.getResource("right_paper.jpg");
				handsAndResultPNGs[1] = ImageIO.read(imageURL);

				imageURL = RockPaperScissors.class.getResource("right_scissors.jpg");
				handsAndResultPNGs[2] = ImageIO.read(imageURL);
			}

			imageURL = RockPaperScissors.class.getResource("trophy.png");
			handsAndResultPNGs[3]= ImageIO.read(imageURL);

			imageURL = RockPaperScissors.class.getResource("red_X.png");
			handsAndResultPNGs[4] = ImageIO.read(imageURL);

		} catch (IOException e) {
			Constants.log("Error loading hand images.");
		}
	}
}
