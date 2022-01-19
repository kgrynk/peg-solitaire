import javax.swing.*;
import java.awt.*;

/* Game board */
public class Board extends JPanel {

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		/* Draw board*/
		int d = Const.BOARD_DIMENSION;
		graphics.setColor(Const.BOARD_COLOR);
		graphics.fillOval(Logic.windowCenterY(d), Logic.windowCenterX(d), d, d);

		/* Draw outline */
		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(Const.OUTLINE_COLOR);
		graphics.drawOval(Logic.windowCenterY(d), Logic.windowCenterX(d), d, d);

	}

	public Board() {
		setSize(new Dimension(400, 400));
		setBackground(Const.BACKGROUND_COLOR);
		setLayout(null);
	}
}
