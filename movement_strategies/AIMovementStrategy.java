package bricker.movement_strategies;

import danogl.GameObject;
import danogl.util.Vector2;

/**
 * Class for the AIMovementStrategy, implements MovementStrategy interface.
 */
public class AIMovementStrategy implements MovementStrategy {

    private GameObject objectToFollow;

    /**
     * Constructs a new AIMovementStrategy object.
     * @param objectToFollow The ball the AIPaddle follows.
     */
    public AIMovementStrategy(GameObject objectToFollow) {
        this.objectToFollow = objectToFollow;
    }

    /**
     * Calculating the moving direction of the paddle according to the ball's location.
     * @param owner this
     * @return The Vector2 representing the moving direction.
     */
    @Override
    public Vector2 calcMovementDir(GameObject owner) {
        Vector2 movementDir = Vector2.ZERO;

        if(this.objectToFollow.getCenter().x() < owner.getCenter().x()) {
            movementDir = Vector2.LEFT;
        }

        if(this.objectToFollow.getCenter().x() > owner.getCenter().x()) {
            movementDir = Vector2.RIGHT;
        }
        return movementDir;
    }
}
