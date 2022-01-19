import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

	static class Window extends JFrame {

		/* Top menu bar */
		class Bar extends JMenuBar {
			JMenu game = new JMenu("Game"),
					moves = new JMenu("Moves"),
					settings = new JMenu("Settings");


			JMenuItem start, end, select, jump, boardTypeBritish, boardTypeEuropean;

			public Bar() {

				start = new JMenuItem((new AbstractAction("New") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Logic.setStartingPosition();
						gameUpdate();
						repaint();
					}
				}));
				game.add(start);

				game.addSeparator();

				end = new JMenuItem((new AbstractAction("Quit") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}));
				game.add(end);


				jump = new JMenu("Make jump");
				jump.add(cantJump);
				moves.add(jump);

				select = new JMenuItem((new AbstractAction("Select") {

					@Override
					public void actionPerformed(ActionEvent e) {
						State.selection = true;
						State.jumpI = 3;
						State.jumpJ = 3;
						Logic.deactivateButtons();
						State.isButtonActive[State.jumpI][State.jumpJ] = true;
						gameUpdate();
						repaint();
					}
				}));

				moves.add(select);


				boardTypeBritish = new JRadioButtonMenuItem((new AbstractAction("British board") {
					@Override
					public void actionPerformed(ActionEvent e) {
						State.isBoardTypeEuropean = false;
						Logic.setStartingPosition();
						gameUpdate();
						repaint();
					}
				}));
				settings.add(boardTypeBritish);

				boardTypeEuropean = new JRadioButtonMenuItem((new AbstractAction("European board") {
					@Override
					public void actionPerformed(ActionEvent e) {
						State.isBoardTypeEuropean = true;
						Logic.setStartingPosition();
						gameUpdate();
						repaint();
					}
				}));
				settings.add(boardTypeEuropean);

				ButtonGroup boardType = new ButtonGroup();
				boardType.add(boardTypeBritish);
				boardType.add(boardTypeEuropean);
				boardTypeBritish.setSelected(true);

				add(game);
				add(moves);
				add(settings);
				add(Box.createGlue());
			}
		}

		/* Field class */
		static class FieldButton extends RoundButton {

			protected void paintComponent(Graphics g) {
				if (getModel().isArmed() || getModel().isSelected()) {
					g.setColor(Const.FIELD_CLICKED_COLOR);
				} else {
					g.setColor(Const.FIELD_COLOR);
				}

				super.paintComponent(g);
			}
		}

		/* Pawn class */
		static class PawnButton extends RoundButton {

			protected void paintComponent(Graphics g) {
				if (getModel().isArmed() || getModel().isSelected()) {
					g.setColor(Const.PAWN_CLICKED_COLOR);
				} else {
					g.setColor(Const.PAWN_COLOR);
				}

				super.paintComponent(g);
			}
		}

		/* Right click menu */
		static class ButtonMenu extends JPopupMenu {
			JMenuItem left, right, up, down;
			JLabel cantJump;

			public JMenuItem getMenuItem(Const.jumpType type) {
				switch (type) {
					case LEFT -> {
						return left;
					}
					case RIGHT -> {
						return right;
					}
					case UP -> {
						return up;
					}
					case DOWN -> {
						return down;
					}
				}
				return null;
			}

			public void setMenuItem(JMenuItem item, Const.jumpType type) {
				switch (type) {
					case LEFT -> left = item;
					case RIGHT -> right = item;
					case UP -> up = item;
					case DOWN -> down = item;
				}
			}
		}

		public static FieldButton[][] fields = new FieldButton[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
		public static PawnButton[][] pawns = new PawnButton[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
		public static ButtonMenu[][] menus = new ButtonMenu[Const.BOARD_LENGTH][Const.BOARD_LENGTH];

		Bar bar = new Bar();
		Board board = new Board();
		JLabel info = new JLabel();

		static JLabel cantJump = new JLabel("Cannot jump!");

		/* Create buttons (pawns/fields) */
		private void addButtons() {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				for (int j = 0; j < Const.BOARD_LENGTH; j++) {
					fields[i][j] = new FieldButton();
					pawns[i][j] = new PawnButton();
					menus[i][j] = new ButtonMenu();

					int finalI = i;
					int finalJ = j;

					for (Const.jumpType type : Const.jumpTypes) {
						JMenuItem item = new JMenuItem(new AbstractAction("Jump " + type) {
							@Override
							public void actionPerformed(ActionEvent e) {
								Logic.makeJump(finalI, finalJ, type);
								gameUpdate();
								repaint();
							}
						});
						menus[i][j].setMenuItem(item, type);
					}
					menus[i][j].cantJump = cantJump;

					pawns[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							if (State.makingJump) {
								Logic.deactivateButtons();
								State.makingJump = false;
							} else {
								for (Const.jumpType type : Const.jumpTypes) {
									if (Logic.isJumpLegal(finalI, finalJ, type)) {
										State.isButtonActive[Logic.jumpingPawnNewI(finalI, type)][Logic.jumpingPawnNewJ(finalJ, type)] = true;
										State.makingJump = true;
									}
								}
								if (State.makingJump) {
									State.jumpI = finalI;
									State.jumpJ = finalJ;
								}
							}
							gameUpdate();
							repaint();
						}
					});

					fields[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							if (State.makingJump) {
								Const.jumpType type = Logic.getJumpType(State.jumpI, State.jumpJ, finalI, finalJ);
								if (type != null) {
									System.out.println(type.toString());
								} else {
									System.out.println("null");
								}
								State.makingJump = false;
							}

							Logic.deactivateButtons();
							gameUpdate();
							repaint();
						}
					});
					pawns[i][j].setBounds(Logic.evalPawnPositionX(j),
							Logic.evalPawnPositionY(i),
							Const.PAWN_DIMENSION,
							Const.PAWN_DIMENSION);
					fields[i][j].setBounds(Logic.evalPawnPositionX(j),
							Logic.evalPawnPositionY(i),
							Const.PAWN_DIMENSION,
							Const.PAWN_DIMENSION);
				}
			}
		}

		/* Updates game to actual state from State class */
		private void gameUpdate() {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				for (int j = 0; j < Const.BOARD_LENGTH; j++) {

					board.remove(pawns[i][j]);
					board.remove(fields[i][j]);

					boolean addPawn = false, addField = false;

					if (State.pawnActive[i][j]) {
						addPawn = true;
					} else {
						if (State.isBoardTypeEuropean) {
							if (Const.EUROPEAN_BOARD[i][j]) {
								addField = true;
							}
						} else if (Const.BRITISH_BOARD[i][j]) {
							addField = true;
						}
					}

					if (addPawn) {
						board.add(pawns[i][j]);
						JPopupMenu popupMenu = new JPopupMenu();

						boolean canJump = false;
						for (Const.jumpType type : Const.jumpTypes) {
							if (Logic.isJumpLegal(i, j, type)) {
								popupMenu.add(menus[i][j].getMenuItem(type));
								canJump = true;
							}
						}
						if (!canJump) {
							popupMenu.add(menus[i][j].cantJump);
						}
						pawns[i][j].setComponentPopupMenu(popupMenu);

						if (State.isButtonActive[i][j]) {
							pawns[i][j].getModel().setSelected(true);
							System.out.println(i + " " + j + " armed");
						} else {
							pawns[i][j].getModel().setSelected(false);
						}
					} else if (addField) {
						board.add(fields[i][j]);

						if (State.isButtonActive[i][j]) {
							fields[i][j].getModel().setSelected(true);
							System.out.println(i + " " + j + " armed");
						} else {
							fields[i][j].getModel().setSelected(false);
						}
					}
				}
			}

			if (State.pawnsLeft == 1) {
				info.setText("You won!");
			} else {
				info.setText("Pawns left: " + State.pawnsLeft);
			}

			if (State.isBoardTypeEuropean
					&& State.pawnsLeft == Const.NUMBER_OF_PAWNS_EUROPEAN
					|| (State.pawnsLeft == Const.NUMBER_OF_PAWNS_BRITISH)) {
				bar.boardTypeBritish.setEnabled(true);
				bar.boardTypeEuropean.setEnabled(true);
			} else {
				bar.boardTypeBritish.setEnabled(false);
				bar.boardTypeEuropean.setEnabled(false);
			}

			if(State.selection){
				boolean canJump = false;
				ButtonMenu menu = menus[State.jumpI][State.jumpJ];
				bar.jump.remove(cantJump);
				for (Const.jumpType type : Const.jumpTypes) {
					bar.jump.remove(menu.getMenuItem(type));
					if (Logic.isJumpLegal(State.jumpI, State.jumpJ, type)) {
						bar.jump.add(menu.getMenuItem(type));
						canJump = true;
					}
				}
				if (!canJump) {
					bar.jump.add(menu.cantJump);
				}
			}
		}

		/* Keyboard "driver" */
		class KeySelectListener extends KeyAdapter{
			@Override
			public void keyPressed(KeyEvent e) {
				if(State.selection){
					for (Const.jumpType type : Const.jumpTypes) {
						int eventType = 0;
						switch (type) {
							case LEFT -> eventType = KeyEvent.VK_LEFT;
							case RIGHT -> eventType = KeyEvent.VK_RIGHT;
							case UP -> eventType = KeyEvent.VK_UP;
							case DOWN -> eventType = KeyEvent.VK_DOWN;
						}
						if (e.getKeyCode() == eventType) {
							int i = Logic.beatenPawnNewI(State.jumpI, type);
							int j = Logic.beatenPawnNewJ(State.jumpJ, type);
							if (Logic.isField(i, j, State.isBoardTypeEuropean)) {
								State.jumpI = i;
								State.jumpJ = j;
								Logic.deactivateButtons();
								State.isButtonActive[i][j] = true;
							}
							gameUpdate();
							repaint();
						}

					}
				}}

		}

		/* Create game window */
		public Window() {
			super("Peg solitaire");

			addKeyListener(new KeySelectListener());
			setSize(Const.WINDOW_DIMENSION, Const.WINDOW_DIMENSION);
			setLocationRelativeTo(null);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			add(bar);
			setJMenuBar(bar);

			info.setHorizontalAlignment(0);
			info.setFont(info.getFont().deriveFont(16.0f));
			add(BorderLayout.SOUTH, info);
			add(board);

			Logic.setStartingPosition();
			addButtons();
			gameUpdate();

			setVisible(true);
		}
	}

	public static void main(String[] args) {
		new Window();
	}
}
