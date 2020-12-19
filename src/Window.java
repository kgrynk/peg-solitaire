import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

	static class FieldButton extends RoundButton {

		public static FieldButton[][] initArray(int a, int b) {
			FieldButton[][] array = new FieldButton[a][b];
			for (int i = 0; i < a; i++) {
				for (int j = 0; j < b; j++) {
					array[i][j] = new FieldButton();
				}
			}
			return array;
		}

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

		public static PawnButton[][] initArray(int a, int b) {
			PawnButton[][] array = new PawnButton[a][b];
			for (int i = 0; i < a; i++) {
				for (int j = 0; j < b; j++) {
					array[i][j] = new PawnButton();
				}
			}
			return array;
		}

		protected void paintComponent(Graphics g) {
			if (getModel().isArmed()) {
				g.setColor(Const.PAWN_CLICKED_C);
			} else {
				g.setColor(Const.PAWN_C);
			}

			super.paintComponent(g);
		}
	}


	static class Logic {
		static void setStartingPos() {
			if (State.european) {
				State.pawns[1][1] = true;
				State.pawns[1][5] = true;
				State.pawns[5][1] = true;
				State.pawns[5][5] = true;
			}
			State.pawns[3][3] = false;
			if (State.european) {
				State.pawnsLeft = Const.PAWNS_EU;
			}
		}

		public static FieldButton[][] fields = FieldButton.initArray(Const.BOARD_LENGTH, Const.BOARD_LENGTH);
		public static PawnButton[][] pawns = PawnButton.initArray(Const.BOARD_LENGTH, Const.BOARD_LENGTH);


		//TODO: sprawdzanie czy można zrobić skok i bicie pionka
	}


	static class Board extends JPanel {

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
		//TODO: PPM (na pionku) daje 4 (max) opcje skoku, rozwijane menu

		private void drawPawns() {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				for (int j = 0; j < Const.BOARD_LENGTH; j++) {
					Logic.pawns[i][j].setBounds(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);
					Logic.fields[i][j].setBounds(evalPawnPosX(j), evalPawnPosY(i), Const.PAWN_D, Const.PAWN_D);

					if (State.pawns[i][j]) {
						add(Logic.pawns[i][j]);
					} else {
						if (State.european) {
							if (Const.FIELDS_EU[i][j]) {
								add(Logic.fields[i][j]);
							}
						} else if (Const.FIELDS_UK[i][j]) {
							add(Logic.fields[i][j]);
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

		}

		public Board() {
			setSize(new Dimension(400, 400));
			setBackground(Const.BACKGROUND_C);
			setLayout(null);
			drawPawns();

		}
	}


	private void makeMenu() {
		JMenuBar bar = new JMenuBar();

		JMenu game = new JMenu("Gra");
		game.add(new JMenuItem("Nowa"));
		game.addSeparator();
		game.add(new JMenuItem("Zakończ"));

		//TODO: zerowanie stanu gry do początku /zabijanie okna

		JMenu moves = new JMenu("Ruchy");
		moves.add(new JMenuItem("Zaznacz"));
		moves.add(new JMenuItem("Skocz"));
		//TODO: trzeba jakoś umożliwić wybranie konkretnego pola i potem zrobić obsługę 4 (albo mniej) skoków

		JMenu sett = new JMenu("Ustawienia");
		sett.add(new JMenuItem("Typ planszy"));
		sett.add(new JMenuItem("Kolory"));
		//TODO: zmiana jednego boola (ale tylko kiedy gra nie jest aktywna?) /tych stałych od kolorków

		JMenu help = new JMenu("Pomoc");
		help.add(new JMenuItem("O grze"));
		help.add(new JMenuItem("O aplikacji"));
		//TODO: opisy jakieś (okienko)

		bar.add(game);
		bar.add(moves);
		bar.add(sett);
		bar.add(Box.createGlue());
		bar.add(help);

		setJMenuBar(bar);
	}

	public Window() {
		super("Samotnik");

		setSize(Const.WINDOW_SIZE, Const.WINDOW_SIZE);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		makeMenu();

		Logic.setStartingPos();
		JPanel board = new Board();
		add(board);

		JLabel info = new JLabel("Pozostałe pionki: " + State.pawnsLeft);
		if (State.gameEnded) {
			info = new JLabel("Gratulacje, wygrałeś!");
		}
		info.setFont(info.getFont().deriveFont(16.0f));
		info.setHorizontalAlignment(0);
		add(BorderLayout.SOUTH, info);

		setVisible(true);
		System.out.println(Const.FIELDS_EU[3][3]);

	}
}
