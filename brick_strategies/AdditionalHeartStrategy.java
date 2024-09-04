package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * Strategy for an extra 2 heart.
 * wraps the basic game manager. Inherits BasicCollisionStrategy.
 * Has a BrickerGameManager as a member.
 */
public class AdditionalHeartStrategy extends BasicCollisionStrategy {

    private BrickerGameManager brickerGameManager;

    /**
     * Builds a new AdditionalHeartStrategy object
     * @param brickerGameManager - the game manger running the current game
     */
    public AdditionalHeartStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    /**
     * Overrides BasicCollisionStrategy's onCollision, calling brickerGameManager's
     * createAdditionalHeart.
     * @param firstObject the brick that was hit
     * @param secondObject the hitting object
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        this.brickerGameManager.createAdditionalHeart(thisObj.getCenter());
        super.onCollision(thisObj, otherObj);
    }
}
