import java.awt.*;

/* Constants class */
public class Const {
	static final int WINDOW_SIZE = 800,
			ABOUT_SIZE = 300,
			OFFSET = 37,
			BOARD_D = 600,
			PAWN_D = 40,
			BOARD_LENGTH = 7,
			PAWNS_UK = 32,
			PAWNS_EU = 36;

	static final Color OUTLINE_C = new Color(150, 230, 0),
			BOARD_C = new Color(200, 255, 80),
			BACKGROUND_C = new Color(255, 255, 180),
			FIELD_C = new Color(160, 240, 40),
			FIELD_CLICKED_C = new Color(255, 250, 170),
			PAWN_C = new Color(80, 175, 25),
			PAWN_CLICKED_C = new Color(250, 240, 20);

	enum jumpType {LEFT, RIGHT, UP, DOWN}

	static jumpType[] typeList = {jumpType.LEFT, jumpType.RIGHT, jumpType.UP, jumpType.DOWN};

	/* UK board */
	static final boolean[][] FIELDS_UK = {{false, false, true, true, true, false, false},
			{false, false, true, true, true, false, false},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{false, false, true, true, true, false, false},
			{false, false, true, true, true, false, false},
	};

	/* European board */
	static final boolean[][] FIELDS_EU = {{false, false, true, true, true, false, false},
			{false, true, true, true, true, true, false},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{false, true, true, true, true, true, false},
			{false, false, true, true, true, false, false},
	};
}