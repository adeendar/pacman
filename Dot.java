package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * The Dot class is a wrapper class for the JavaFX Ellipse shape.
 */
public class Dot implements Collidable {

    private Ellipse dot;
    private BorderPane gamePane;
    private Game game;

    /**
     * The constructor takes in the Game so that in when collided with, a dot can modify the Game's score.
     * @param row the starting row that the dot should be at
     * @param col the column the dot should be located at
     * @param game the Game
     */
    public Dot (int row, int col, Game game) {
       this.game = game;
        this.dot = new Ellipse(col * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET, row *
                Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET, Constants.DOT_RADIUS, Constants.DOT_RADIUS);
        this.dot.setFill(Color.BISQUE);
        this.gamePane = game.getGamePane();
        this.gamePane.getChildren().add(this.dot);
    }

    /**
     * When a dot is collided with, it increments the score by 10 and removes the dot from the gamePane and from the
     * MazeSquare it was collided at.
     * @param sq the MazeSquare that the collison occured
     */
    @Override
    public void onCollision(MazeSquare sq) {
        sq.removeCollidable(this);
        this.game.updateScore(Constants.DOT_SCORE);
        this.gamePane.getChildren().remove(this.dot);
    }
}
