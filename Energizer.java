package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * The Energizer class is a wrapper class for JavaFX rectangle.
 */
public class Energizer implements Collidable {

    private Ellipse energizer;
    private BorderPane gamePane;
    private Game game;

    public Energizer (int row, int col, Game game) {
        this.energizer = new Ellipse(col * Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET, row *
                Constants.SQUARE_SIZE + Constants.ELLIPSE_OFFSET, Constants.ENERGIZER_RADIUS, Constants.ENERGIZER_RADIUS);
        this.energizer.setFill(Color.BISQUE);
        this.game = game;
        this.gamePane = game.getGamePane();
        this.gamePane.getChildren().add(this.energizer);
    }

    /**
     * When an energizer is collided with it sets the gameMode into FRIGTHEN, updates the score, and removes the
     * energizer from the gamePane.
     * @param sq the MazeSquare where the collision occured
     */
    @Override
    public void onCollision (MazeSquare sq) {
        this.gamePane.getChildren().remove(this.energizer);
        this.game.setMode(GameMode.FRIGHTEN);
        this.game.updateScore(Constants.ENERGIZER_SCORE);
        sq.removeCollidable(this);
    }

}
