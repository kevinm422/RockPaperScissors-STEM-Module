
import rps.Constants;
import rps.Moves.Move;

/**
 * 
 * @author martin
 *
 */
public class PlayerColumn extends Column {

	private static final long serialVersionUID = 1L;
	private HandImagePanel handImagePanel;
	
	public PlayerColumn(Constants.PlayersAndTie p, String name, int[][] stats) {

		setBackgroundColor(p);

		setupLabel(name);
	
		handImagePanel = new HandImagePanel(p);
		add(handImagePanel);

		add(new BarGraph(stats));
	}
	
	public void setMoveOnHandImagePanel(Move move) {
		handImagePanel.drawHand(move);
	}
	public void setResultOnHandImagePanel(Constants.RoundResult result) {
		handImagePanel.drawMatchResult(result);
	}

	private void setBackgroundColor(Constants.PlayersAndTie p) {
		if(p == Constants.PlayersAndTie.PLAYER_1) {
			this.setBackground(ColorScheme.left_column);
		} else if(p == Constants.PlayersAndTie.PLAYER_2) {
			this.setBackground(ColorScheme.right_column);
		}
	}
}