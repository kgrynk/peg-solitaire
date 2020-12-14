import javax.swing.*;
import java.awt.*;


public class Board extends JPanel {

	private int centerPosY(int objDiameter) {
		return (Const.WINDOW_SIZE - objDiameter) / 2 - Const.OFFSET;
	}

	private int centerPosX(int objDiameter) {
		return (Const.WINDOW_SIZE - objDiameter) / 2;
	}

	private void fillOvalInPos(int x, int y, int d, Color c, Graphics2D g2) {
		g2.setColor(c);
		g2.fillOval(x, y, d, d);
	}

	private void fillOvalInCenter(int d, Color c, Graphics2D g2) {
		g2.setColor(c);

		g2.fillOval(centerPosX(d), centerPosY(d), d, d);
	}

	private void drawOvalInPos(int x, int y, int d, Color c, Graphics2D g2) {
		g2.setColor(c);
		g2.drawOval(x, y, d, d);
	}

	private void drawOvalInCenter(int d, Color c, Graphics2D g2) {
		g2.setColor(c);
		g2.drawOval(centerPosX(d), centerPosY(d), d, d);
	}

	private int evalPawnPosX(int j) {
		int field = Const.BOARD_D / 7;
		return centerPosX(Const.PAWN_D) + (j - 3) * field;
	}

	private int evalPawnPosY(int i) {
		int field = Const.BOARD_D / (Const.BOARD_LENGTH + 1);
		return centerPosY(Const.PAWN_D) + (i - 3) * field;
	}

	private void drawPawns(Graphics2D g2) {
		for (int i = 0; i < Const.BOARD_LENGTH; i++) {
			for (int j = 0; j < Const.BOARD_LENGTH; j++) {
				if (State.pawns[i][j]) {
					fillOvalInPos(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.PAWN_C, g2);
				} else {
					if (State.european) {
						if (Const.FIELDS_EU[i][j]) {
							fillOvalInPos(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.FIELD_C, g2);
						}
					} else if (Const.FIELDS_UK[i][j]) {
						fillOvalInPos(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.FIELD_C, g2);
					}
				}
			}
		}
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		// Board //
		fillOvalInCenter(Const.BOARD_D, Const.BOARD_C, graphics);

		// Outline //
		graphics.setStroke(new BasicStroke(3));
		drawOvalInCenter(Const.BOARD_D, Const.OUTLINE_C, graphics);

		// Pawns //
		drawPawns(graphics);
	}

	public Board() {
		setSize(new Dimension(400, 400));
		setBackground(Const.BACKGROUND_C);

	}
}
