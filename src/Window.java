import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

	private void makeMenu() {
		JMenuBar bar = new JMenuBar();

		JMenu game = new JMenu("Gra");
		game.add(new JMenuItem("Nowa"));
		game.add(new JMenuItem("Zakończ"));

		JMenu moves = new JMenu("Ruchy");
		moves.add(new JMenuItem("Zaznacz"));
		moves.add(new JMenuItem("Skocz"));

		JMenu sett = new JMenu("Ustawienia");
		sett.add(new JMenuItem("Typ planszy"));
		sett.add(new JMenuItem("Kolory"));

		JMenu help = new JMenu("Pomoc");
		help.add(new JMenuItem("O grze"));
		help.add(new JMenuItem("O aplikacji"));

		bar.add(game);
		bar.add(moves);
		bar.add(sett);
		bar.add(Box.createGlue());
		bar.add(help);

		setJMenuBar(bar);
	}

	public Window() {
		super("Gra madżong czy jakoś tak");

		setSize(Const.WINDOW_SIZE, Const.WINDOW_SIZE);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		makeMenu();

		JPanel board = new Board();
		add(board);

		JLabel info = new JLabel("Pozostałe pionki: " + State.pawnsLeft);
		if (State.gameEnded) {
			info = new JLabel("Gratulacje, wygrałeś!");
		}
		info.setFont(info.getFont().deriveFont(16.0f));
		info.setHorizontalAlignment(0);
		add(BorderLayout.SOUTH, info);

		Logic.setStartingPos();
		setVisible(true);
		System.out.println(Const.FIELDS_EU[3][3]);
		/*
		while (true) {
			board.repaint();
			if(State.gameEnded){
				System.exit(0);
			}
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
				System.exit(0);
			}
		}*/
	}
}
