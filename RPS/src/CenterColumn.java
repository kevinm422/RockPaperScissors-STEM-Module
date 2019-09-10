
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rps.Constants;
import javax.swing.JPanel;

/**
 * Displays stats in the center of RockPaperScissors.
 * @author martin
 *
 */
public class CenterColumn extends Column {

	private static final long serialVersionUID = 1L;

	private PieChart pieChart;
	private ScoreSlider scoreSlider;
	private Grid grid;
	private JPanel buttonPanel;
	private Button startPauseButton, slowSpeedButton, mediumSpeedButton, fastSpeedButton, finishButton;
	private Button exitButton;

	public CenterColumn(int[] score, Button exit) {

		setBackground(ColorScheme.centerColumnBackground);

		setupLabel("Ties");
		scoreLabel.setForeground(ColorScheme.button_text);

		// w / h
		// TODO allow for different screen resolutions - relies on 1920/1080
		Dimension dp = new Dimension(630, 160);
		scoreSlider = new ScoreSlider(score);
		scoreSlider.setPreferredSize(dp);
		scoreSlider.setMaximumSize(dp);
		add(scoreSlider);

		dp = new Dimension(630, 505);
		grid = new Grid(dp);
		grid.setPreferredSize(dp);
		grid.setMaximumSize(dp);
		add(grid);

		dp = new Dimension(630, 320);
		pieChart = new PieChart(score);
		pieChart.setPreferredSize(dp);
		pieChart.setMaximumSize(dp);
		add(pieChart);

		setupButtons();

		exitButton = exit;
		setupExitButton(exitButton);
		add(buttonPanel);
	}

	public void addGridSquareToImage(Constants.PlayersAndTie winner) {
		grid.addSquareToBufferedImage(winner);
	}	

	public void showExitButton() {
		startPauseButton.setVisible(false);
		slowSpeedButton.setVisible(false);
		mediumSpeedButton.setVisible(false);
		fastSpeedButton.setVisible(false);
		finishButton.setVisible(false);

		exitButton.setVisible(true);
	}
	private void setupButtons() {
		Dimension dp = new Dimension(630, 40);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(ColorScheme.centerColumnBackground);
		buttonPanel.setPreferredSize(dp);
		buttonPanel.setMaximumSize(dp);
		setupStartPauseButton();
		setupSpeedSelectButtons();
	}
	private void setupStartPauseButton() {
		startPauseButton = new Button("Start");
		startPauseButton.addActionListener(new RockPaperScissors.RPS_Thread(startPauseButton));
		startPauseButton.setPreferredSize(new Dimension(80, 30));
		buttonPanel.add(startPauseButton);
	}
	private void setupSpeedSelectButtons() {
		slowSpeedButton = new Button("Slow");
		setSleepTimeToButtonAction(slowSpeedButton, 500);
		buttonPanel.add(slowSpeedButton);

		mediumSpeedButton = new Button("Medium");
		setSleepTimeToButtonAction(mediumSpeedButton, 250);
		buttonPanel.add(mediumSpeedButton);

		fastSpeedButton = new Button("Fast");
		setSleepTimeToButtonAction(fastSpeedButton, 1);
		buttonPanel.add(fastSpeedButton);

		finishButton = new Button("Finish");
		finishButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				RockPaperScissors.RPS_Thread.threadSleepTime = 0;
				if(!RockPaperScissors.RPS_Thread.isRunning()) {
					startPauseButton.doClick();
				}
			}
		});
		buttonPanel.add(finishButton);
	}

	private void setupExitButton(Button exitButton) {
		exitButton.setVisible(false);
		buttonPanel.add(exitButton);
	}

	private void setSleepTimeToButtonAction(Button b, int sleepTime) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RockPaperScissors.RPS_Thread.threadSleepTime = sleepTime;
			}
		});
	}
}
