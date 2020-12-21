import java.util.Arrays;

public class Logic {
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

	static Const.jumpType makeValidJump(int i, int j, int iJump, int jJump) {
		Const.jumpType type = null;
		if (i == iJump) {
			if (j - 2 == jJump) {
				type = Const.jumpType.LEFT;
			} else if (j + 2 == jJump) {
				type = Const.jumpType.RIGHT;
			}
		} else if (j == jJump) {
			if (i - 2 == iJump) {
				type = Const.jumpType.UP;
			} else if (i + 2 == iJump) {
				type = Const.jumpType.DOWN;
			}
		}
		jump(i, j, type);
		return type;
	}

	static boolean canJump(int i, int j, Const.jumpType type) {
		if (type == null) {
			return false;
		}
		switch (type) {
			case LEFT -> {
				if (j < 2) {
					return false;
				}
				if (j < 4 && (i < 2 || i > 4)) {
					if (!(((i == 1 || i == 5) && j == 3) && State.european)) {
						return false;
					}
				}
				return !State.pawns[i][j - 2] && State.pawns[i][j - 1];
			}
			case RIGHT -> {
				if (j > 4) {
					return false;
				}
				if (j > 2 && (i < 2 || i > 4)) {
					if (!(((i == 1 || i == 5) && j == 3) && State.european)) {
						return false;
					}
				}

				return !State.pawns[i][j + 2] && State.pawns[i][j + 1];
			}
			case UP -> {
				if (i < 2) {
					return false;
				}
				if (i < 4 && (j < 2 || j > 4)) {
					if (!(((j == 1 || j == 5) && i == 3) && State.european)) {
						return false;
					}
				}

				return !State.pawns[i - 2][j] && State.pawns[i - 1][j];
			}
			case DOWN -> {
				if (i > 4) {
					return false;
				}
				if (i > 2 && (j < 2 || j > 4)) {
					if (!(((j == 1 || j == 5) && i == 3) && State.european)) {
						return false;
					}
				}

				return !State.pawns[i + 2][j] && State.pawns[i + 1][j];
			}
		}
		return false;
	}

	static boolean jump(int i, int j, Const.jumpType type) {
		if (canJump(i, j, type)) {
			switch (type) {
				case LEFT -> {
					State.pawns[i][j - 1] = false;
					State.pawns[i][j - 2] = true;
				}
				case RIGHT -> {
					State.pawns[i][j + 1] = false;
					State.pawns[i][j + 2] = true;
				}
				case UP -> {
					State.pawns[i - 1][j] = false;
					State.pawns[i - 2][j] = true;
				}
				case DOWN -> {
					State.pawns[i + 1][j] = false;
					State.pawns[i + 2][j] = true;
				}
			}
			State.pawns[i][j] = false;
			State.pawnsLeft--;
			return true;
		}
		return false;
	}
}