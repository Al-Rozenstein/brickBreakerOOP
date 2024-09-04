package bricker.brick_strategies;

import danogl.GameObject;
import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;

/**
 * Strategy for the cameraShift mechanic..
 * wraps the basic game manager. Inherits BasicCollisionStrategy.
 * Has a BrickerGameManager as a member.
 */
public class CameraShiftStrategy extends BasicCollisionStrategy {

    private static final String MAIN_OBJECT_TAG = "Main";
    private BrickerGameManager brickerGameManager;

    /**
     * Builds a new CameraShiftStrategy object
     * @param brickerGameManager - the game manger running the current game
     */
    public CameraShiftStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
    }


    @Override
    /**
     * Overrides BasicCollisionStrategy's onCollision, using its
     * BrickerGameManager object to call his public method cameraShift,
     * In case the brick was hit by the main ball.
     * @param firstObject the brick that was hit
     * @param secondObject the hitting object
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if((otherObj instanceof Ball) && (otherObj.getTag().equals(MAIN_OBJECT_TAG))) {
            this.brickerGameManager.cameraShift();
        }
    }
}
