import java.awt.*;

/* Constants class */
public class Const {
	static final int WINDOW_DIMENSION = 800,
			OFFSET = 37,
			BOARD_DIMENSION = 600,
			PAWN_DIMENSION = 40,
			BOARD_LENGTH = 7,
			NUMBER_OF_PAWNS_BRITISH = 32,
			NUMBER_OF_PAWNS_EUROPEAN = 36;

	static final Color OUTLINE_COLOR = new Color(150, 230, 0),
			BOARD_COLOR = new Color(200, 255, 80),
			BACKGROUND_COLOR = new Color(255, 255, 180),
			FIELD_COLOR = new Color(160, 240, 40),
			FIELD_CLICKED_COLOR = new Color(255, 250, 170),
			PAWN_COLOR = new Color(80, 175, 25),
			PAWN_CLICKED_COLOR = new Color(250, 240, 20);

	enum jumpType {LEFT, RIGHT, UP, DOWN}

	static jumpType[] jumpTypes = {jumpType.LEFT, jumpType.RIGHT, jumpType.UP, jumpType.DOWN};

	/* British board */
	static final boolean[][] BRITISH_BOARD = {{false, false, true, true, true, false, false},
			{false, false, true, true, true, false, false},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{false, false, true, true, true, false, false},
			{false, false, true, true, true, false, false},
	};

	/* European board */
	static final boolean[][] EUROPEAN_BOARD = {{false, false, true, true, true, false, false},
			{false, true, true, true, true, true, false},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{true, true, true, true, true, true, true},
			{false, true, true, true, true, true, false},
			{false, false, true, true, true, false, false},
	};
}