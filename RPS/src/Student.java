
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.fife.ui.rtextarea.*;

import rps.Constants;

import org.fife.ui.rsyntaxtextarea.*;

public class Student {

	private static String name = null;
	private static Button createButton;
	private static Button testButton;
	private static Button submitButton;

	private static JFrame f;
	private static RSyntaxTextArea codeArea = new RSyntaxTextArea(20, 60);
	private static JTextArea compileErrorArea = new JTextArea(10, 60);
	private static int fontSize = 18;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Student();
	}

	public Student() {

		ColorScheme.loadColorScheme();

		// Make roster file if it doesn't exist
		File directory = new File(Constants.PLAYERS_FILEPATH);
		if (!directory.exists()){
			directory.mkdir();
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		createWindow(screenSize);

		createButton = new Button("Create Player");
		createButton.addActionListener(new CreatePlayer(codeArea));

		testButton = new Button("Test");
		testButton.setEnabled(false);
		testButton.addActionListener(new TestCode(codeArea, compileErrorArea));

		submitButton = new Button("Submit");
		submitButton.setEnabled(false);
		submitButton.addActionListener(new SubmitCode(codeArea));

		JPanel button_panel = new JPanel();
		button_panel.add(createButton);
		button_panel.add(testButton);
		button_panel.add(submitButton);

		createCodeArea();

		compileErrorArea.setFont(new Font("Serif", Font.ITALIC, fontSize));
		compileErrorArea.setForeground(ColorScheme.lost);
		compileErrorArea.setEditable(false);
		compileErrorArea.setWrapStyleWord(true);

		f.add(button_panel, BorderLayout.NORTH);

		RTextScrollPane sp = new RTextScrollPane(codeArea);
		f.add(sp, BorderLayout.CENTER);
	
		JScrollPane scroll = new JScrollPane(compileErrorArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		f.add(scroll, BorderLayout.SOUTH);

		showSplashScreen();

		f.pack();
		f.setVisible(true);
		createButton.requestFocusInWindow();
	}

	public static void allowCreate(boolean b) {
		submitButton.setEnabled(b);
	}

	public static void setCreateButtonText(String text) {
		createButton.setText(text);
	}

	public static void allowSubmit(boolean b) {
		submitButton.setEnabled(b);
	}

	public static void allowTest(boolean b) {
		testButton.setEnabled(b);
	}

	public static String getName() {
		return name;
	}

	public static void setName(String s) {
		f.setTitle(s + "\'s Rock Paper Scissors Code Entry!");
		name = s;
	}
	private void createCodeArea() {
		codeArea.setEditable(false);
		codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		codeArea.setCodeFoldingEnabled(false);

		codeArea.setFont(new Font("Courier", Font.PLAIN, fontSize));

		codeArea.setFocusable(true);
		codeArea.requestFocusInWindow();
		codeArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) { 
				if ((e.getKeyCode() == KeyEvent.VK_EQUALS) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

					if(fontSize < 72) {
						fontSize++;
						codeArea.setFont(new Font("Courier", Font.PLAIN, fontSize));
						compileErrorArea.setFont(new Font("Serif", Font.ITALIC, fontSize));
					}

				} else if ((e.getKeyCode() == KeyEvent.VK_MINUS) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					if(fontSize > 6) {
						fontSize--;
						codeArea.setFont(new Font("Courier", Font.PLAIN, fontSize));
						compileErrorArea.setFont(new Font("Serif", Font.ITALIC, fontSize));
					}
				} else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					testButton.doClick();
				}
			}

			public void keyReleased(KeyEvent e) { }
			public void keyTyped(KeyEvent e) { }
		});
	}

	private void createWindow(Dimension screenSize) {
		f = new JFrame("Rock-Paper-Scissors Player Coder!");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(screenSize);
		f.setLocation(0, 0);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);

		f.setIconImage(Constants.getIcon());
	}

	// a joke loading screen (doesn't actually take anytime to load)
	private void showSplashScreen() {
		try {
			// splash screen
			JDialog splash = new JDialog();

			splash.setUndecorated(true);
			splash.setResizable(false);

			BufferedImage splash_img = ImageIO.read(RockPaperScissors.class.getResource("splash.bmp"));
			splash.setSize(splash_img.getWidth(), splash_img.getHeight() + 20);
			JLabel splash_label = new JLabel(new ImageIcon(splash_img));

			splash_label.setText("Loading Images..");
			splash_label.setHorizontalTextPosition(JLabel.CENTER);
			splash_label.setVerticalTextPosition(JLabel.BOTTOM);
			splash.add(splash_label);

			DifficultyDialog.showOnScreen(0, splash);
			splash.setVisible(true);
			Thread.sleep(800);
			splash_label.setText("Loading Game Engine...");
			Thread.sleep(1100);
			splash_label.setText("Loading AI Neural Network...");
			Thread.sleep(900);
			splash_label.setText("Loading Rocks...");
			Thread.sleep(1800);

			splash.dispose();
		} catch (IOException e) {
			Constants.log("Couldn't load splash screen splash.bmp");
		} catch (InterruptedException e1) {
			System.err.println("Splash screen couldn't sleep!");
		}
	}
}
