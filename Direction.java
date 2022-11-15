package pacman;

/**
 * This class defines 4 directions that the Ghost or Pacman can move in.
 */
public enum Direction {
    LEFT, RIGHT, DOWN, UP;

    /**
     * This method gets the opposite direction given an initial direction passed in.
     * @param current the direction to return the opposite direction of
     * @return the opposite direction of current
     */
    public Direction getOpposite (Direction current) {
        switch (current) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP:
                return DOWN;
            default:
                return UP;
        }
    }
}

