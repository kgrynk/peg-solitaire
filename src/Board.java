import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {

	static class FieldButton extends RoundButton {

		protected void paintComponent(Graphics g) {
			if (getModel().isArmed()) {
				g.setColor(Const.FIELD_CLICKED_C);
			} else {
				g.setColor(Const.FIELD_C);
			}

			super.paintComponent(g);
		}
	}

	static class PawnButton extends RoundButton {


		protected void paintComponent(Graphics g) {
			if (getModel().isArmed()) {
				g.setColor(Const.PAWN_CLICKED_C);
			} else {
				g.setColor(Const.PAWN_C);
			}

			super.paintComponent(g);
		}
	}

	static class ButtonMenu extends JPopupMenu {
		JMenuItem left, right, up, down;
		JLabel cantJump;
	}


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

	public void drawOvalInCenter(int d, Color c, Graphics2D g2) {
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

	//TODO: LPM trzyma zaznaczenie na (pionku) do kliknięcia innego pola/pionka -> ruch albo nie, zerowanie zaznaczenia


	public static FieldButton[][] fields = new FieldButton[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
	public static PawnButton[][] pawns = new PawnButton[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
	public static ButtonMenu[][] menus = new ButtonMenu[Const.BOARD_LENGTH][Const.BOARD_LENGTH];

	private void addButtons() {
		for (int i = 0; i < Const.BOARD_LENGTH; i++) {
			for (int j = 0; j < Const.BOARD_LENGTH; j++) {
				fields[i][j] = new FieldButton();
				pawns[i][j] = new PawnButton();
				menus[i][j] = new ButtonMenu();

				int finalI = i;
				int finalJ = j;
				menus[i][j].left = new JMenuItem(new AbstractAction("Jump left") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Logic.jumpLeft(finalI, finalJ);
						// System.out.println("jumped left!");
						boardUpdate();
						repaint();
					}
				});
				menus[i][j].right = new JMenuItem(new AbstractAction("Jump right") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Logic.jumpRight(finalI, finalJ);
						// System.out.println("jumped right!");
						boardUpdate();
						repaint();
					}
				});
				menus[i][j].up = new JMenuItem(new AbstractAction("Jump up") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Logic.jumpUp(finalI, finalJ);
						// System.out.println("jumped up!");
						boardUpdate();
						repaint();
					}
				});
				menus[i][j].down = new JMenuItem(new AbstractAction("Jump down") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Logic.jumpDown(finalI, finalJ);
						// System.out.println("jumped down!");
						boardUpdate();
						repaint();
					}
				});

				menus[i][j].cantJump = new JLabel("Cannot jump!");

				pawns[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {

					}
				});
				pawns[i][j].setBounds(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
				fields[i][j].setBounds(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
			}
		}
	}

	public void boardUpdate() {
		for (int i = 0; i < Const.BOARD_LENGTH; i++) {
			for (int j = 0; j < Const.BOARD_LENGTH; j++) {

				remove(pawns[i][j]);
				remove(fields[i][j]);

				boolean addPawn = false, addField = false;

				if (State.pawns[i][j]) {
					addPawn = true;
				} else {
					if (State.european) {
						if (Const.FIELDS_EU[i][j]) {
							addField = true;
						}
					} else if (Const.FIELDS_UK[i][j]) {
						addField = true;
					}
				}

				if (addPawn) {
					add(pawns[i][j]);
					JPopupMenu popupMenu = new JPopupMenu();

					boolean cantJump = true;
					if (Logic.canJumpLeft(i, j)) {
						popupMenu.add(menus[i][j].left);
						cantJump = false;
					}
					if (Logic.canJumpRight(i, j)) {
						popupMenu.add(menus[i][j].right);
						cantJump = false;
					}
					if (Logic.canJumpUp(i, j)) {
						popupMenu.add(menus[i][j].up);
						cantJump = false;
					}
					if (Logic.canJumpDown(i, j)) {
						popupMenu.add(menus[i][j].down);
						cantJump = false;
					}
					if(cantJump){
						popupMenu.add(menus[i][j].cantJump);
					}
					pawns[i][j].setComponentPopupMenu(popupMenu);

				} else if (addField) {
					add(fields[i][j]);
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

	}

	public Board() {
		setSize(new Dimension(400, 400));
		setBackground(Const.BACKGROUND_C);
		setLayout(null);

		Logic.setStartingPos();
		addButtons();
		boardUpdate();
	}
}
