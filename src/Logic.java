import java.util.Arrays;

/* Game logic procedures */
public class Logic {

	/* Initialize game state */
	static void setStartingPosition() {
		if (State.isBoardTypeEuropean) {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				State.pawnActive[i] = Arrays.copyOf(Const.EUROPEAN_BOARD[i], Const.EUROPEAN_BOARD[i].length);
			}
		} else {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				State.pawnActive[i] = Arrays.copyOf(Const.BRITISH_BOARD[i], Const.BRITISH_BOARD[i].length);
			}
		}
		State.pawnActive[3][3] = false;
		if (State.isBoardTypeEuropean) {
			State.pawnsLeft = Const.NUMBER_OF_PAWNS_EUROPEAN;
		} else {
			State.pawnsLeft = Const.NUMBER_OF_PAWNS_BRITISH;
		}
	}

	/* For given coordinates returns jump direction */
	static Const.jumpType getJumpType(int i, int j, int iJump, int jJump) {
		for (Const.jumpType type : Const.jumpTypes) {
			if (iJump == jumpingPawnNewI(i, type) && jJump == jumpingPawnNewJ(j, type)) {
				makeJump(i, j, type);
				return type;
			}
		}
		return null;
	}

	/* Checks if jump can be done */
	static boolean isJumpLegal(int i, int j, Const.jumpType type) {
		if (type == null) {
			return false;
		}
		if (isField(i, j, State.isBoardTypeEuropean)
				&& isField(jumpingPawnNewI(i, type), jumpingPawnNewJ(j, type), State.isBoardTypeEuropean)
				&& isField(beatenPawnNewI(i, type), beatenPawnNewJ(j, type), State.isBoardTypeEuropean)) {
			return !State.pawnActive[jumpingPawnNewI(i, type)][jumpingPawnNewJ(j, type)]
					&& State.pawnActive[beatenPawnNewI(i, type)][beatenPawnNewJ(j, type)];
		}
		return false;
	}

	/* For given direction makes jump (changes game state) */
	static void makeJump(int i, int j, Const.jumpType type) {
		if (isJumpLegal(i, j, type)) {
			State.pawnActive[jumpingPawnNewI(i, type)][jumpingPawnNewJ(j, type)] = true;
			State.pawnActive[beatenPawnNewI(i, type)][beatenPawnNewJ(j, type)] = false;
			State.pawnActive[i][j] = false;
			State.pawnsLeft--;
		}
	}

	/* For given jump direction calculates jumping pawn new i index */
	static int jumpingPawnNewI(int i, Const.jumpType type) {
		switch (type) {
			case LEFT, RIGHT -> {
				return i;
			}
			case UP -> {
				return i - 2;
			}
			case DOWN -> {
				return i + 2;
			}
		}
		return -1;
	}

	/* For given jump direction calculates jumping pawn new j index */
	static int jumpingPawnNewJ(int j, Const.jumpType type) {
		switch (type) {
			case LEFT -> {
				return j - 2;
			}
			case RIGHT -> {
				return j + 2;
			}
			case UP, DOWN -> {
				return j;
			}
		}
		return -1;
	}

	/* For given jump direction calculates beaten pawn new i index */
	static int beatenPawnNewI(int i, Const.jumpType type) {
		switch (type) {
			case LEFT, RIGHT -> {
				return i;
			}
			case UP -> {
				return i - 1;
			}
			case DOWN -> {
				return i + 1;
			}
		}
		return -1;
	}


	/* For given jump direction calculates beaten pawn new j index */
	static int beatenPawnNewJ(int j, Const.jumpType type) {
		switch (type) {
			case LEFT -> {
				return j - 1;
			}
			case RIGHT -> {
				return j + 1;
			}
			case UP, DOWN -> {
				return j;
			}
		}
		return -1;
	}

	public static boolean isField(int i, int j, boolean isBoardTypeEuropean) {
		if (i >= 0 && j >= 0 && i < 7 && j < 7) {
			if (isBoardTypeEuropean)
				return Const.EUROPEAN_BOARD[i][j];
			return Const.BRITISH_BOARD[i][j];
		}
		return false;
	}

	public static int windowCenterX(int objectDiameter) {
		return (Const.WINDOW_DIMENSION - objectDiameter) / 2 - Const.OFFSET;
	}

	public static int windowCenterY(int objectDiameter) {
		return (Const.WINDOW_DIMENSION - objectDiameter) / 2;
	}

	public static int evalPawnPositionX(int j) {
		int field = Const.BOARD_DIMENSION / 7;
		return windowCenterY(Const.PAWN_DIMENSION) + (j - 3) * field;
	}

	public static int evalPawnPositionY(int i) {
		int field = Const.BOARD_DIMENSION / (Const.BOARD_LENGTH + 1);
		return windowCenterX(Const.PAWN_DIMENSION) + (i - 3) * field;
	}

	/* Deactivate all buttons */
	public static void deactivateButtons(){
		State.isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
	}
}