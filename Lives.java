package pacman;

/**
 * The lives class is used to keep track of the number of lives remaining.
 */
public class Lives {

    private int lives;
    private SideBar sideBar;

    /**
     * This class is associated with the sideBar class so that it can graphcially update the score label.
     * @param sideBar the side bar
     */
    public Lives (SideBar sideBar) {
        this.lives = Constants.THREE;
        this.sideBar = sideBar;
    }

    /**
     * This method returns the number of lives remaining
     * @return the number of lives remaining
     */
    public int getLives() {
        return this.lives;
    }

    public void die () {
        this.lives -= 1;
        this.sideBar.updateLives();
    }

}
