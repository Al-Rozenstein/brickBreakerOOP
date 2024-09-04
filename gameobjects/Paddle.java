package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.movement_strategies.MovementStrategy;


/**
 * The class for the Paddle object. Inherits GameObject.
 */
public class Paddle extends GameObject {
    private static final int ZERO = 0;
    private static final float MOVEMENT_SPEED = 300;
    private static final String NOT_MAIN = "Not Main";
    private Counter hitCounter;
    private MovementStrategy movementStrategy;
    private final Vector2 windowDimensions;


    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  MovementStrategy movementStrategy, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.movementStrategy = movementStrategy;
        this.windowDimensions = windowDimensions;
        this.hitCounter = new Counter(ZERO);
    }

    private void checkPaddleOutOfBounds(Vector2 windowDims) {

        if(this.getTopLeftCorner().x() < ZERO) {
            this.setTopLeftCorner(new Vector2(ZERO, this.getTopLeftCorner().y()));
        }
        if(this.getTopLeftCorner().x() + this.getDimensions().x() > windowDims.x()) {
            this.setTopLeftCorner(new Vector2(windowDims.x() -
                    this.getDimensions().x(), this.getTopLeftCorner().y()));
        }
    }

    @Override
    /**
     * Overrides the update method. used to define the paddle's movement according to the
     * Movement strategy.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = movementStrategy.calcMovementDir(this);
        checkPaddleOutOfBounds(this.windowDimensions);
        this.setVelocity(movementDir.mult(Paddle.MOVEMENT_SPEED));
    }

    @Override
    /**
     * Overides onCollisionEnter method, to track how many times the AdditionalPaddle was hit.
     */
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if((this.getTag().equals(NOT_MAIN)) && (other instanceof Ball)) {
            this.hitCounter.increment();
        }
    }

    /**
     * getter - to notify BrickerGameManager when to delete the AdditionalPaddle.
     * @return hitCounter
     */
    public Counter getHitCounter() {
        return this.hitCounter;
    }
}
