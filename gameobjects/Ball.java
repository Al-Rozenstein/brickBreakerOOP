package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The class for the Ball object. represents both the main ball of the game,
 * and all the puckBalls by inheritance. Inherits GameObject.
 */
public class Ball extends GameObject {
    private static final int ZERO = 0;
    private static final int COLLISION_COUNTER_SETTER = -1;
    private final Sound collisionSound;
    private int collisionCounter;
    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound The bloop sound the ball makes upon hitting an object.
     *
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = ZERO;
    }

    @Override
    /**
     * Defines the ball's behaviour upon hitting an object i.e. rebound.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = this.getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        this.collisionCounter++;
        this.renderer().setRenderable(new RectangleRenderable(Color.RED));

    }

    /**
     * getter
     * @return CollisionCounter
     */
    public int getCollisionCounter() {
        return this.collisionCounter;
    }

    /**
     * Sets the collisionCounter to -1. Used in BrickerGameManager to reset the
     * counter for the camera
     */
    public void setCollisionCounter() {
        this.collisionCounter = COLLISION_COUNTER_SETTER;
    }
}
