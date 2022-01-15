/* Game state */
public class State {
	static int pawnsLeft = Const.PAWNS_UK;
	static boolean european = false,
			makingJump = false,
			selection = false;
	static int jumpI = 0, jumpJ = 0;

	public static boolean[][] pawns = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
	public static boolean[][] isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];

}
