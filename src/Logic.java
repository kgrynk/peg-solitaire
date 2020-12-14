public class Logic {
	static void setStartingPos() {
		if (State.european) {
			State.pawns[1][1] = true;
			State.pawns[1][5] = true;
			State.pawns[5][1] = true;
			State.pawns[5][5] = true;
		}
		State.pawns[3][3] = false;
		//Const.FIELDS_EU[3][3] = true;
	}
}
