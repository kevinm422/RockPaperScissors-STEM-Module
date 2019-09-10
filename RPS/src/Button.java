


import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Button extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Button() {
		super();
		formatButton(this);
	}
	public Button(String s) {
		super(s);
		formatButton(this);
	}

	protected static void formatButton(JButton b) {
		b.setFocusPainted(false);
		b.setContentAreaFilled(true);

		if(b.isEnabled()) {
			b.setForeground(ColorScheme.left_column);
			b.setBackground(ColorScheme.button_background);
		} else {
			b.setForeground(ColorScheme.button_text);
			b.setBackground(ColorScheme.button_background);
		}

		Border line = new LineBorder(ColorScheme.left_column);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		b.setBorder(compound);

		b.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				if(b.isEnabled()) {
					b.setBackground(ColorScheme.left_column);
					b.setForeground(ColorScheme.button_background);
				} else {
					b.setBackground(ColorScheme.button_text);
					b.setForeground(ColorScheme.button_background);
				}
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				if(b.isEnabled()) {
					b.setForeground(ColorScheme.left_column);
					b.setBackground(ColorScheme.button_background);
				} else {
					b.setForeground(ColorScheme.button_text);
					b.setBackground(ColorScheme.button_background);
				}
			}
		});
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		formatButton(this);
	}
}
