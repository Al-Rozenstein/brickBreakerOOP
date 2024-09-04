package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * Strategy for an extra paddle.
 * wraps the basic game manager. Inherits BasicCollisionStrategy.
 * Has a BrickerGameManager as a member.
 */
public class AdditionalPaddleStrategy extends BasicCollisionStrategy {

    private BrickerGameManager brickerGameManager;

    /**
     * Builds a new AdditionalPaddleStrategy object
     * @param brickerGameManager - the game manger running the current game
     */
    public AdditionalPaddleStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    /**
     * Overrides BasicCollisionStrategy's onCollision, using its
     * BrickerGameManager object to call his public createAdditionalPaddle.
     * @param firstObject the brick that was hit
     * @param secondObject the hitting object
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        this.brickerGameManager.createAdditionalPaddle();
    }
}
