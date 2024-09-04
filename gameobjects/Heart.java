package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The class for the Heart object. Inherits GameObject.
 */
public class Heart extends GameObject {
    private static final String MAIN_PADDLE = "mainPaddle";
    private boolean collisionIndicator;

    /**
     * Construct a new Heart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionIndicator = false;
    }

    @Override
    /**
     * Overrides shouldCollideWith to make sure the heart collides only with the additional paddle.
     */
    public boolean shouldCollideWith(GameObject other) {
        if(other.getTag().equals(MAIN_PADDLE)) {
            return super.shouldCollideWith(other);
        }
        return false;
    }

    @Override
    /**
     * Overrides onCollisionEnter for the same purpose as above, and to set the collisionIndicator
     * to True.
     */
    public void onCollisionEnter(GameObject other, Collision collision) {
        if(other.getTag().equals(MAIN_PADDLE)) { // just to be sure!
            this.collisionIndicator = true;
            super.onCollisionEnter(other, collision);
        }
    }

    /**
     * A getter. Used in BrickerGameObject to check if the heart was collected by the paddle.
     * @return collisionIndicator.
     */
    public boolean getCollisionIndicator() {
        return this.collisionIndicator;
    }
}
