package pacman;

/**
 * This class is responsible for keeping track of the game's score
 */
public class Score {

    private int score;
    private SideBar sideBar;

    public Score (SideBar sideBar) {
        this.score = 0;
        this.sideBar = sideBar;
    }

    /**
     * This method gets the current score
     * @return the current score
     */
    public int getScore () {
        return this.score;
    }

    /**
     * This method udpates the score depending on the given score value
     * @param update how much to update the score by
     */
    public void updateScore (int update) {
        this.score += update;
        this.sideBar.updateScore();
    }
}
