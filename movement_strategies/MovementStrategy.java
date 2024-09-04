package bricker.movement_strategies;

import danogl.GameObject;
import danogl.util.Vector2;


/**
 * An interface for defining the movement of a Paddle.
 * @author Alon
 */
public interface MovementStrategy {
    /**
     * The main method of the strategy, used by the Paddle to determine direction.
     * @param owner The paddle which owns this strategy.
     * @return A movement direction of type Vector2 whose y coordinate is 0,
     * and x coordinate is -1, 1 or 0.
     */
    Vector2 calcMovementDir(GameObject owner);
}
