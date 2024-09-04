package bricker.brick_strategies;


import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * The factory class for the CollisionStrategies.
 * Used to generate the CollisionStrategy for the bricks.
 */
public class CollisionStrategyFactory {
    private static final int ADDITIONAL_BALLS = 0;
    private static final int ADDITIONAL_PADDLE = 1;
    private static final int CAMERA_SHIFT = 2;
    private static final int ADDITIONAL_HEART = 3;
    private static final int DOUBLE_BEHAVIOUR = 4;
    private static final int STRATEGY_FACTORY_CASES = 5;
    private BrickerGameManager brickerGameManager;
    private Random randObj;
    private int level;


    /**
     * Factory for building brick_strategies. Built in BrickerGameManager's generateBricksMatrix
     * to send a collisionStrategy to each brick.
     * @param brickerGameManager the current BrickerGameManager that runs the game.
     * @param level used to make sure a single brick doesn't have more than 3 instances of
     *              DoubleBehaviourStrategy (see bellow). See README for full documentation of the
     *              design.
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager, int level) {
    this.brickerGameManager = brickerGameManager;
    this.randObj = new Random();
    this.level = level;
    }


    /**
     * builds a new CollisionStrategy in ratio of 5 : 1 : 1 : 1 : 1 : 1, between
     * BasicCollisionStrategy, and the rest.
     */
    public CollisionStrategy buildStrategy() {

        if(randObj.nextBoolean()) {
            return new BasicCollisionStrategy(this.brickerGameManager);
        }
        int diceRoll = randObj.nextInt(STRATEGY_FACTORY_CASES);
        switch (diceRoll) {
            case ADDITIONAL_BALLS:
                return new AdditionalBallsStrategy(this.brickerGameManager);

            case ADDITIONAL_PADDLE:
                return new AdditionalPaddleStrategy(this.brickerGameManager);

            case CAMERA_SHIFT:
                return new CameraShiftStrategy(this.brickerGameManager);

            case ADDITIONAL_HEART:
                return new AdditionalHeartStrategy(this.brickerGameManager);

            case DOUBLE_BEHAVIOUR:
                return new DoubleBehaviourStrategy(this.brickerGameManager, this.level);
        }
        return null;
    }
}
