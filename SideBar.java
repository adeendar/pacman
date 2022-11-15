package pacman;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * The SideBar class is repsonsible for creating and maintaing the score, number of lives, and the quit button.
 */
public class SideBar {

    private BorderPane sidePane;
    private Score score;
    private Label scoreLabel; //instance variable because it changes throughout
    private Label livesLabel; //instance variable because it changes throughout the program
    private Lives lives;

    public SideBar(BorderPane sidePane) {
        this.score = new Score(this);
        this.lives = new Lives(this);
        this.sidePane = sidePane;
        this.setUpScorePane();
        this.setUpLivesPane();
        this.makeQuitButton();
    }

    /**
     * This method is responsible for creating the quit button.
     */
    public void makeQuitButton() {
        BorderPane buttonPane = new BorderPane();
        buttonPane.setStyle("-fx-background-color: darkblue");
        buttonPane.setPrefSize(Constants.PANE_WIDTH/ Constants.THREE, Constants.PANE_HEIGHT - Constants.PANE_WIDTH);

        Button quitButton = new Button("Quit");
        quitButton.setPrefSize(Constants.BUTTON_PANE_WIDTH, Constants.BUTTON_PANE_HEIGHT);
        quitButton.setStyle("-fx-background-color: white");
        buttonPane.setCenter(quitButton);
        this.sidePane.setRight(buttonPane);

        quitButton.setOnAction((ActionEvent e) -> System.exit(0));
        quitButton.setFocusTraversable(false);
    }

    /**
     * This method is responsible for setting up the lives label and pane.
     */
    public void setUpLivesPane() {
        BorderPane livesPane = new BorderPane();
        livesPane.setStyle("-fx-background-color: darkblue;");
        livesPane.setPrefSize(Constants.PANE_WIDTH/Constants.THREE, Constants.PANE_HEIGHT - Constants.PANE_WIDTH);
        this.livesLabel = new Label("Lives " + this.lives.getLives());
        this.livesLabel.setTextFill(Color.WHITE);
        this.livesLabel.setFont(new Font ("Namco", Constants.FONT_SIZE));
        this.livesLabel.setTextAlignment(TextAlignment.LEFT);
        livesPane.setCenter(this.livesLabel);
        this.sidePane.setCenter(livesPane);
    }

    /**
     * This method sets up the score Label and pane.
     */
    public void setUpScorePane() {
        BorderPane scorePane = new BorderPane();
        scorePane.setStyle("-fx-background-color: darkblue;");
        scorePane.setPrefSize(Constants.PANE_WIDTH / Constants.THREE, Constants.PANE_HEIGHT - Constants.PANE_WIDTH);
        this.scoreLabel = new Label("Score: " + this.score.getScore());
        this.scoreLabel.setTextFill(Color.WHITE);
        this.scoreLabel.setFont(new Font("Namco",Constants.FONT_SIZE));
        this.scoreLabel.setTextAlignment(TextAlignment.RIGHT);
        scorePane.setCenter(this.scoreLabel);
        this.sidePane.setLeft(scorePane);
    }

    /**
     * This method updates the lives label
     */
    public void updateLives() {
        this.livesLabel.setText("Lives: " + this.lives.getLives());
    }

    /**
     * This method updates the score label
     */
    public void updateScore() {
        this.scoreLabel.setText("Score: " + this.score.getScore());
    }

    /**
     * This method returns the game's score
     * @return the game score
     */
    public Score getScore () {
        return this.score;
    }

    /**
     * This method returns the game's lives
     * @return the game lives
     */
    public Lives getLives () {
        return this.lives;
    }
}
