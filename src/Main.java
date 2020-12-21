import javax.swing.*;
import java.awt.*;

public class Main {
	static class Window extends JFrame {

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

			Board board = new Board();
			add(board);

			board.boardUpdate();
			JLabel info = new JLabel("Pozostałe pionki: " + State.pawnsLeft);
			if (State.gameEnded) {
				info = new JLabel("Gratulacje, wygrałeś!");
			}
			info.setFont(info.getFont().deriveFont(16.0f));
			info.setHorizontalAlignment(0);
			add(BorderLayout.SOUTH, info);

			setVisible(true);
		}
	}

    public static void main(String[] args) {
    	new Window();
    }
}
