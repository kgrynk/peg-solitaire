import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RoundButton extends JButton {

	public RoundButton() {
		setFocusable(false);
		setContentAreaFilled(false);
		setBorder(null);
	}

	protected void paintComponent(Graphics g) {
/*		if (getModel().isArmed()) {
			g.setColor(Const.FIELD_CLICKED_C);
		} else {
			g.setColor(Const.FIELD_C);
		}*/
		g.fillOval(0, 0, getSize().width, getSize().height);

		super.paintComponent(g);
	}

	// Hit detection.
	Shape shape;

	public boolean contains(int x, int y) {
		// If the button has changed size,  make a new shape object.
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
}