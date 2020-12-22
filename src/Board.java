import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		// Board //
		int d = Const.BOARD_D;
		graphics.setColor(Const.BOARD_C);
		graphics.fillOval(Logic.centerPosX(d), Logic.centerPosY(d), d, d);

		// Outline //
		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(Const.OUTLINE_C);
		graphics.drawOval(Logic.centerPosX(d), Logic.centerPosY(d), d, d);

	}

	public Board() {
		setSize(new Dimension(400, 400));
		setBackground(Const.BACKGROUND_C);
		setLayout(null);
	}
}
