package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.platformer.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

public class GameScreen extends ScreenAdapter {

   private PlatformerGame game;

   private World world;

   private Sprite sprite;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;


    Player player;

    BodyDef groundBodyDef;

    private float runTime; // tracks how long the game has run


   public GameScreen(PlatformerGame game) {
       this.game = game; // reference main class to enable switching to another screen

        Gdx.app.log(this.getClass().getSimpleName(), "Loaded");
   }

    // Executes when this screen is set as the active screen
    @Override
    public void show() {
        world = new World(new Vector2(0, -9.8f), true); // init world and set y gravity to -10

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false); // invert coordinates (y = 0 at bottom of window)

        viewport = new FitViewport(AppConfig.SCREEN_WIDTH, AppConfig.SCREEN_HEIGHT, camera);
        viewport.apply();  // apply viewport settings

        camera.position.set((float) AppConfig.SCREEN_WIDTH / 2, (float) AppConfig.SCREEN_HEIGHT / 2, 0);
        camera.update();

        player = new Player(world,300, 300);

         createGround();

    }

    @Override
    public void render(float deltaTime) {
        player.update();

        input();
        logic(deltaTime);
        draw();

        doPhysicsStep(deltaTime);
    }

    private void doPhysicsStep(float deltaTime) {

        // max frame time
        float frameTime = Math.min(deltaTime * AppConfig.TIME_SCALE, 0.25f);
        runTime += frameTime;
        while (runTime >= AppConfig.TIME_STEP) {
            world.step(AppConfig.TIME_STEP, AppConfig.VELOCITY_ITERATIONS, AppConfig.POSITION_ITERATIONS);
            runTime -= AppConfig.TIME_STEP;
        }
    }

    private void input() {

    }

    private void logic(float deltaTime) {

    }

    private void draw() {
       // This may not be necessary if we choose to use a background image of some kind
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.render(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
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
    }

    private void createGround() {

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(camera.viewportWidth / 2, 0);

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth / 2, 0);

        groundBody.createFixture(groundBox, 0.0f);

        groundBox.dispose();
    }
}
