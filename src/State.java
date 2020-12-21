public class State {
	static int pawnsLeft = Const.PAWNS_UK;
	static boolean gameEnded = false;
	static boolean european = true;

	public static boolean[][] pawns = {{false, false, true, true, true, false, false},
			{false, false, true, true, true, false, false},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{false, false, true, true, true, false, false},
			{false, false, true, true, true, false, false},
	};
	public static boolean[][] isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];

}
