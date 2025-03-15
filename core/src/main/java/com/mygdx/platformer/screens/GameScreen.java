package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.platformer.EnemyManager;
import com.mygdx.platformer.GameTimer;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.attacks.BaseAttack;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.characters.player.HealthBar;
import com.mygdx.platformer.characters.player.Player;
import com.mygdx.platformer.difficulty.GameDifficultyManager;
import com.mygdx.platformer.pcg.manager.PlatformManager;
import com.mygdx.platformer.screens.overlays.GameOverOverlay;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * This class represents the main game screen.
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class GameScreen extends ScreenAdapter {
    /** Reference to the main game instance to allow screen switching. */
    private final PlatformerGame game;

    /** The Box2D physics world for managing physics interactions. */
    private World world;

    /** SpriteBatch for rendering game elements. */
    private SpriteBatch batch;

    /** Camera for viewing the game world. */
    private OrthographicCamera camera;

    /** Viewport. */
    private FitViewport viewport;

    /** The player character. */
    private Player player;

    private GameOverOverlay gameOverOverlay;

    /** Tracks run time, for physics updates. */
    private float runTime; // tracks how long the game has run

    /** Manages procedural platform generation. */
    private PlatformManager platformManager;

    private boolean isGameOver = false;

    GameTimer gameTimer;

    EnemyManager enemyManager;
    AttackManager attackManager;

    Boolean autoPlayEnabled = false;

    private HealthBar healthBar;

    private float UIScale;

    private float cameraXPosition;

    /**
     * Constructor for the GameScreen class, which initializes a reference to the
     * game instance.
     * @param g main Game instance.
     */
   public GameScreen(final PlatformerGame g, boolean autoPlay, float UIScale) {
       this.game = g; // reference main class to enable switching to another screen
       this.gameTimer = new GameTimer(UIScale);
       this.UIScale = UIScale;
       autoPlayEnabled = autoPlay;


        //Gdx.app.log(this.getClass().getSimpleName(), "Loaded");
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
        viewport.apply();  // apply viewport settings

        cameraXPosition = viewport.getWorldWidth() / 2f;
        camera.position.set(cameraXPosition, viewport.getWorldHeight() / 2f, 0);
        camera.update();

        platformManager = new PlatformManager(world, enemyManager);


        player = new Player(world, spawnPosition,AppConfig.PLAYER_HP,
            AppConfig.PLAYER_MOVE_SPEED,
            attackManager, autoPlayEnabled, camera);

        healthBar = new HealthBar(player, camera, viewport, UIScale);

        gameOverOverlay = new GameOverOverlay(game, gameTimer.getElapsedTime());

        initCollisionListener();

    }

    /**
     * This method essentially serves as the game loop, rendering objects, performing physics
     * updates, and logic updates.
     * @param deltaTime The time since the last render.
     */
    @Override
    public void render(final float deltaTime) {
        if (!isGameOver) {
            checkGameOver();
            //if(!autoPlayEnabled) {
                player.handleInput();
            //}
            gameTimer.update(deltaTime);
            cameraXPosition += 2f * deltaTime;
            camera.position.set(cameraXPosition, viewport.getWorldHeight() / 2f, 0);
            camera.update();
            input();
            logic(deltaTime);
            platformManager.update(camera.position.x, AppConfig.SCREEN_WIDTH);
            GameDifficultyManager.getInstance().update(deltaTime);
            doPhysicsStep(deltaTime);

            enemyManager.setTargetPosition(player.getBody().getPosition());
        }

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
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
     * Performs physics simulations in fixed time steps. These updates are decoupled
     * from the render() update frequency, to allow more freedom for physics updates, not
     * having to adhere to the monitor update frequency.
     * @param deltaTime The time since the last render.
     */
    private void doPhysicsStep(final float deltaTime) {
//        if(player.jumpTriggered()){
//            long physicsResponseTime = TimeUtils.nanoTime();
//            long latencyNanos = physicsResponseTime - player.inputTimestamp;
//            float latencyMillis = latencyNanos / 1_000_000f;
//
//            Gdx.app.log("InputTest", "Latency from input to physics update: " + latencyMillis + " ms");
//
//            player.setJumpTriggered(false);
//        }
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
     * @param deltaTime Time passed since last update.
     */
    private void logic(final float deltaTime) {

    }

    /**
     * Handles resizing the screen, updating the viewport with the new size.
     * @param width The new screen width.
     * @param height The new screen height.
     */
    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);

        camera.position.set(
            cameraXPosition,
            viewport.getWorldHeight() / 2f,
            0
        );
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
    }

    /**
     * Initializes collision detection logic.
     */
    private void initCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();
                Object aUserData = a.getBody().getUserData();
                Object bUserData = b.getBody().getUserData();

                Body playerBody = player.getBody();

                if (a.getBody() == playerBody || b.getBody() == playerBody) {
                    if (a.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM
                        || b.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM
                        || a.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY
                        || b.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY) {
                        player.setGrounded(true);
                    }
                }

                // Attack -> Enemy collision
                if ((a.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                    && b.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY)
                    || (b.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                    && a.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY)) {

                    if (aUserData instanceof BaseAttack || bUserData instanceof BaseAttack) {
                        BaseAttack attack =
                            (aUserData instanceof BaseAttack) ?
                                (BaseAttack) aUserData : (BaseAttack) bUserData;

                        attack.setShouldRemove(true);
                        BaseEnemy enemy = (aUserData instanceof BaseEnemy) ?
                            (BaseEnemy) aUserData : (BaseEnemy) bUserData;

                        enemy.takeDamage(attack.getDamage());
                    }
                }

                if ((a.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                    && b.getFilterData().categoryBits == AppConfig.CATEGORY_PLAYER)
                    || (b.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                    && a.getFilterData().categoryBits == AppConfig.CATEGORY_PLAYER)) {
                        BaseAttack attack = (aUserData instanceof BaseAttack) ? (BaseAttack)aUserData
                            : (BaseAttack) bUserData;

                    if (!attack.isPlayerAttack()) {
                        attack.setShouldRemove(true);
                        player.takeDamage(attack.getDamage());
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                Body playerBody = player.getBody();

                if (a.getBody() == playerBody || b.getBody() == playerBody) {
                    if (a.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM ||
                        b.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM
                        || a.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY
                        || b.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY) {
                        player.setGrounded(false);
                    }
                }
            }

            @Override
            public void preSolve(final Contact contact, final Manifold manifold) { }

            @Override
            public void postSolve(final Contact contact, final ContactImpulse contactImpulse) { }
        });
    }

}
