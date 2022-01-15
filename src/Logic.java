import java.util.Arrays;

/* Game logic procedures */
public class Logic {

	/* Initialize game state */
	static void setStartingPos() {
		if (State.european) {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				State.pawns[i] = Arrays.copyOf(Const.FIELDS_EU[i], Const.FIELDS_EU[i].length);
			}
		} else {
			for (int i = 0; i < Const.BOARD_LENGTH; i++) {
				State.pawns[i] = Arrays.copyOf(Const.FIELDS_UK[i], Const.FIELDS_UK[i].length);
			}
		}
		State.pawns[3][3] = false;
		if (State.european) {
			State.pawnsLeft = Const.PAWNS_EU;
		} else {
			State.pawnsLeft = Const.PAWNS_UK;
		}
	}

	/* For given coordinates returns jump direction */
	static Const.jumpType makeValidJump(int i, int j, int iJump, int jJump) {
		for (Const.jumpType type : Const.typeList) {
			if (iJump == jumpPosI(i, type) && jJump == jumpPosJ(j, type)) {
				jump(i, j, type);
				return type;
			}
		}
		return null;
	}

	/* Returns true if jump is legal */
	static boolean canJump(int i, int j, Const.jumpType type) {
		if (type == null) {
			return false;
		}
		if (isField(i, j, State.european)
				&& isField(jumpPosI(i, type), jumpPosJ(j, type), State.european)
				&& isField(midPosI(i, type), midPosJ(j, type), State.european)) {
			return !State.pawns[jumpPosI(i, type)][jumpPosJ(j, type)]
					&& State.pawns[midPosI(i, type)][midPosJ(j, type)];
		}
		return false;
	}

	/* For given direction makes jump (changes game state) */
	static void jump(int i, int j, Const.jumpType type) {
		if (canJump(i, j, type)) {
			State.pawns[jumpPosI(i, type)][jumpPosJ(j, type)] = true;
			State.pawns[midPosI(i, type)][midPosJ(j, type)] = false;
			State.pawns[i][j] = false;
			State.pawnsLeft--;
		}
	}

	/* For given jump direction calculates dx */
	static int jumpPosI(int i, Const.jumpType type) {
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

	/* For given jump direction calculates dy */
	static int jumpPosJ(int j, Const.jumpType type) {
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

	/* For given jump direction calculates beaten pawn dx */
	static int midPosI(int i, Const.jumpType type) {
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


	/* For given jump direction calculates beaten pawn dy */
	static int midPosJ(int j, Const.jumpType type) {
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

	public static boolean isField(int i, int j, boolean european) {
		if (i >= 0 && j >= 0 && i < 7 && j < 7) {
			if (european)
				return Const.FIELDS_EU[i][j];
			return Const.FIELDS_UK[i][j];
		}
		return false;
	}

	public static int centerPosY(int objDiameter) {
		return (Const.WINDOW_SIZE - objDiameter) / 2 - Const.OFFSET;
	}

	public static int centerPosX(int objDiameter) {
		return (Const.WINDOW_SIZE - objDiameter) / 2;
	}

	public static int evalPawnPosX(int j) {
		int field = Const.BOARD_D / 7;
		return centerPosX(Const.PAWN_D) + (j - 3) * field;
	}

	public static int evalPawnPosY(int i) {
		int field = Const.BOARD_D / (Const.BOARD_LENGTH + 1);
		return centerPosY(Const.PAWN_D) + (i - 3) * field;
	}

	/* Deactive all buttons */
	public static void noAcitveButtons(){
		State.isButtonActive = new boolean[Const.BOARD_LENGTH][Const.BOARD_LENGTH];
	}
}