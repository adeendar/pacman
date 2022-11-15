package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Ghost class is a wrapper class for JavaFX rectangle and defines several methods to affect the ghost's
 * behavior.
 */
public class Ghost implements Collidable {

    private Rectangle ghost;
    private BorderPane gamePane;
    private int row;
    private int col;
    private Maze maze;
    private Direction direction;
    private Color originalColor;
    private BoardCoordinate scatterTarget;
    private BoardCoordinate chaseTarget;
    private BoardCoordinate start;
    private Game game;

    /**
     * The Constructor takes in game as a parameter to get the reference of gamePane and Maze.
     * @param start the starting location
     * @param color the original color
     * @param chase the chase target
     * @param game the Game
     * @param scatter the scatter target
     */
    public Ghost (BoardCoordinate start, Color color, BoardCoordinate chase, Game game, BoardCoordinate scatter) {
        this.game = game;
        this.start = start;
        this.row = start.getRow();
        this.col = start.getColumn();
        this.ghost = new Rectangle(col * Constants.SQUARE_SIZE, row * Constants.SQUARE_SIZE,
                Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
        this.ghost.setFill(color);
        this.ghost.setStroke(Color.BLACK);
        this.gamePane = game.getGamePane();
        this.maze = game.getMazeObject();
        this.direction = Direction.UP;
        this.gamePane.getChildren().add(this.ghost);
        this.originalColor = color;
        this.chaseTarget = chase;
        this.scatterTarget = scatter;
        this.maze.addGhost(this);
    }

    /**
     * This method is used to reset the Ghost's position to its starting location.
     */
    public void reset() {
        this.maze.removeGhost(this);
        this.setPosition(this.start);
        this.maze.addGhost(this);
    }

    /**
     * This method is used to set the loation of a ghost once it is removed from the ghostPen.
     */
    public void exitPen() {
        this.maze.removeGhost(this);
        this.ghost.setX(Constants.GHOST_STARTING_COL * Constants.SQUARE_SIZE);
        this.ghost.setY(Constants.GHOST_STARTING_ROW * Constants.SQUARE_SIZE);
        this.row = Constants.GHOST_STARTING_ROW;
        this.col = Constants.GHOST_STARTING_COL;
        this.maze.addGhost(this);
    }

    /**
     * This method is used to move the ghost in chase mode and calls the ghostBFS method, passing in pacman's current location
     * manipulated by the ghost's specific chase target.
     * @param pacmanLoc pacman's current location
     */
    public void chase (BoardCoordinate pacmanLoc) {
        BoardCoordinate target = new BoardCoordinate(pacmanLoc.getRow() + this.chaseTarget.getRow(),
                pacmanLoc.getColumn() + this.chaseTarget.getColumn(), true);
        this.setColor(this.originalColor);
        this.move(this.ghostBFS(target));
    }

    /**
     * This method is used to move the ghost in scatter mode.
     */
    public void scatter () {
        this.setColor(this.originalColor);
        this.move(this.ghostBFS(this.scatterTarget));
    }

    /**
     * This method is used to move the ghost in frighten mode and gets a random direction for the ghost
     * to move in at every intersection.
     */
    public void frighten () {
        this.setColor(Color.LIGHTBLUE);
        BoardCoordinate start = new BoardCoordinate(this.row, this.col, false);
        ArrayList<Direction> directions = new ArrayList<>();
        if (this.checkValidity(Direction.LEFT, start) && this.checkOppDirection(Direction.LEFT)) {
            directions.add(Direction.LEFT);
        }
        if (this.checkValidity(Direction.RIGHT, start) && this.checkOppDirection(Direction.RIGHT)) {
            directions.add(Direction.RIGHT);
        }
        if (this.checkValidity(Direction.UP, start) && this.checkOppDirection(Direction.UP)) {
            directions.add(Direction.UP);
        }
        if (this.checkValidity(Direction.DOWN, start)  && this.checkOppDirection(Direction.DOWN)) {
            directions.add(Direction.DOWN);
        }
        int random = (int) (Math.random() * directions.size());
        this.move(directions.get(random));
    }

    /**
     * This method is intended to handle wrapping for if a ghost moves out of bounds.
     */
    private void handleWrapping () {
        if (this.col == 22 && this.direction == Direction.RIGHT) {
            this.setPosition(new BoardCoordinate(this.row, 1, false));
        } else if (this.col == 0 && this.direction == Direction.LEFT) {
            this.setPosition(new BoardCoordinate(this.row, 21, false));
        }
    }

    /**
     * This method is used by all modes to move the ghost based on the direction input to this method.
     */
    private void move(Direction direction) {
        this.direction = direction;
        this.maze.removeGhost(this);
        this.handleWrapping();
            switch (direction) {
                case LEFT:
                        this.col -= 1;
                    break;
                case RIGHT:
                        this.col += 1;
                    break;
                case UP:
                    this.row -= 1;
                    break;
                case DOWN:
                    this.row += 1;
                    break;
            }
            BoardCoordinate newPos = new BoardCoordinate(this.row, this.col, false);
            this.setPosition(newPos);
            this.maze.addGhost(this);
    }

    /**
     * This method is used to check if the direction to be moved in is the opposite direction of the
     * ghost's current direction and is used to prevent 180 degree turns.
     * @param direction the direction to check
     * @return if the direction passed in is opposite to the current direciton
     */
    private boolean checkOppDirection (Direction direction) {
        if (this.direction.getOpposite(this.direction) != direction) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method is my BFS algorithm and is used to find which direction a ghost should move to be closest to pacman
     * @param target the target location (offset from Pacman's)
     * @return which direction the ghost should turn
     */
    private Direction ghostBFS(BoardCoordinate target) {
        Queue<BoardCoordinate> queue = new LinkedList<>();
        Direction[][] directionTracker = new Direction[Constants.NUM_ROWS][Constants.NUM_COLS]; // array to map directions of valid neighbors
        BoardCoordinate closestSquare = new BoardCoordinate(this.row, this.col, false); // ghost's initial position
        this.getNeighbors(directionTracker, closestSquare, queue, 1); //get valid neighbors of starting ghost square
        double smallestDistance = this.calcDistance(closestSquare, target);
        while (!queue.isEmpty()) {
            BoardCoordinate current = queue.remove();
            this.getNeighbors(directionTracker, current, queue, 2); //counter keeps track of whether getting original neighbors or setting the direction of new neighbors
            double distance = this.calcDistance(current, target);
            if (distance < smallestDistance) {
                closestSquare = current;
                smallestDistance = distance;
            }
        }
        return directionTracker[closestSquare.getRow()][closestSquare.getColumn()];
    }

    /**
     * This method is used to queue the valid neighbors given a ghosts square and is modified to be used inside or outside the while loop
     * @param directionTracker the array of directions
     * @param start the starting square to check for neighbors
     * @param queue the queue of valid neighbors
     * @param counter wether or not to check for opposite direction (inside or outside the while loop)
     */
    private void getNeighbors(Direction[][] directionTracker, BoardCoordinate start, Queue<BoardCoordinate> queue, int counter) {
        int row = start.getRow();
        int col = start.getColumn();
        Direction startDirection = directionTracker[row][col];
        if (col < Constants.NUM_COLS - 1 && col > 0)  { //used to prevent arrayIndexOutOfBounds excpetions
            if (this.checkValidity(Direction.LEFT, start) && directionTracker[row][col - 1] == null) {
                if (counter == 1 && this.checkOppDirection(Direction.LEFT)) {
                    directionTracker[row][col - 1] = Direction.LEFT;
                }
                if (counter == 2) {
                    directionTracker[row][col - 1] = startDirection;
                }
                queue.add(new BoardCoordinate(row, col - 1, false));
            }

            if (this.checkValidity(Direction.RIGHT, start) && directionTracker[row][col + 1] == null) {
                if (counter == 1 && this.checkOppDirection(Direction.RIGHT)) {
                    directionTracker[row][col + 1] = Direction.RIGHT;
                }
                if (counter == 2) {
                    directionTracker[row][col + 1] = startDirection;
                }
                queue.add(new BoardCoordinate(row, col + 1, false));
            }

            if (this.checkValidity(Direction.DOWN, start) && directionTracker[row + 1][col] == null) {
                if (counter == 1 && this.checkOppDirection(Direction.DOWN)) {
                    directionTracker[row + 1][col] = Direction.DOWN;
                }
                if (counter == 2) {
                    directionTracker[row + 1][col] = startDirection;
                }
                queue.add(new BoardCoordinate(row + 1, col, false));
            }

            if (this.checkValidity(Direction.UP, start) && directionTracker[row - 1][col] == null) {
                if (counter == 1 && this.checkOppDirection(Direction.UP)) {
                    directionTracker[row - 1][col] = Direction.UP;
                }
                if (counter == 2) {
                    directionTracker[row - 1][col] = directionTracker[start.getRow()][start.getColumn()];
                }
                queue.add(new BoardCoordinate(row - 1, col, false));
            }
        }

        //the lines below are supposed to handle wrapping but i'm still running into the infinite while loop error
        //MOVING LEFT
        if (col == 0 && counter == 2 && startDirection == Direction.LEFT) {
            if (directionTracker[row][Constants.NUM_COLS - 1] == null) {
                directionTracker[row][Constants.NUM_COLS - 1] = startDirection;
                queue.add(new BoardCoordinate(row, Constants.NUM_COLS - 1, false));
            }
        }

        if (col == Constants.NUM_COLS - 1 && counter == 2 && startDirection == Direction.LEFT) {
            if (directionTracker[row][Constants.NUM_COLS - 2] == null) {
                directionTracker[row][Constants.NUM_COLS - 2] = startDirection;
                queue.add(new BoardCoordinate(row, Constants.NUM_COLS - 2, false));
            }
        }

        //moving RIGHT
        if (col == Constants.NUM_COLS -1 && counter == 2 && startDirection == Direction.RIGHT) {
            if (directionTracker[row][0] == null) {
                directionTracker[row][0] = startDirection;
                queue.add(new BoardCoordinate(row, 0, false));
            }
        }

        if (col == 0 && counter == 2 && startDirection == Direction.RIGHT) {
            if (directionTracker[row][1] == null) {
                directionTracker[row][1] = startDirection;
                queue.add(new BoardCoordinate(row, 1, false));
            }
        }
    }

    /**
     * This method checks if the direction the ghost may move into is a wall or not and returns the correct boolean
     * @param direction the direction to move in
     * @param start the ghost's starting location
     * @return if the ghost will or will not be moving into a wall
     */
    private boolean checkValidity(Direction direction, BoardCoordinate start) {
        int i = start.getRow();
        int j = start.getColumn();
        boolean moveValid = false;
        MazeSquare[][] maze = this.maze.getMaze();
            switch (direction) {
                case LEFT:
                    if (!maze[i][j - 1].isWall()) {
                        moveValid = true;
                    }
                    break;
                case RIGHT:
                    if (!maze[i][j + 1].isWall()) {
                        moveValid = true;
                    }
                    break;
                case UP:
                    if (!maze[i - 1][j].isWall()) {
                        moveValid = true;
                    }
                    break;
                case DOWN:
                    if (!maze[i + 1][j].isWall()) {
                        moveValid = true;
                    }
                    break;
            }
            return moveValid;
        }


    /**
     * This method is used to calculate the distance between the target and the current MazeSquare to find the shortest
     * distance the ghost must travel in order to reach its target.
     * @param current the ghost's current location
     * @param target the target location
     * @return the distance between the current location and target location
     */
    private double calcDistance(BoardCoordinate current, BoardCoordinate target) {
        return Math.sqrt(Math.pow((current.getColumn() - target.getColumn()), 2) +
                Math.pow((current.getRow() - target.getRow()), 2));
    }

    /**
     * This method is called whenever Pacman and a Ghost are in the same MazeSauare depending on the current game mode.
     * @param sq the MazeSauare where collision occured
     */
    @Override
    public void onCollision(MazeSquare sq) {
        sq.removeCollidable(this);
        switch (this.game.getMode()) {
            case CHASE:
                this.game.die();
                break;
            case FRIGHTEN:
                this.gamePane.getChildren().remove(this.ghost);
                this.game.addToPen(this);
                this.setPosition(this.start);
                this.gamePane.getChildren().add(this.ghost);
                this.game.updateScore(Constants.GHOST_SCORE);
                break;
            case SCATTER:
                this.game.die();
                break;
        }
    }

    /**
     * This method is used to set the position of a ghost
     * @param position the position to set
     */
    public void setPosition (BoardCoordinate position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        this.ghost.setX(this.col * Constants.SQUARE_SIZE);
        this.ghost.setY(this.row * Constants.SQUARE_SIZE);
    }

    /**
     * This method returns the ghost's current row
     * @return the ghost's row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * This method returns the ghost's current column
     * @return the ghost's column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * This method is used to set the color of the ghost depending on its current mode
     * @param newColor the color to set
     */
    private void setColor(Color newColor) {
        this.ghost.setFill(newColor);
    }
}

