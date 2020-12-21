import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


				select = new JMenuItem((new AbstractAction("Zaznacz") {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO: wybranie pionka
					}
				}));
				moves.add(select);

				jump = new JMenuItem((new AbstractAction("Skocz") {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO: wybranie skoku (coś z tego popupa)
					}
				}));
				moves.add(jump);


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
		}

		public static FieldButton[][] fields = new FieldButton[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
		public static PawnButton[][] pawns = new PawnButton[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
		public static ButtonMenu[][] menus = new ButtonMenu[Const.BOARD_LENGTH][Const.BOARD_LENGTH];

		Bar bar = new Bar();
		Board board = new Board();
		JLabel info = new JLabel();

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
							Logic.jump(finalI, finalJ, Const.jumpType.LEFT);
							// System.out.println("jumped left!");
							gameUpdate();
							repaint();
						}
					});
					menus[i][j].right = new JMenuItem(new AbstractAction("Jump right") {
						@Override
						public void actionPerformed(ActionEvent e) {
							Logic.jump(finalI, finalJ, Const.jumpType.RIGHT);
							// System.out.println("jumped right!");
							gameUpdate();
							repaint();
						}
					});
					menus[i][j].up = new JMenuItem(new AbstractAction("Jump up") {
						@Override
						public void actionPerformed(ActionEvent e) {
							Logic.jump(finalI, finalJ, Const.jumpType.UP);
							// System.out.println("jumped up!");
							gameUpdate();
							repaint();
						}
					});
					menus[i][j].down = new JMenuItem(new AbstractAction("Jump down") {
						@Override
						public void actionPerformed(ActionEvent e) {
							Logic.jump(finalI, finalJ, Const.jumpType.DOWN);
							// System.out.println("jumped down!");
							gameUpdate();
							repaint();
						}
					});

					menus[i][j].cantJump = new JLabel("Cannot jump!");

					pawns[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							if (State.makingJump) {
								State.isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
								State.makingJump = false;
								gameUpdate();
								repaint();
							} else {
								if (Logic.canJump(finalI, finalJ, Const.jumpType.LEFT)) {
									State.isButtonActive[finalI][finalJ - 2] = true;
									State.makingJump = true;
								}
								if (Logic.canJump(finalI, finalJ, Const.jumpType.RIGHT)) {
									State.isButtonActive[finalI][finalJ + 2] = true;
									State.makingJump = true;
								}
								if (Logic.canJump(finalI, finalJ, Const.jumpType.UP)) {
									State.isButtonActive[finalI - 2][finalJ] = true;
									State.makingJump = true;
								}
								if (Logic.canJump(finalI, finalJ, Const.jumpType.DOWN)) {
									State.isButtonActive[finalI + 2][finalJ] = true;
									State.makingJump = true;
								}
								if (State.makingJump) {
									State.jumpI = finalI;
									State.jumpJ = finalJ;
								}
								gameUpdate();
								repaint();
							}
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
								State.isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
								State.makingJump = false;
								gameUpdate();
								repaint();
							} else {
								State.isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
								gameUpdate();
								repaint();
							}
						}
					});
					pawns[i][j].setBounds(Const.evalPawnPosX(j), Const.evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
					fields[i][j].setBounds(Const.evalPawnPosX(j), Const.evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
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

						boolean cantJump = true;
						if (Logic.canJump(i, j, Const.jumpType.LEFT)) {
							popupMenu.add(menus[i][j].left);
							cantJump = false;
						}
						if (Logic.canJump(i, j, Const.jumpType.RIGHT)) {
							popupMenu.add(menus[i][j].right);
							cantJump = false;
						}
						if (Logic.canJump(i, j, Const.jumpType.UP)) {
							popupMenu.add(menus[i][j].up);
							cantJump = false;
						}
						if (Logic.canJump(i, j, Const.jumpType.DOWN)) {
							popupMenu.add(menus[i][j].down);
							cantJump = false;
						}
						if (cantJump) {
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
		}


		public Window() {
			super("Samotnik");

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
