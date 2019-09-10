
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TieDialog {

	private static JDialog jd = null;

	public static void show(String name, String fileInfo) {
// TODO dispose on lose focus or time?
		jd = new JDialog();
		jd.setTitle(name + " wins!");
		jd.setResizable(false);
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel tf = new JLabel(fileInfo);
		tf.setFont(new Font("Arial", Font.PLAIN, 34));

		panel.add(tf);
		Button b = new Button("Ok");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});	

		buttonPanel.add(b);

		jd.add(panel, BorderLayout.NORTH);
		jd.add(buttonPanel, BorderLayout.SOUTH);
		jd.setSize(630,150);
		
		showOnScreen(0, jd);		
		jd.setModal(true);
		jd.setVisible(true);
	}

	public static void showOnScreen( int screen, JDialog frame ) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if( screen > 0 && screen < 2 ) {
			int x = frame.getWidth()/2;
			int y = frame.getHeight()/2;
			frame.setLocation((gd[screen].getDefaultConfiguration().getBounds().width * 3/2) - x,   (gd[screen].getDefaultConfiguration().getBounds().height *1/2) - y);
		} else if( gd.length > 0 ) {
			frame.setLocationRelativeTo(null);
		} else {
			throw new RuntimeException( "No Screens Found" );
		}
	}
	
	public static void main(String[] args) {
//		"<html>"+player1.getName()+": " + p1Lines + " lines " + p1Chars + " characters.<br>" + player2.getName() + ": " + p2Lines + " lines " + p2Chars + " characters.</html>";
		int p1Lines = 300, p2Lines = 2, p1Chars = 20, p2Chars = 400;
		
		TieDialog.show("player1", "<html>Kevin: " + p1Lines + " lines " + p1Chars + " characters.<br>Anthony: " + p2Lines + " lines " + p2Chars + " characters.</html>");
	}

}
