import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

	static class Window extends JFrame {

		class Bar extends JMenuBar {
			JMenu game = new JMenu("Gra"),
					moves = new JMenu("Ruchy"),
					sett = new JMenu("Ustawienia"),
					help = new JMenu("Pomoc");


			JMenuItem start, end, select, jump, boardTypeUK, boardTypeEU, colors, aboutGame, aboutApp;

			public Bar() {

				start = new JMenuItem((new AbstractAction("Nowa") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Logic.setStartingPos();
						gameUpdate();
						repaint();
					}
				}));
				game.add(start);

				game.addSeparator();

				end = new JMenuItem((new AbstractAction("Zakończ") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}));
				game.add(end);


				jump = new JMenu("Skocz");
				jump.add(cantJump);
				moves.add(jump);

				select = new JMenuItem((new AbstractAction("Zaznacz") {

					@Override
					public void actionPerformed(ActionEvent e) {
						State.selection = true;
						State.jumpI = 3;
						State.jumpJ = 3;
						Logic.noAcitveButtons();
						State.isButtonActive[State.jumpI][State.jumpJ] = true;
						gameUpdate();
						repaint();
					}
				}));

				moves.add(select);


				boardTypeUK = new JRadioButtonMenuItem((new AbstractAction("Plansza brytyjska") {
					@Override
					public void actionPerformed(ActionEvent e) {
						State.european = false;
						Logic.setStartingPos();
						gameUpdate();
						repaint();
					}
				}));
				sett.add(boardTypeUK);

				boardTypeEU = new JRadioButtonMenuItem((new AbstractAction("Plansza europejska") {
					@Override
					public void actionPerformed(ActionEvent e) {
						State.european = true;
						Logic.setStartingPos();
						gameUpdate();
						repaint();
					}
				}));
				sett.add(boardTypeEU);

				ButtonGroup boardType = new ButtonGroup();
				boardType.add(boardTypeUK);
				boardType.add(boardTypeEU);
				boardTypeUK.setSelected(true);

				colors = new JMenuItem((new AbstractAction("Kolory") {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO: okienko z kolorami, może zmieniać Consty xd
					}
				}));
				sett.add(colors);


				aboutGame = new JMenuItem((new AbstractAction("O grze") {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO: okienko i opis z wiki
					}
				}));
				help.add(aboutGame);

				aboutApp = new JMenuItem((new AbstractAction("O aplikacji") {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO: okienko i autor, wersja itd
					}
				}));
				help.add(aboutApp);

				add(game);
				add(moves);
				add(sett);
				add(Box.createGlue());
				add(help);
			}
		}

		static class FieldButton extends RoundButton {

			protected void paintComponent(Graphics g) {
				if (getModel().isArmed() || getModel().isSelected()) {
					g.setColor(Const.FIELD_CLICKED_C);
				} else {
					g.setColor(Const.FIELD_C);
				}

				super.paintComponent(g);
			}
		}

		static class PawnButton extends RoundButton {

			protected void paintComponent(Graphics g) {
				if (getModel().isArmed() || getModel().isSelected()) {
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

		private void addButtons() {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				for (int j = 0; j < Const.BOARD_LENGTH; j++) {
					fields[i][j] = new FieldButton();
					pawns[i][j] = new PawnButton();
					menus[i][j] = new ButtonMenu();

					int finalI = i;
					int finalJ = j;

					for (Const.jumpType type : Const.typeList) {
						JMenuItem item = new JMenuItem(new AbstractAction("Jump " + type) {
							@Override
							public void actionPerformed(ActionEvent e) {
								Logic.jump(finalI, finalJ, type);
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
								Logic.noAcitveButtons();
								State.makingJump = false;
							} else {
								for (Const.jumpType type : Const.typeList) {
									if (Logic.canJump(finalI, finalJ, type)) {
										State.isButtonActive[Logic.jumpPosI(finalI, type)][Logic.jumpPosJ(finalJ, type)] = true;
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
								Const.jumpType type = Logic.makeValidJump(State.jumpI, State.jumpJ, finalI, finalJ);
								if (type != null) {
									System.out.println(type.toString());
								} else {
									System.out.println("null");
								}
								State.makingJump = false;
							}

							Logic.noAcitveButtons();
							gameUpdate();
							repaint();
						}
					});
					pawns[i][j].setBounds(Logic.evalPawnPosX(j), Logic.evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
					fields[i][j].setBounds(Logic.evalPawnPosX(j), Logic.evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
				}
			}
		}

		private void gameUpdate() {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				for (int j = 0; j < Const.BOARD_LENGTH; j++) {

					board.remove(pawns[i][j]);
					board.remove(fields[i][j]);

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
						board.add(pawns[i][j]);
						JPopupMenu popupMenu = new JPopupMenu();

						boolean canJump = false;
						for (Const.jumpType type : Const.typeList) {
							if (Logic.canJump(i, j, type)) {
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
				info.setText("Gratulacje, wygrałeś!");
			} else {
				info.setText("Pozostałe pionki: " + State.pawnsLeft);
			}

			if (State.european && State.pawnsLeft == Const.PAWNS_EU || (State.pawnsLeft == Const.PAWNS_UK)) {
				bar.boardTypeUK.setEnabled(true);
				bar.boardTypeEU.setEnabled(true);
			} else {
				bar.boardTypeUK.setEnabled(false);
				bar.boardTypeEU.setEnabled(false);
			}

			if(State.selection){
				boolean canJump = false;
				ButtonMenu menu = menus[State.jumpI][State.jumpJ];
				bar.jump.remove(cantJump);
				for (Const.jumpType type : Const.typeList) {
					bar.jump.remove(menu.getMenuItem(type));
					if (Logic.canJump(State.jumpI, State.jumpJ, type)) {
						bar.jump.add(menu.getMenuItem(type));
						canJump = true;
					}
				}
				if (!canJump) {
					bar.jump.add(menu.cantJump);
				}
			}
		}
		class KeySelectListener extends KeyAdapter{
			@Override
			public void keyPressed(KeyEvent e) {
				if(State.selection){
					for (Const.jumpType type : Const.typeList) {
						int eventType = 0;
						switch (type) {
							case LEFT -> eventType = KeyEvent.VK_LEFT;
							case RIGHT -> eventType = KeyEvent.VK_RIGHT;
							case UP -> eventType = KeyEvent.VK_UP;
							case DOWN -> eventType = KeyEvent.VK_DOWN;
						}
						if (e.getKeyCode() == eventType) {
							int i = Logic.midPosI(State.jumpI, type);
							int j = Logic.midPosJ(State.jumpJ, type);
							if (Logic.isField(i, j, State.european)) {
								State.jumpI = i;
								State.jumpJ = j;
								Logic.noAcitveButtons();
								State.isButtonActive[i][j] = true;
							}
							gameUpdate();
							repaint();
						}

					}
				}}

		}

		public Window() {
			super("Samotnik");

			addKeyListener(new KeySelectListener());
			setSize(Const.WINDOW_SIZE, Const.WINDOW_SIZE);
			setLocationRelativeTo(null);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			add(bar);
			setJMenuBar(bar);

			info.setHorizontalAlignment(0);
			info.setFont(info.getFont().deriveFont(16.0f));
			add(BorderLayout.SOUTH, info);
			add(board);

			Logic.setStartingPos();
			addButtons();
			gameUpdate();

			setVisible(true);
		}
	}

	public static void main(String[] args) {
		new Window();
	}
}
