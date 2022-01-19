/* Game state */
public class State {
	static int pawnsLeft = Const.NUMBER_OF_PAWNS_BRITISH;
	static boolean isBoardTypeEuropean = false,
			makingJump = false,
			selection = false;
	static int jumpI = 0, jumpJ = 0;

	public static boolean[][] pawnActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
	public static boolean[][] isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];

}
