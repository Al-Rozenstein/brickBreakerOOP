package bricker.movement_strategies;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Class for the UserMovementStrategy, implements MovementStrategy interface.
 */
public class UserMovementStrategy implements MovementStrategy {
    private UserInputListener inputListener;


    /**
     * Constructs a new AIMovementStrategy object.
     * @param inputListener Reads the user's keyboard keys pressed.
     */
    public UserMovementStrategy(UserInputListener inputListener) {
        this.inputListener = inputListener;
    }


    @Override
    /**
     * Calculating the moving direction of the paddle according to the user's keyboard keys pressed.
     * @param owner this
     * @return The Vector2 representing the moving direction.
     */
    public Vector2 calcMovementDir(GameObject owner) {
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        return movementDir;
    }
}
