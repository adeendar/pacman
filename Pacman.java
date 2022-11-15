package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * The Pacman class is a wrapper class for JavaFX rectangle and is used to define aspects of Pacman's movement and
 * get its attributes for use in other classes.
 */
public class Pacman {

    private Ellipse body;
    private Direction direction;
    private int row;
    private int col;

    /**
     * The constructor takes in an instance of gamePane so that it can add itself graphically
     * @param gamePane the game pane
     */
    public Pacman(BorderPane gamePane) {
        this.row = 0;
        this.col = 0;
        this.body = new Ellipse(col * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET,
                row * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET, Constants.PACMAN_RADIUS, Constants.PACMAN_RADIUS);
        this.body.setFill(Color.YELLOW);
        gamePane.getChildren().add(this.body);
        this.direction = Direction.UP;
    }

    /**
     * This method is used to get the location of pacman
     * @return pacman's location
     */
    public BoardCoordinate getLocation () {
        BoardCoordinate pacmanLoc = new BoardCoordinate(this.row, this.col, true);
        return pacmanLoc;
    }


    /**
     * This method is used to reset pacman's location to its starting position.
     * @param start pacman's starting positon
     */
    public void setStartPosition(BoardCoordinate start){
        this.row = start.getRow();
        this.col = start.getColumn();
        this.body.setCenterX(this.col * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
        this.body.setCenterY(this.row * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
    }

    /**
     * This method is used to move pacman and accounts for wrapping.
     * @param maze the maze
     */
    public void animate(MazeSquare[][] maze) {
        int i = this.row;
        int j = this.col;
        switch (this.direction) {
            case LEFT:
                if (this.moveValid(Direction.LEFT, maze)) {
                    this.body.setCenterX((j - 1) * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
                    this.col = (j - 1);
                    if (this.col == 0) {
                        this.body.setCenterX((Constants.NUM_COLS - 1) * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
                        this.col = (Constants.NUM_COLS -1);
                    }
                }
                break;
            case RIGHT:
                if (this.moveValid(Direction.RIGHT, maze)) {
                    this.body.setCenterX((j + 1) * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
                    this.col = (j + 1);
                    if (this.col == Constants.NUM_COLS - 1) {
                        this.body.setCenterX(Constants.ELLIPSE_OFFSET);
                        this.col = (0);
                    }
                }
                break;
            case UP:
                if (this.moveValid(Direction.UP, maze)) {
                    this.body.setCenterY((i - 1) * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
                    this.row = (i - 1);
                }
                break;
            case DOWN:
                if (this.moveValid(Direction.DOWN, maze)) {
                    this.body.setCenterY((i + 1) * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET);
                    this.row = (i + 1);
                }
                break;
        }
    }

    /**
     * This method is used to make sure that pacman is not moving into walls
     * @param direction the direction pacman is to move in
     * @param maze the maze
     * @return wether pacman can move in the given direction
     */
    public boolean moveValid(Direction direction, MazeSquare [][] maze) {
        int i = this.row;
        int j = this.col;
        boolean moveValid = false;
        switch (direction) {
            case LEFT:
                if (!maze[i][j-1].isWall()){
                    moveValid = true;
                }
                break;
            case RIGHT:
                if (!maze[i][j+1].isWall()){
                    moveValid = true;
                }
                break;
            case UP:
                if (!maze[i-1][j].isWall()){
                    moveValid = true;
                }
                break;
            case DOWN:
                if (!maze[i+1][j].isWall()){
                    moveValid = true;
                }
                break;
        }
        return moveValid;
    }

    /**
     * This method is used to set pacman's direction and is called in Game's handleKeyPressed method
     * @param newDir the direction to set pacman
     */
    public void setDirection(Direction newDir) {
        this.direction = newDir;
    }

    /**
     * This method gets pacman's current row
     * @return pacman's current row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * This method get pacman's current column
     * @return pacman's current column
     */
    public int getCol() {
        return this.col;
    }
}

