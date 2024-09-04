package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * Strategy for the camera shift.
 * wraps the basic game manager. Inherits BasicCollisionStrategy.
 * Has a BrickerGameManager as a member, and 2 other CollisionStrategies.
 */
public class DoubleBehaviourStrategy extends BasicCollisionStrategy {
    private static final int FATHER = 1;
    private static final int SON = 2;
    private static final int THE_HOLY_SPIRIT = 3;
    private int level;
    private CollisionStrategyFactory collisionStrategyFactory;
    private CollisionStrategy strategy1;
    private CollisionStrategy strategy2;

    /**
     * Builds a new DoubleBehaviourStrategy object
     * @param brickerGameManager - the game manger running the current game
     * @param level - the depth of the 'recursive' hierarchy of the current brick.
     *              (A son/grandson of a brick with DoubleBehaviourStrategy).
     *              See README for full documentation.
     */
    public DoubleBehaviourStrategy(BrickerGameManager brickerGameManager, int level) {
        super(brickerGameManager);
        this.collisionStrategyFactory = new CollisionStrategyFactory(brickerGameManager,
                level + FATHER);
        this.level = level + FATHER;


        this.strategy1 = this.collisionStrategyFactory.buildStrategy();
        this.strategy2 = this.collisionStrategyFactory.buildStrategy();

        if(this.level == THE_HOLY_SPIRIT) {
            while(this.strategy1 instanceof DoubleBehaviourStrategy) {
                this.strategy1 = this.collisionStrategyFactory.buildStrategy();
            }
            while(this.strategy2 instanceof DoubleBehaviourStrategy) {
                this.strategy2 = this.collisionStrategyFactory.buildStrategy();
            }
        }
        if(this.level == SON) {
            if(this.strategy1 instanceof DoubleBehaviourStrategy){
                while(this.strategy2 instanceof DoubleBehaviourStrategy) {
                    this.strategy2 = this.collisionStrategyFactory.buildStrategy();
                }
            }
        }
    }

    @Override
    /**
     * Overrides BasicCollisionStrategy's onCollision, using its
     * BrickerGameManager object to activate the double strategies' onCollision..
     * @param firstObject the brick that was hit
     * @param secondObject the hitting object
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        strategy1.onCollision(thisObj, otherObj);
        strategy2.onCollision(thisObj, otherObj);
        super.onCollision(thisObj, otherObj);
    }
}
