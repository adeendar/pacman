package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * The MazeSquare class is a wrapper class for the JavaFX Rectangle.
 */
public class MazeSquare {

    private ArrayList<Collidable> containedElements;
    private Rectangle smartSquare;
    private boolean isWall;
    private int row;
    private int col;
    private BorderPane gamePane;

    /**
     * The constructor takes in an instance of gamePane so that it can graphically add itself to the gamePane
     * @param gamePane the game pane
     * @param row the row to add itself
     * @param col the column to add itself
     */
    public MazeSquare(BorderPane gamePane, int row, int col) {
        this.row = row;
        this.col = col;
        this.isWall = false;
        this.smartSquare = new Rectangle(this.col * Constants.SQUARE_SIZE, this.row * Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
        this.smartSquare.setFill(Color.BLACK);
        this.containedElements = new ArrayList<>(); //arrayList keeps track of the elements inside a mazeSquare
        this.gamePane = gamePane;
        this.gamePane.getChildren().add(smartSquare);
        this.smartSquare.toBack();
    }

    /**
     * This method makes a wall is specificed by the CS15SqureType in Maze's populate maze method
     */
    public void makeWall () {
        this.isWall = true;
        this.smartSquare.setFill(Color.DARKBLUE);
    }

    /**
     * This method is used to add a collidable to the MazeSquare
     * @param elementToAdd the Collidable object to add
     */
    public void addCollidable (Collidable elementToAdd) {
        this.containedElements.add(elementToAdd);
    }

    /**
     * This method is used to remove a collidable to the MazeSquare
     * @param elementToRemove the Collidable object to remove
     */
    public void removeCollidable (Collidable elementToRemove) {
        this.containedElements.remove(elementToRemove);
    }

    /**
     * This method is used to tell if the MazeSquare is a wall
     * @return if the given MazeSquare is a wall
     */
    public boolean isWall() {
        return this.isWall;
    }

    /**
     * This method is used to get the elements contained in the MazeSquare
     * @return the Collidable elements in the MazeSquare
     */
    public ArrayList<Collidable> getContainedElements () {
        return this.containedElements;
    }
}
