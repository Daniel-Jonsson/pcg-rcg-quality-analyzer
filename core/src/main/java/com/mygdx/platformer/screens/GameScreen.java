package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.platformer.characters.enemies.EnemyManager;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.player.HealthBar;
import com.mygdx.platformer.characters.player.Player;
import com.mygdx.platformer.difficulty.GameDifficultyManager;
import com.mygdx.platformer.difficulty.observer.GameDifficultyObserver;
import com.mygdx.platformer.pcg.manager.PlatformManager;
import com.mygdx.platformer.physics.GameContactListener;
import com.mygdx.platformer.screens.overlays.GameOverOverlay;
import com.mygdx.platformer.sound.AudioManager;
import com.mygdx.platformer.ui.GameTimer;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;
import com.mygdx.platformer.utilities.Settings;

/**
 * The main gameplay screen of the platformer game.
 * <p>
 * This class manages the core gameplay experience, including:
 * <ul>
 * <li>Physics simulation using Box2D</li>
 * <li>Procedural platform generation</li>
 * <li>Player movement and controls</li>
 * <li>Enemy spawning and AI behavior</li>
 * <li>Attack mechanics and collision detection</li>
 * <li>Camera movement and parallax scrolling background</li>
 * <li>Game difficulty progression</li>
 * <li>Game over conditions and UI</li>
 * </ul>
 * </p>
 * <p>
 * The screen implements the GameDifficultyObserver interface to respond to
 * changes in game difficulty, adjusting gameplay elements accordingly to
 * increase challenge as the player progresses.
 * </p>
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class GameScreen extends ScreenAdapter implements GameDifficultyObserver {
    /**
     * Reference to the main game instance to allow screen switching.
     * <p>
     * This reference is used to transition between different screens,
     * such as returning to the main menu when the game ends.
     * </p>
     */
    private final PlatformerGame game;

    /**
     * The Box2D physics world for managing physics interactions.
     * <p>
     * This world handles all physics simulations including gravity,
     * collisions between entities, and movement of dynamic bodies.
     * </p>
     */
    private World world;

    /**
     * SpriteBatch for rendering game elements.
     * <p>
     * Used to efficiently batch and render all game sprites including
     * the player, enemies, platforms, and background.
     * </p>
     */
    private SpriteBatch batch;

    /**
     * Camera for viewing the game world.
     * <p>
     * An orthographic camera that follows the player horizontally
     * as they progress through the level. The camera's position
     * determines what portion of the game world is visible.
     * </p>
     */
    private OrthographicCamera camera;

    /**
     * Viewport that manages the game's display area.
     * <p>
     * A FitViewport that maintains the game's aspect ratio regardless
     * of the window size, ensuring consistent gameplay across different
     * screen dimensions.
     * </p>
     */
    private FitViewport viewport;

    /**
     * The player character controlled by the user.
     * <p>
     * Contains the player's physics body, health, movement logic,
     * and attack capabilities.
     * </p>
     */
    private Player player;

    /**
     * Overlay displayed when the player dies.
     * <p>
     * Shows the game over screen with the player's survival time
     * and options to return to the main menu or quit the game.
     * </p>
     */
    private GameOverOverlay gameOverOverlay;

    /**
     * Tracks accumulated time for physics updates.
     * <p>
     * Used to ensure physics simulations run at a fixed timestep
     * regardless of the actual frame rate.
     * </p>
     */
    private float runTime;

    /**
     * Manages procedural platform generation.
     * <p>
     * Responsible for creating, updating, and removing platforms
     * as the player progresses through the level. Adjusts platform
     * parameters based on difficulty level.
     * </p>
     */
    private PlatformManager platformManager;

    /**
     * Flag indicating whether the game has ended.
     * <p>
     * When true, gameplay stops and the game over overlay is displayed.
     * Set to true when the player dies by falling off platforms or
     * losing all health.
     * </p>
     */
    private boolean isGameOver = false;

    /**
     * Tracks and displays the player's survival time.
     * <p>
     * Updates continuously during gameplay and is displayed
     * on the game over screen when the player dies.
     * </p>
     */
    GameTimer gameTimer;

    /**
     * Manages enemy spawning, behavior, and lifecycle.
     * <p>
     * Handles creating enemies, updating their AI behavior,
     * and removing them when they are defeated or off-screen.
     * </p>
     */
    EnemyManager enemyManager;

    /**
     * Manages attack creation, movement, and collision.
     * <p>
     * Handles attacks from both the player and enemies,
     * including their physics bodies, movement, and damage effects.
     * </p>
     */
    AttackManager attackManager;

    /**
     * Flag indicating whether AI-controlled play is enabled.
     * <p>
     * When true, the player character is controlled by an AI agent
     * rather than user input, useful for testing or demonstration.
     * </p>
     */
    Boolean autoPlayEnabled;

    /**
     * Visual representation of the player's health.
     * <p>
     * Displays the player's current health as a bar on the screen,
     * updating in real-time as the player takes damage.
     * </p>
     */
    private HealthBar healthBar;

    /**
     * Scaling factor for UI elements.
     * <p>
     * Determined by user settings, this value affects the size
     * of UI elements like the health bar and timer.
     * </p>
     */
    private final float UIScale;

    /**
     * The camera's current X position in the world.
     * <p>
     * Increases continuously as the game progresses, creating
     * the effect of the camera moving forward through the level.
     * </p>
     */
    private float cameraXPosition;

    /**
     * Multiplier for movement speed based on difficulty.
     * <p>
     * Increases as the game's difficulty level rises, making
     * the camera move faster and requiring quicker reactions
     * from the player.
     * </p>
     */
    private float difficultySpeed = 1.0f;

    /**
     * First background texture for parallax scrolling.
     * <p>
     * Part of the pair of textures used to create an infinite
     * scrolling background effect.
     * </p>
     */
    private Texture background1;

    /**
     * Second background texture for parallax scrolling.
     * <p>
     * Positioned adjacent to background1 and loops seamlessly
     * to create a continuous background.
     * </p>
     */
    private Texture background2;

    /**
     * X-coordinate offset for background scrolling.
     * <p>
     * Updated continuously to create the parallax scrolling effect,
     * making the background move at a different rate than the camera.
     * </p>
     */
    private float backgroundX;

    /**
     * Width of the background textures in world units.
     * <p>
     * Matches the viewport width to ensure the background
     * covers the entire visible area.
     * </p>
     */
    private float backgroundWidth;

    /**
     * Height of the background textures in world units.
     * <p>
     * Matches the viewport height to ensure the background
     * covers the entire visible area.
     * </p>
     */
    private float backgroundHeight;

    /**
     * Constructor for the GameScreen class, which initializes a reference to the
     * game instance.
     *
     * @param g        main Game instance.
     * @param autoPlay indicates whether autoplay is enabled.
     */
    public GameScreen(final PlatformerGame g, boolean autoPlay) {
        this.game = g; // reference main class to enable switching to another screen
        this.UIScale = Settings.getUIScale();
        this.gameTimer = new GameTimer(UIScale);
        autoPlayEnabled = autoPlay;
        GameDifficultyManager.getInstance().registerObserver(this);

    }

    /**
     * Executes when this screen is set as the active screen.
     */
    @Override
    public void show() {
        world = new World(new Vector2(0, AppConfig.GRAVITY), true); // init world and set y gravity to -10

        Vector2 spawnPosition = new Vector2(AppConfig.PLAYER_SPAWN_X, AppConfig.PLAYER_SPAWN_Y);
        this.attackManager = new AttackManager(world);
        this.enemyManager = new EnemyManager(world, attackManager, spawnPosition);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false); // invert coordinates (y = 0 at bottom of window)
        batch.setProjectionMatrix(camera.combined);

        viewport = new FitViewport(AppConfig.SCREEN_WIDTH, AppConfig.SCREEN_HEIGHT, camera);
        viewport.apply(); // apply viewport settings

        cameraXPosition = viewport.getWorldWidth() / 2f;
        camera.position.set(cameraXPosition, viewport.getWorldHeight() / 2f, 0);
        camera.update();

        platformManager = new PlatformManager(world, enemyManager);

        player = new Player(world, spawnPosition, AppConfig.PLAYER_HP,
                AppConfig.PLAYER_MOVE_SPEED,
                attackManager, autoPlayEnabled, camera);

        healthBar = new HealthBar(player, camera, viewport, UIScale);

        gameOverOverlay = new GameOverOverlay(game, gameTimer.getElapsedTime());

        initCollisionListener();
        initBackgroundImage();

    }

    private void initBackgroundImage() {
        background1 = Assets.getTexture(Assets.BACKGROUND_IMAGE_TEXTURE);
        background2 = Assets.getTexture(Assets.BACKGROUND_IMAGE_TEXTURE);
        backgroundWidth = viewport.getWorldWidth();
        backgroundHeight = viewport.getWorldHeight();
        backgroundX = 0;
    }

    /**
     * This method essentially serves as the game loop, rendering objects,
     * performing physics updates, and logic updates.
     *
     * @param deltaTime The time since the last render.
     */
    @Override
    public void render(final float deltaTime) {
        if (!isGameOver) {
            checkGameOver();
            // if(!autoPlayEnabled) {
            player.handleInput();
            // }
            gameTimer.update(deltaTime);
            cameraXPosition += 2f * deltaTime * difficultySpeed;
            camera.position.set(cameraXPosition, viewport.getWorldHeight() / 2f, 0);
            camera.update();
            input();
            logic(deltaTime);
            platformManager.update(camera.position.x, AppConfig.SCREEN_WIDTH);
            GameDifficultyManager.getInstance().update(deltaTime);
            doPhysicsStep(deltaTime);

            updateBackgroundPosition(deltaTime);

            enemyManager.setTargetPosition(player.getBody().getPosition());
        }

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        float leftEdge = camera.position.x - viewport.getWorldWidth() / 2;
        float firstBgX = leftEdge + backgroundX;

        batch.draw(background1, firstBgX, 0, backgroundWidth, backgroundHeight);
        batch.draw(background2, firstBgX + backgroundWidth, 0, backgroundWidth, backgroundHeight);

        platformManager.render(batch);
        player.render(batch);
        enemyManager.render(batch);
        attackManager.render(batch);
        healthBar.render(batch);
        batch.end();

        gameTimer.render();

        if (isGameOver) {
            gameOverOverlay.render();
        }
    }

    /**
     * Updates the background position to create an infinite scrolling effect.
     * The background scrolls at a different rate than the camera to create a
     * parallax effect.
     * 
     * @param deltaTime Time since last frame.
     */
    private void updateBackgroundPosition(float deltaTime) {
        float backgroundVelocity = AppConfig.BACKGROUND_IMAGE_SCROLL_SPEED;
        backgroundX -= backgroundVelocity * deltaTime * difficultySpeed;
        backgroundX = backgroundX % backgroundWidth;
    }

    /**
     * Performs physics simulations in fixed time steps. These updates are decoupled
     * from the render() update frequency, to allow more freedom for physics
     * updates, not
     * having to adhere to the monitor update frequency.
     *
     * @param deltaTime The time since the last render.
     */
    private void doPhysicsStep(final float deltaTime) {

        // max frame time
        float frameTime = Math.min(deltaTime * AppConfig.TIME_SCALE, AppConfig.MAX_FRAME_TIME);
        runTime += frameTime;
        while (runTime >= AppConfig.TIME_STEP) {
            player.update(AppConfig.TIME_STEP);
            enemyManager.update(AppConfig.TIME_STEP);
            attackManager.update(camera.position.x,
                    AppConfig.SCREEN_WIDTH);
            world.step(AppConfig.TIME_STEP, AppConfig.VELOCITY_ITERATIONS, AppConfig.POSITION_ITERATIONS);
            runTime -= AppConfig.TIME_STEP;
        }
    }

    /**
     * Checks whether the players body position is less-than-or-equal-to 0 (i
     * .e., Player has fallen of a platform).
     */
    private void checkGameOver() {
        if ((player.getBody().getPosition().y + AppConfig.PLAYER_HEIGHT) <= 0 || player.getCurrentHealth() <= 0) {
            isGameOver = true;
            gameOverOverlay = new GameOverOverlay(game, gameTimer.getElapsedTime());
            gameOverOverlay.show();
            GameDifficultyManager.getInstance().resetDifficulty();
        }
    }

    /**
     * Handles input. (currently empty, will be expanded later).
     */
    private void input() {

    }

    /**
     * Handles logix. (currently empty, will be expanded later).
     *
     * @param deltaTime Time passed since last update.
     */
    private void logic(final float deltaTime) {

    }

    /**
     * Handles resizing the screen, updating the viewport with the new size.
     *
     * @param width  The new screen width.
     * @param height The new screen height.
     */
    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);

        camera.position.set(
                cameraXPosition,
                viewport.getWorldHeight() / 2f,
                0);
        camera.update();
        gameOverOverlay.resize(width, height);
    }

    /**
     * Executes when the game loses focus, e.g. when minimized
     */
    @Override
    public void pause() {

        Gdx.app.log(this.getClass().getSimpleName(), "Paused");
    }

    /**
     * executed when game regains focus, i.e. when it is brought to the foreground.
     */
    @Override
    public void resume() {
        Gdx.app.log(this.getClass().getSimpleName(), "Resumed");
    }

    /**
     * This method is called when another screen replaces this one.
     */
    @Override
    public void hide() {
        Gdx.app.log(this.getClass().getSimpleName(), "Hide");
    }

    /**
     * Releases resources when the screen is no longer needed.
     */
    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        player.dispose();
        platformManager.dispose();
        gameOverOverlay.dispose();
        healthBar.dispose();
        AudioManager.dispose();
        // Don't dispose of background textures here as they're managed by the
        // AssetManager
        // background1.dispose();
        // background2.dispose();
    }

    /**
     * Initializes collision detection logic.
     */
    private void initCollisionListener() {
        world.setContactListener(new GameContactListener(player));
    }

    @Override
    public void onDifficultyChanged(int difficultyLevel) {
        enemyManager.increaseDifficulty(difficultyLevel);
        platformManager.increaseDifficulty(difficultyLevel);
        attackManager.increaseDifficulty(difficultyLevel);
        float newSpeed = difficultySpeed +
                (difficultyLevel * AppConfig.DIFFICULTY_INCREASE_AMOUNT);
        float cameraMaxSpeed = 2.0f;
        difficultySpeed = Math.min(newSpeed, cameraMaxSpeed);
    }
}
