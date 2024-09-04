package bricker.main;

import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.*;
import danogl.util.Vector2;
import bricker.movement_strategies.AIMovementStrategy;
import bricker.movement_strategies.UserMovementStrategy;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Main class of the project, manages and runs the game.
 * Responsible for managing assets, objects, loading and removing game objects,
 * starting adn ending the game.
 * Inherits from GameManager class implemented in DanoGL library.
 */
public class BrickerGameManager extends GameManager {
    private static final int MAX_HEARTS = 4;
    private static final String MAIN_OBJECT_TAG = "Main";
    private static final String MAIN_PADDLE = "mainPaddle";
    private static final String NOT_MAIN = "Not Main";
    private static final float HALF_FACTOR = 0.5F;
    private static final String BACKROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final int AI_PADDLE_Y = 30;
    private static final int USER_PADDLE_Y = 50;
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final double BRICK_WIDTH_FACTOR = 0.8;
    private static final double BRICK_INTERVAL_FACTOR = 0.1;
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final int HEART_Y = 30;
    private static final int SPECIAL_OBJECT_MAX_HITS = 4;
    private static final float CAMERA_SHIFT_FACTOR = 1.2f;
    private static final int HEART_VELOCITY = 100;
    private static final String ADDITIONAL_HEART_TAG = "Additional Heart";
    private static final int FATHER = 0;
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final int CMD_BRICKS_WIDTH = 0;
    private static final int CMD_BRICKS_HEIGHT = 1;
    private LifeCounter lifeCounter;
    private static final int LIVES = 3;
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 500;
    private static final String TITLE = "Bricker";
    private static final int WINDOW_WALL_WIDTH = 10;
    private static final String EMPTY_STRING = "";
    private static final String WINNING_MESSAGE_PROMPT = "You win!";
    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final String LOSING_MESSAGE_PROMPT = "You lose!";
    private static final float HEART_RADIUS = 27.2F;
    private static final float HEARTS_INTERVAL = 5F;
    private static final float HALF = 0.5f;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final float BALL_SPEED = 250;
    private static final int BALL_RADIUS = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private final static int BRICK_HEIGHT = 15;
    private Ball mainBall;
    private final int bricksRows;
    private final int bricksCols;
    private WindowController windowController;
    private int heartCounter;
    private Heart[] heartsArray;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Paddle additionalPaddle;
    private boolean isThereAdditionalPaddle;


    /**
     * Builds a new BrickerGameManager object, called in main().
     * @param windowTitle The game title
     * @param windowDimensions the size of the window
     * @param bricksRows number of rows of bricks
     * @param bricksCols number of bricks in a row
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksRows,
                              int bricksCols) {
        super(windowTitle, windowDimensions);
        this.bricksRows = bricksRows;
        this.bricksCols = bricksCols;
    }

    /**
     *
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.inputListener = inputListener;

        this.imageReader = imageReader;
        this.soundReader = soundReader;

        this.heartsArray = new Heart[MAX_HEARTS];

        this.heartCounter = ZERO;

        this.isThereAdditionalPaddle = false;

        // Create a ball
        createBall(MAIN_OBJECT_TAG, null);

        // Create user and AI paddles
        createUserPaddle(MAIN_PADDLE);

        // Create boundaries:
        initializeBoundaries();

        // Create backround
        generateBackround();

        // Create a brick
        generateBricksMatrix();

        // Generate hearts
        generateHearts();

        // Create the life counter
        generateLifeCounter();
    }

    /**
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkIfObjectFell();
        removeAdditionalPaddle();
        resetCamera();
        updateLifeCounter(); // updates the what's his face
        checkForGameEnd();
    }


    private void updateLifeCounter() {
        this.lifeCounter.setLifeCounterString(String.valueOf(this.heartCounter));
        setLifeCounterColour();
    }

    private void checkForGameEnd() {
        String prompt = EMPTY_STRING;
        float ballHeight = this.mainBall.getCenter().y();

        if(ballHeight > this.windowController.getWindowDimensions().y()) { // lost game
            initiateBallLocation(this.mainBall,
                    this.windowController.getWindowDimensions().mult(HALF_FACTOR));
            removeHeart();
        }

        boolean winningIndicator = true;

        for(GameObject gameObject: gameObjects()) {
            if(gameObject instanceof Brick) {
                winningIndicator = false;
                break;
            }
        }

        if((winningIndicator)|| (inputListener.isKeyPressed(KeyEvent.VK_W))) {
            prompt = WINNING_MESSAGE_PROMPT;
        }

        if(this.heartCounter == ZERO) {
            prompt = LOSING_MESSAGE_PROMPT;
        }

        if(!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_PROMPT;
            if(this.windowController.openYesNoDialog(prompt)) {
                this.windowController.resetGame();
            }
            else {
                this.windowController.closeWindow();
            }
        }
    }


    private void checkIfObjectFell() {
        for(GameObject gameObject: this.gameObjects()) {
            float objectHeight = gameObject.getCenter().y();

            if(gameObject instanceof PuckBall) {
                if(objectHeight > this.windowController.getWindowDimensions().y()) {
                    this.gameObjects().removeGameObject(gameObject);
                }
            }

            if(gameObject.getTag().equals(ADDITIONAL_HEART_TAG)) {
                Heart heart = (Heart) gameObject;
                if(heart.getCollisionIndicator()) {
                    this.gameObjects().removeGameObject(gameObject);
                    addHeartToArray(createHeart());
                }

                if(objectHeight > this.windowController.getWindowDimensions().y()) {

                    this.gameObjects().removeGameObject(gameObject);
                }
            }
        }
    }



    private void removeAdditionalPaddle() {
        if(!this.isThereAdditionalPaddle) {
            return;
        }
        if(this.additionalPaddle.getHitCounter().value() == SPECIAL_OBJECT_MAX_HITS) {
            this.gameObjects().removeGameObject(this.additionalPaddle);
            this.isThereAdditionalPaddle = false;
        }
    }
    private void generateBackround() {
        Vector2 windowDimensions = this.windowController.getWindowDimensions();
        Renderable backroundImage = this.imageReader.readImage(BACKROUND_IMAGE_PATH,
                false);

        GameObject backround = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                windowDimensions.y()), backroundImage);
        backround.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(backround, Layer.BACKGROUND);
    }

    /**
     * Called when a brick witH cameraShiftStrategy is broken, from the strategy's class.
     * Shifts the camera to follow the main ball.
     */
    public void cameraShift() {
        if(this.camera() == null) {
            this.setCamera(new Camera(this.mainBall,
                    Vector2.ZERO,
                    this.windowController.getWindowDimensions().mult(CAMERA_SHIFT_FACTOR),
                    this.windowController.getWindowDimensions()));
            this.mainBall.setCollisionCounter();
        }
    }

    private void resetCamera() {
        if(this.camera() != null) {
            if(this.mainBall.getCollisionCounter() == SPECIAL_OBJECT_MAX_HITS) {
                this.setCamera(null);
            }
        }
    }

    private void initializeBoundaries() {
//        RectangleRenderable wallColour = new RectangleRenderable(Color.CYAN);
        RectangleRenderable wallColour = null;

        Vector2 windowDimensions = this.windowController.getWindowDimensions();

        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WINDOW_WALL_WIDTH,
                windowDimensions.y()), wallColour);

        GameObject rightWall = new GameObject(Vector2.ZERO,
                new Vector2(WINDOW_WALL_WIDTH, windowDimensions.y()),
                wallColour);

        GameObject ceiling = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                WINDOW_WALL_WIDTH), wallColour);

        rightWall.setCenter(new Vector2(windowDimensions.x() - (float) WINDOW_WALL_WIDTH / TWO,
                windowDimensions.y()/TWO));

        this.gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(ceiling, Layer.STATIC_OBJECTS);
    }

    /**
     * Deletes an object from the game, called by collisionStrategyObject,
     * to delete a brick when hit.
     * @param objectToDelete the object needs to be removed from the game.
     */
    public void deleteObject(GameObject objectToDelete, int ObjectLayer) {
        gameObjects().removeGameObject(objectToDelete, ObjectLayer);
    }
    private void createBall(String tag, Vector2 coordinate) {

        Renderable ballImage;

        Sound collisionSound = this.soundReader.readSound(BALL_SOUND_PATH);
        if (tag.equals(MAIN_OBJECT_TAG)) {
            ballImage = this.imageReader.readImage(BALL_IMAGE_PATH, true);
            this.mainBall = new Ball(Vector2.ZERO,
                    new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
            mainBall.setTag(MAIN_OBJECT_TAG);

            initiateBallLocation(this.mainBall,
                    this.windowController.getWindowDimensions().mult(HALF_FACTOR));
            this.gameObjects().addGameObject(this.mainBall);
        }
        else {
            ballImage = this.imageReader.readImage(PUCK_IMAGE_PATH, true);
            PuckBall puckBall =
                    new PuckBall(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage,
                    collisionSound);
            initiateBallLocation(puckBall, coordinate);
            this.gameObjects().addGameObject(puckBall);
        }
    }

    /**
     * Creates a puckBall object, called from AdditionalBallsStrategy when a brick with
     * such strategy is broken.
     * @param coordinate - the brick's center's coordinate where the puckBall appears
     */
    public void createPuck(Vector2 coordinate) {
        createBall(NOT_MAIN, coordinate);
    }

    private void initiateBallLocation(Ball ball, Vector2 coordinate) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()) {
            ballVelX *= (-ONE);
        }

        if(rand.nextBoolean()) {
            ballVelY *= (-ONE);
        }

        ball.setCenter(coordinate);
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }
    private void createUserPaddle(String tag) {
        Renderable paddleImage =
                this.imageReader.readImage(PADDLE_IMAGE_PATH, true);

        Paddle userPaddle = new Paddle(Vector2.ZERO,
                                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                                paddleImage, new UserMovementStrategy(this.inputListener),
                                this.windowController.getWindowDimensions());

        float paddleY;
        userPaddle.setTag(tag);

        if(tag.equals(MAIN_PADDLE)) {
            paddleY = this.windowController.getWindowDimensions().y() - USER_PADDLE_Y;
        }
        else {
            paddleY = this.windowController.getWindowDimensions().y() / TWO;
            this.additionalPaddle = userPaddle;
        }

        userPaddle.setCenter(new Vector2(
                this.windowController.getWindowDimensions().x() / TWO, paddleY));
        gameObjects().addGameObject(userPaddle);
    }


    /**
     * Creates the additional paddle, called in AdditionalPaddleStrategy when a brick with
     * such strategy is broken.
     */
    public void createAdditionalPaddle() {
        if(!this.isThereAdditionalPaddle) {
            createUserPaddle(NOT_MAIN);
            this.isThereAdditionalPaddle = true;
        }
    }
    private void createAIPaddle() {
        Renderable paddleImage =
                this.imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject aiPaddle = new Paddle(Vector2.ZERO,
                              new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                              new AIMovementStrategy(mainBall),
                this.windowController.getWindowDimensions());

        aiPaddle.setCenter(
            new Vector2(this.windowController.getWindowDimensions().x() / TWO, AI_PADDLE_Y));
        gameObjects().addGameObject(aiPaddle);
    }
    private void generateBrick(Renderable brickImage, float brickWidth, Vector2 brickCoordinate,
                               CollisionStrategy collisionStrategy) {
        GameObject brick = new Brick(Vector2.ZERO,
                           new Vector2(brickWidth, BRICK_HEIGHT), brickImage,
                        collisionStrategy);

        brick.setTopLeftCorner(brickCoordinate);
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }
    private void generateBricksMatrix() {
        Renderable brickImage =
                this.imageReader.readImage(BRICK_IMAGE_PATH, false);
        CollisionStrategyFactory collisionStrategyFactory =
                new CollisionStrategyFactory(this, FATHER);

        float initialCoordinate = WINDOW_WALL_WIDTH;
        // calculate available screen area for bricks
        float availableAreaXAxis =
                this.windowController.getWindowDimensions().x() - (TWO * WINDOW_WALL_WIDTH);

        float unit = availableAreaXAxis / this.bricksCols;
        double brickWidth = BRICK_WIDTH_FACTOR * unit;
        double interval = BRICK_INTERVAL_FACTOR * unit;

        for(int row = ZERO; row < this.bricksRows; row++) {
            for(int col = ZERO; col < this.bricksCols; col++) {
                Vector2 brickCoordinate =
                new Vector2((float)
                        (initialCoordinate + interval + col * (brickWidth + TWO * interval)),
                        (float)
            (initialCoordinate + interval + row * (BRICK_HEIGHT + TWO * HALF_FACTOR * interval)));

                generateBrick(brickImage,
                        (float) brickWidth, brickCoordinate, collisionStrategyFactory.buildStrategy());
            }
        }
    }

    private void generateHearts() {
        for(int i = ZERO; i < LIVES; i++) {
            addHeartToArray(createHeart());
        }
    }

    private Heart createHeart() {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH,
                true);
        return new Heart(Vector2.ZERO, new Vector2(HEART_RADIUS, HEART_RADIUS), heartImage);
    }

    private void addHeartToArray(Heart heart) {
        if(this.heartCounter >= MAX_HEARTS) {
            return;
        }
        Vector2 coordinate = calculateHeartCoordinates();
        heart.setTopLeftCorner(coordinate);
        this.gameObjects().addGameObject(heart, Layer.UI);
        this.heartsArray[this.heartCounter] = heart;
        this.heartCounter++;
        if (this.lifeCounter != null) {
            this.lifeCounter.setLifeCounterString(String.valueOf(this.heartCounter));
        }
    }


    /**
     * Creates an additional heart. called in AdditionalHeartStrategy when such brick is broken.
     * @param coordinate The broken brick's coordinate, where the heart will fall from.
     */
    public void createAdditionalHeart(Vector2 coordinate) {
        Heart heart = createHeart();
        heart.setTag(ADDITIONAL_HEART_TAG);
        heart.setCenter(coordinate);
        heart.setVelocity(new Vector2(ZERO, HEART_VELOCITY));
        this.gameObjects().addGameObject(heart);
    }

    private Vector2 calculateHeartCoordinates() {
        return new Vector2((WINDOW_WALL_WIDTH + HEARTS_INTERVAL +
                        this.heartCounter * (HEART_RADIUS + TWO * HEARTS_INTERVAL) + HEART_RADIUS),
            (this.windowController.getWindowDimensions().y() - WINDOW_WALL_WIDTH - HEART_RADIUS));
    }

    /**
     * removes a heart object from the UI and updates the life counter
     */
    private void removeHeart() {
        this.heartCounter--;
        this.gameObjects().removeGameObject(this.heartsArray[this.heartCounter], Layer.UI);
        this.lifeCounter.setLifeCounterString(String.valueOf(this.heartCounter));
        setLifeCounterColour();
    }
    private void generateLifeCounter() {
        TextRenderable textRenderable = new TextRenderable(EMPTY_STRING);
        this.lifeCounter = new LifeCounter(Vector2.ZERO,
                                        new Vector2(HEART_RADIUS, HEART_RADIUS), textRenderable);

        this.lifeCounter.setCenter(new Vector2(TWO * WINDOW_WALL_WIDTH,
                this.windowController.getWindowDimensions().y() - HEART_Y));
        this.lifeCounter.setLifeCounterString(String.valueOf(this.heartCounter));
        setLifeCounterColour();
        this.gameObjects().addGameObject(this.lifeCounter, Layer.UI);
    }
    private void setLifeCounterColour() {
        if (this.heartCounter >= THREE) {
            this.lifeCounter.setColor(Color.green);
        }
        if (this.heartCounter == TWO) {
            this.lifeCounter.setColor(Color.yellow);
        }

        if (this.heartCounter == ONE) {
            this.lifeCounter.setColor(Color.red);
        }
    }

    /**
     * main function, if user chooses he or she can choose how many bricks to start the game with.
     * creates a BrickerGameManager object to run the games untill the player decides she or he
     * wants to quit.
     * @param args - the command line arguments, used to for input for how many bricks to start
     *             the game with.
     */
    public static void main(String[] args) {
        int bricksHeight = DEFAULT_BRICK_ROWS;
        int bricksWidth = DEFAULT_BRICK_COLS;

        if(args.length == TWO) {
            bricksWidth = Integer.parseInt(args[CMD_BRICKS_WIDTH]);
            bricksHeight = Integer.parseInt(args[CMD_BRICKS_HEIGHT]);
        }

        Vector2 windowDims = new Vector2(1920, 1000);
        BrickerGameManager brickerGameManager = new BrickerGameManager(TITLE,
                windowDims, bricksHeight, bricksWidth);

        brickerGameManager.run();
    }
}
