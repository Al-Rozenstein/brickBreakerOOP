package bricker.brick_strategies;

import danogl.collisions.Layer;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * The strategy for the basic behaviour, hitting the bricks makes them disappear.
 * implements the CollisionStrategy interface.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private BrickerGameManager brickerGameManager;

    /**
     * Builds a new BasicCollisionStrategy, holds an BrickerGameManager object.
     * We chose to design our software in such way where the BasicCollisionStrategy
     * holds the BrickerGameManager object, so it can notify it when it should delete a brick,
     * to keep the responsibility for deleting bricks and all gameObjects only in the hands
     * of BrickerGameManager.
     * @param brickerGameManager - the BrickerGameManager currently running the game.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    /**
     * Overrides CollisionStrategy's on collision. calls it's BrickerGameManager's
     * deleteObject public method when a brick is hit.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        this.brickerGameManager.deleteObject(thisObj, Layer.STATIC_OBJECTS);
    }
}
