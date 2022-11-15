package pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the App class where your Pacman game will start.
  * The main method of this application calls the start method. You
  * will need to fill in the start method to instantiate your game.
  *
  * Class comments here... 
  *
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        PaneOrganizer main = new PaneOrganizer();
        Scene scene = new Scene(main.getRoot(), Constants.PANE_WIDTH, Constants.PANE_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
