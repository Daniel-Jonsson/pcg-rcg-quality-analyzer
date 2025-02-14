package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.platformer.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

public class GameScreen extends ScreenAdapter {

   private PlatformerGame game;

   private Sprite sprite;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;


    Player player;

    private Texture texture;        // for graphics test

    private float runTime; // tracks how long the game has run


   public GameScreen(PlatformerGame game) {
       this.game = game; // reference main class to enable switching to another screen

        Gdx.app.log(this.getClass().getSimpleName(), "Loaded");
   }

   private void createTestGraphics() {
       int width = 50;
       int height = 50;

       Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
       pixmap.setColor(Color.RED);
       pixmap.fill();
       pixmap.drawRectangle(0, 0, width, height);

       texture = new Texture(pixmap);
       sprite = new Sprite(texture);
       sprite.setPosition(100, 100);
       sprite.setSize(width, height);

       pixmap.dispose();


   }

    // Executes when this screen is set as the active screen
    @Override
    public void show() {
        Gdx.app.log(this.getClass().getSimpleName(), "Show");

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false); // invert coordinates (y = 0 at bottom of window)

        viewport = new FitViewport(AppConfig.SCREEN_WIDTH, AppConfig.SCREEN_HEIGHT, camera);
        viewport.apply();  // apply viewport settings

        camera.position.set((float) AppConfig.SCREEN_WIDTH / 2, (float) AppConfig.SCREEN_HEIGHT / 2, 0);
        camera.update();

        // createTestGraphics();

        player = new Player(10, 10);


    }



    // updated logics, graphics etc. Equivalent to game loop.
    @Override
    public void render(float deltaTime) {
       input();
       logic(deltaTime);
       draw();
    }

    private void input() {
        // input handling here
    }
    // updates logic
    private void logic(float deltaTime) {
        //runTime += deltaTime;
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
        // Destroy screen's assets here.
        batch.dispose();
        texture.dispose();
    }
}
