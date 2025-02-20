package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.platformer.pcg.PlatformGenerator;
import com.mygdx.platformer.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

public class GameScreen extends ScreenAdapter {

   private PlatformerGame game;

   private World world;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Player player;

    private float runTime; // tracks how long the game has run

    private PlatformGenerator platformGenerator;


   public GameScreen(final PlatformerGame g) {
       this.game = g; // reference main class to enable switching to another screen

        Gdx.app.log(this.getClass().getSimpleName(), "Loaded");
   }

    // Executes when this screen is set as the active screen
    @Override
    public void show() {
        world = new World(new Vector2(0, AppConfig.GRAVITY), true); // init world and set y gravity to -10

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false); // invert coordinates (y = 0 at bottom of window)

        viewport = new FitViewport(AppConfig.SCREEN_WIDTH, AppConfig.SCREEN_HEIGHT, camera);
        viewport.apply();  // apply viewport settings

        camera.position.set(AppConfig.SCREEN_WIDTH / 2, AppConfig.SCREEN_HEIGHT / 2, 0);
        camera.update();

        platformGenerator = new PlatformGenerator(world);

        player = new Player(world, AppConfig.PLAYER_SPAWN_X, AppConfig.PLAYER_SPAWN_Y);

        // createGround();
        initCollisionListener();

    }

    @Override
    public void render(final float deltaTime) {
        player.update();

        camera.position.x += 2f * deltaTime; // example fixed scroll speed (adjust as needed)
        camera.update();

        input();
        logic(deltaTime);

        platformGenerator.update(camera.position.x, AppConfig.SCREEN_WIDTH);

        // This may not be necessary if we choose to use a background image of some kind
        // clear screen
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // render objects
        batch.begin();
        platformGenerator.render(batch);
        player.render(batch);
        batch.end();

        // physics step
        doPhysicsStep(deltaTime);
    }

    private void doPhysicsStep(final float deltaTime) {

        // max frame time
        float frameTime = Math.min(deltaTime * AppConfig.TIME_SCALE, AppConfig.MAX_FRAME_TIME);
        runTime += frameTime;
        while (runTime >= AppConfig.TIME_STEP) {
            world.step(AppConfig.TIME_STEP, AppConfig.VELOCITY_ITERATIONS, AppConfig.POSITION_ITERATIONS);
            runTime -= AppConfig.TIME_STEP;
        }
    }

    private void input() {

    }

    private void logic(final float deltaTime) {

    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);
    }

    // executes when the game loses focus, e.g. when minimized
    @Override
    public void pause() {

        Gdx.app.log(this.getClass().getSimpleName(), "Paused");
    }

    // executed when game regains focus, i.e. when it is brought to the foreground.
    @Override
    public void resume() {
        Gdx.app.log(this.getClass().getSimpleName(), "Resumed");
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
        Gdx.app.log(this.getClass().getSimpleName(), "Hide");
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        player.dispose();
        platformGenerator.dispose();
    }

//    private void createGround() {
//
//        BodyDef groundBodyDef = new BodyDef();
//        groundBodyDef.type = BodyDef.BodyType.StaticBody;
//        groundBodyDef.position.set(camera.viewportWidth / 2, 0);
//
//        Body groundBody = world.createBody(groundBodyDef);
//
//        PolygonShape groundBox = new PolygonShape();
//        groundBox.setAsBox(camera.viewportWidth / 2, 0.01f);
//
//        groundBody.createFixture(groundBox, 0.0f);
//
//        groundBox.dispose();
//    }

    private void initCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                if (a.getBody() == player.getBody() || b.getBody() == player.getBody()) {
                    player.setGrounded(true);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                if (a.getBody() == player.getBody() || b.getBody() == player.getBody()) {
                    player.setGrounded(false);
                }
            }

            @Override
            public void preSolve(final Contact contact, final Manifold manifold) { }

            @Override
            public void postSolve(final Contact contact, final ContactImpulse contactImpulse) { }
        });
    }
}
