package bricker.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The class for the PuckBall object. Inherits Ball.
 */
public class PuckBall extends Ball {
    private static final float PUCK_FACTOR = 0.75F;

    /**
     * Construct a new PuckBall instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound The bloop sound the ball makes upon hitting an object.
     */
    public PuckBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    Sound collisionSound) {
        super(topLeftCorner, dimensions.mult(PUCK_FACTOR), renderable, collisionSound);
    }

}
