package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;

/**
 * The Maze class is used to set up the maze using the given support map.
 */
public class Maze {

    private MazeSquare[][] maze;
    private Pacman pacman;
    private BoardCoordinate blinky;
    private BoardCoordinate inky;
    private BoardCoordinate pinky;
    private BoardCoordinate clyde;
    private BoardCoordinate pacmanStart;
    private Game game;

    /**
     * The constructor takes in game so that it can pass the game into the Dots and Energizers it creates and also to
     * get the gamePane.
     * @param game the Game
     */
    public Maze(Game game) {
        this.game = game;
        this.maze = new MazeSquare[Constants.NUM_ROWS][Constants.NUM_COLS];
        this.pacman = game.getPacman();
        this.populateMaze();
    }

    /**
     * This method uses the given support map in order to dictate to the maze what should be contained at each square.
     */
    private void populateMaze() {
        CS15SquareType[][] supportMap = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                CS15SquareType squareType = supportMap[row][col];
                this.maze[row][col] = new MazeSquare(this.game.getGamePane(), row, col);
                switch (squareType) {
                    case DOT:
                        Dot dot = new Dot(row, col, this.game);
                        this.maze[row][col].addCollidable(dot);
                        break;
                    case WALL:
                        this.maze[row][col].makeWall();
                        break;
                    case ENERGIZER:
                        Energizer energizer = new Energizer(row, col, this.game);
                        this.maze[row][col].addCollidable(energizer);
                        break;
                    case GHOST_START_LOCATION:
                        this.blinky = new BoardCoordinate (row - Constants.TWO, col, false);
                        this.pinky = new BoardCoordinate (row, col - 1, false);
                        this.inky = new BoardCoordinate (row, col, false);
                        this.clyde =new BoardCoordinate (row, col+1, false);
                        break;
                    case PACMAN_START_LOCATION:
                        this.pacmanStart = new BoardCoordinate(row, col, false);
                        this.pacman.setStartPosition(pacmanStart);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * This method is used to add the Ghost to its respective location's collidable arrayList
     * @param ghost the ghost to be added
     */
    public void addGhost (Ghost ghost){
        this.maze[ghost.getRow()][ghost.getCol()].addCollidable(ghost);
    }

    /**
     * This method is used to remove the ghost from its respective location's collidable arrayList
     * @param ghost the ghost to be removed
     */
    public void removeGhost (Ghost ghost) {
        this.maze[ghost.getRow()][ghost.getCol()].removeCollidable(ghost);
    }

    /**
     * This method is used to get Blinky's starting location
     * @return Blinky's starting location
     */
    public BoardCoordinate getBlinky() {
        return this.blinky;
    }

    /**
     * This method is used to get Pinky's starting location
     * @return Pinky's starting location
     */
    public BoardCoordinate getPlinky() {
        return this.pinky;
    }

    /**
     * This method is used to get inky's starting location
     * @return inky's starting location
     */
    public BoardCoordinate getInky() {
        return this.inky;
    }

    /**
     * This method is used to get clyde's starting location
     * @return clyde's starting location
     */
    public BoardCoordinate getClyde() {
        return this.clyde;
    }

    /**
     * This method is used to get pacman's starting location
     * @return pacman's starting location
     */
    public BoardCoordinate pacmanStart() {
        return this.pacmanStart;
    }

    /**
     * This method is used to get the MazeSquare verison of the maze, not the object itself
     * @return the maze
     */
    public MazeSquare[][] getMaze() {
        return this.maze;
    }

}


