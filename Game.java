package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Game Class is the main logic class that sets up the timeline and actions that impact the entire game.
 */
public class Game {

    private Maze maze;
    private BorderPane gamePane;
    private Timeline timeline;
    private Pacman pacman;
    private Score score;
    private double counter;
    private GameMode mode;
    private Queue<Ghost> ghostPen;
    private Lives lives;
    private PaneOrganizer mainPane;
    private Ghost[] ghosts;
    private double penCounter;
    private int checkWinner; //only the score value of dots and energizers collided with

    /**
     * The Game constructor is associated with the PaneOrganizer so that it can get reference sof
     * lives, gamePane, and score.
     * @param mainPane the PaneOrganizer object
     */
    public Game(PaneOrganizer mainPane) {
        this.mainPane = mainPane;
        this.checkWinner = 0;
        this.lives = this.mainPane.getSideBar().getLives();
        this.gamePane = this.mainPane.getGamePane();
        this.score = this.mainPane.getSideBar().getScore();
        this.counter = Constants.CHASE_MODE_SWITCH;
        this.penCounter = Constants.PEN_DURATION;
        this.pacman = new Pacman(this.gamePane);
        this.ghostPen = new LinkedList<>();
        this.maze = new Maze(this);
        this.ghosts = new Ghost[Constants.GHOSTS_LENGTH];
        this.setUpGhosts();
        this.mode = GameMode.CHASE;
        this.setUpTimeline();
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((e) -> this.handleKeyPress(e));
    }

    /**
     * This method checks if the game is over if the player has ran out of lives or has eaten all the dots
     * and energizers.
     */
    private void checkGameOver() {
        if (this.lives.getLives() == 0) {
            this.mainPane.displayGameOver();
            this.gamePane.setFocusTraversable(false);
            this.timeline.stop();
        }
        else if (this.checkWinner == Constants.WINNER) {
            this.mainPane.displayWinner();
            this.gamePane.setFocusTraversable(false);
            this.timeline.stop();
        }
    }

    /**
     * This method is called everytime Pacman collides with a Ghost in Chase or Scatter Mode. It resets the pen and
     * timeline counters, the ghosts' positions, pacmman's location, the ghostPen, and decrements a life.
     */
    public void die() {
        this.counter = Constants.CHASE_MODE_SWITCH;
        this.penCounter = Constants.PEN_DURATION;
        for (int i = 0; i < this.ghosts.length; i++) {
            this.ghosts[i].reset();
            if (i > 0) {
                this.ghostPen.add(this.ghosts[i]); //for each ghost excluding Blinky
            }
        }
       this.pacman.setStartPosition(this.maze.pacmanStart());
       this.lives.die();
    }

    /**
     * This method sets the initial position, color, chase and scatter targets, and index of each of the 4 ghosts.
     * It also adds the 3 ghosts that need to be added to the ghost Pen.
     */
    private void setUpGhosts() {
        this.ghosts[Constants.GHOSTS_LENGTH - 4] = new Ghost(this.maze.getBlinky(), Color.RED, Constants.BLINKY_OFFSET, this, Constants.BLINKY_CORNER);

        this.ghosts[Constants.GHOSTS_LENGTH - 3] = new Ghost(this.maze.getPlinky(), Color.PINK, Constants.PINKY_OFFSET, this, Constants.PINKY_CORNER);
        this.ghostPen.add(this.ghosts[Constants.GHOSTS_LENGTH - 3]);

        this.ghosts[Constants.GHOSTS_LENGTH - 2] = new Ghost(this.maze.getInky(), Color.TURQUOISE, Constants.INKY_OFFSET, this, Constants.INKY_CORNER);
        this.ghostPen.add(this.ghosts[Constants.GHOSTS_LENGTH - 2]);

        this.ghosts[Constants.GHOSTS_LENGTH - 1] = new Ghost(this.maze.getClyde(), Color.ORANGE, Constants.CLYDE_OFFSET, this, Constants.CLYDE_CORNER);
        this.ghostPen.add(this.ghosts[Constants.GHOSTS_LENGTH - 1]);
    }

    /**
     * This method sets up the timeline, calling the update method every key frame.
     */
    private void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION),
                (ActionEvent e) -> this.update());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    /**
     * This method takes in a mazeSquare to check if any collidable elements are contained in it. If so
     * the element performs its collision action.
     * @param square the MazeSquare where Pacman is current located at
     */
    private void checkIntersection (MazeSquare square) {
        ArrayList<Collidable> elements = square.getContainedElements();
        if (!elements.isEmpty()) {
            for (int i = 0; i < elements.size(); i++){
                Collidable element = elements.get(i);
                element.onCollision(square);
            }
        }
    }

    /**
     * This method changes the game mode depending on the value of the timeline counter.
     */
    private void checkMode() {
        switch (this.mode) {
            case CHASE:
                if (this.counter <= 0) {
                    this.mode = GameMode.SCATTER;
                    this.counter = Constants.SEVEN_SECONDS;
                }
                break;
            case SCATTER: case FRIGHTEN:
                if (this.counter <= 0) {
                    this.mode = GameMode.CHASE;
                    this.counter = Constants.CHASE_MODE_SWITCH; //20 seconds
                }
                break;
        }
    }

    /**
     * This method releases a ghost from the ghost Pen every four seconds.
     */
    private void releaseFromPen () {
        if (this.penCounter <= 0 && !this.ghostPen.isEmpty()) { //release ghost every 4 seconds
            this.penCounter = Constants.PEN_DURATION;
            this.ghostPen.remove().exitPen();
        }
    }

    /**
     * This method moves all the ghosts depending on the current game mode.
     */
    private void moveGhosts() {
        for (Ghost ghost : this.ghosts) {
            switch (this.mode) {
                case CHASE:
                    ghost.chase(this.pacman.getLocation());
                    break;
                case FRIGHTEN:
                    ghost.frighten();
                    break;
                case SCATTER:
                    ghost.scatter();
                    break;
            }
        }
    }

    /**
     * This method is called every key frame and is responsible for pacman movement, ghost movement, and checking
     * for collisions and if the game is over.
     */
    private void update() {
        this.counter -= Constants.DURATION;
        this.penCounter -= Constants.DURATION;
        this.checkMode();
        this.pacman.animate(this.maze.getMaze());
        this.checkIntersection(this.maze.getMaze()[this.pacman.getRow()][this.pacman.getCol()]);
        this.releaseFromPen();
        this.moveGhosts();
        this.checkIntersection(this.maze.getMaze()[this.pacman.getRow()][this.pacman.getCol()]);
        this.checkGameOver();
    }

    /**
     * This method is responsible for controlling Pacman movement with the 4 keys (LEFT, RIGHT, DOWN, UP) and sets
     * pacman to the key pressed accordingly.
     * @param e the KeyPressed
     */
    private void handleKeyPress (KeyEvent e) {
        KeyCode key = e.getCode();
        switch (key) {
            case LEFT:
                if (this.pacman.moveValid(Direction.LEFT, this.maze.getMaze())) {
                    this.pacman.setDirection(Direction.LEFT);
                }
                break;
            case RIGHT:
                if (this.pacman.moveValid(Direction.RIGHT, this.maze.getMaze())) {
                    this.pacman.setDirection(Direction.RIGHT);
                }
                break;
            case UP:
                if (this.pacman.moveValid(Direction.UP, this.maze.getMaze())) {
                    this.pacman.setDirection(Direction.UP);
                }
                break;
            case DOWN:
                if (this.pacman.moveValid(Direction.DOWN, this.maze.getMaze())) {
                    this.pacman.setDirection(Direction.DOWN);
                }
                break;
        }
    }

    /**
     * This method is used to return the reference of pacman instantiated in this class.
     * @return Pacman
     */
    public Pacman getPacman () {
        return this.pacman;
    }

    /**
     * This method is used to return the reference of the gamePane instantiated in the PaneOrganizer class.
     * @return the gamePane
     */
    public BorderPane getGamePane() {
        return this.gamePane;
    }

    /**
     * This method is used to set the mode of the game and is used by the onCollision method in the Energizer class.
     * @param newMode the mode to set to
     */
    public void setMode(GameMode newMode) {
        this.mode = newMode;
        if (newMode == GameMode.FRIGHTEN) {
            this.counter = Constants.SEVEN_SECONDS;
        }
    }

    /**
     * This method is used to return the current game mode and is used by the Ghost's onCollision method.
     * @return the current game mode.
     */
    public GameMode getMode() {
        return this.mode;
    }

    /**
     * This method is used to return the object maze instantiated in this class and is used by the Ghost class.
     * @return the Maze
     */
    public Maze getMazeObject() {
        return this.maze;
    }

    /**
     * This method is used to add a ghost to the ghostPen ArrayList
     * @param ghost the ghost to add
     */
    public void addToPen (Ghost ghost) {
        if (ghost.equals(this.ghosts[0])) { //if blinky is being added to the pen, have to add it to a loction in the pen
            ghost.setPosition(Constants.BLINKY_PEN); //arbritrary BoardCoordinate located within the bounds of the pen
        }
        this.ghostPen.add(ghost);
    }

    /**
     * This method is used to update the overall game score and the score of just Dots and Energizers
     * @param update the number to udpate the score
     */
    public void updateScore (int update) {
        this.score.updateScore(update);
        if (update == Constants.ENERGIZER_SCORE || update == Constants.DOT_SCORE) {
            this.checkWinner += update;
        }
    }
}
