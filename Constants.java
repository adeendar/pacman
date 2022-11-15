package pacman;

/**
 * The Constants class defines several static variables for use throughout my program.
 */
public class Constants {

    public static final int NUM_ROWS = 23;
    public static final int NUM_COLS = 23;

    //constants specifying pixel size and offsets
    public static final double SQUARE_SIZE = 24;
    public static final int DOT_RADIUS = 3;
    public static final int ENERGIZER_RADIUS = 6;
    public static final int PACMAN_RADIUS = 10;
    public static final int PANE_WIDTH = 552;
    public static final int PANE_HEIGHT = 650;
    public static final int ELLIPSE_OFFSET = 12;
    public static final int TWO = 2;
    public static final int GHOSTS_LENGTH = 4;

    public static final int DOT_SCORE = 10;
    public static final int ENERGIZER_SCORE = 100;
    public static final int GHOST_SCORE = 200;
    public static final int WINNER = 2220;

    public static final double DURATION = 0.2;
    public static final int PEN_DURATION = 4;
    public static final int CHASE_MODE_SWITCH = 20;
    public static final int SEVEN_SECONDS = 7;

    public static final int GHOST_STARTING_ROW = 8;
    public static final int GHOST_STARTING_COL = 11;

    //scatter mode location constants
    public static final BoardCoordinate BLINKY_CORNER = new BoardCoordinate(0, 23, true);
    public static final BoardCoordinate PINKY_CORNER = new BoardCoordinate(0, 0, true);
    public static final BoardCoordinate INKY_CORNER = new BoardCoordinate(23, 23, true);
    public static final BoardCoordinate CLYDE_CORNER = new BoardCoordinate(0, 23, true);

    //chase mode offsets
    public static final BoardCoordinate BLINKY_OFFSET = new BoardCoordinate(0,0, true);
    public static final BoardCoordinate PINKY_OFFSET = new BoardCoordinate(1,-3, true);
    public static final BoardCoordinate INKY_OFFSET = new BoardCoordinate(0,2, true);
    public static final BoardCoordinate CLYDE_OFFSET = new BoardCoordinate(-4,0, true);
    public static final BoardCoordinate BLINKY_PEN = new BoardCoordinate(10, 10, false);

    public static final int THREE = 3;
    public static final int BUTTON_PANE_WIDTH = 50;
    public static final int BUTTON_PANE_HEIGHT = 25;
    public static final int FONT_SIZE = 18;

}

