package pacman;

/**
 * This interface is implemented by all objects that can collide with pacman (Ghost, Energizer, Dot).
 */
public interface Collidable {

    /**
     * The onCollision method is called when Pacman and a Colllidable object are in the same MazeSquare. It
     * is overriden by all the classes that implement the Collidable interface.
     * @param loc the MazeSquare of collision
     */
    void onCollision(MazeSquare loc);
}
