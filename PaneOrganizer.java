package pacman;

import doodlejump.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * The PaneOrganizder class is the top level graphical class and contains the game
 */
public class PaneOrganizer {

    private BorderPane root;
    private SideBar sideBar;
    private BorderPane sidePane;
    private BorderPane gamePane;

    /**
     * The constructor creates panes for the sideBar and the game.
     */
    public PaneOrganizer () {
        this.root = new BorderPane();
        this.sidePane = new BorderPane();
        root.setBottom(this.sidePane);
        this.sideBar = new SideBar(this.sidePane);
        this.gamePane = new BorderPane();
        root.setCenter(this.gamePane);
        Game game = new Game(this);
    }

    /**
     * This method displays the game over label when the condition is met in Game.
     */
    public void displayGameOver() {
        Label gameOverLabel = new Label("GAME OVER!");
        gameOverLabel.setStyle("-fx-font: italic bold 30px arial, serif;-fx-text-alignment: center;-fx-text-fill: red;");
        VBox labelBox = new VBox(gameOverLabel);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setPrefHeight(Constants.PANE_HEIGHT);
        labelBox.setPrefWidth(Constants.PANE_WIDTH);
        this.gamePane.setCenter(labelBox);
    }

    /**
     * This method displays the winner label when the condition is met in the game.
     */
    public void displayWinner() {
        Label winnerLabel = new Label("WINNER!");
        winnerLabel.setStyle("-fx-font: italic bold 30px arial, serif;-fx-text-alignment: center;-fx-text-fill: yellow;");
        VBox labelBox = new VBox(winnerLabel);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setPrefHeight(Constants.PANE_HEIGHT);
        labelBox.setPrefWidth(Constants.PANE_WIDTH);
        this.gamePane.setCenter(labelBox);
    }

    /**
     * This method gets the game pane
     * @return the game pane
     */
    public BorderPane getGamePane () {
        return this.gamePane;
    }

    /**
     * This method gets the side bar
     * @return the side bar
     */
    public SideBar getSideBar () {
        return this.sideBar;
    }

    /**
     * This method gets the root border pane
     * @return the root border pane
     */
    public BorderPane getRoot () {
        return this.root;
    }
}
