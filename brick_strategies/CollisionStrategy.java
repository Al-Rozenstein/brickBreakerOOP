package bricker.brick_strategies;

import danogl.GameObject;

/**
 * CollisionStrategy interface, for the bricks' strategies.
 */
public interface CollisionStrategy {
    /**
     * The method that defines the brick's collision strategy, polymorphisms according to the
     * type of startegy sent to the brick.
     * @param firstObject The brick
     * @param secondObject The ball hits the brick
     */
    void onCollision(GameObject firstObject, GameObject secondObject);
}
