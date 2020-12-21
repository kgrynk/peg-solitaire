public class Logic {

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

	static boolean canJumpLeft(int i, int j) {
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

	static void jumpLeft(int i, int j) {
		if (canJumpLeft(i, j)) {
			State.pawns[i][j - 1] = false;
			State.pawns[i][j] = false;
			State.pawns[i][j - 2] = true;
			State.pawnsLeft--;
		}
	}

	static boolean canJumpRight(int i, int j) {
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

	static void jumpRight(int i, int j) {
		if (canJumpRight(i, j)) {
			State.pawns[i][j + 1] = false;
			State.pawns[i][j] = false;
			State.pawns[i][j + 2] = true;
			State.pawnsLeft--;

		}
	}

	static boolean canJumpUp(int i, int j) {
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

	static void jumpUp(int i, int j) {
		if (canJumpUp(i, j)) {
			State.pawns[i - 1][j] = false;
			State.pawns[i][j] = false;
			State.pawns[i - 2][j] = true;
			State.pawnsLeft--;

		}
	}

	static boolean canJumpDown(int i, int j) {
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

	static void jumpDown(int i, int j) {
		if (canJumpDown(i, j)) {
			State.pawns[i + 1][j] = false;
			State.pawns[i][j] = false;
			State.pawns[i + 2][j] = true;
			State.pawnsLeft--;

		}
	}

}
