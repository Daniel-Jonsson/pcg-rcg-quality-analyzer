package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {

   private PlatformerGame game;

   private Sprite sprite;

    private SpriteBatch batch;
    private OrthographicCamera camera;
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


   }

    // Executes when this screen is set as the active screen
    @Override
    public void show() {
        Gdx.app.log(this.getClass().getSimpleName(), "Show");

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false); // invert coordinates (y = 0 at bottom of window)

        createTestGraphics();

    }

    // updates logic
    private void update(float deltaTime) {
        runTime += deltaTime;
        sprite.translate(1, 0);
    }

    // updated logics, graphics etc. Equivalent to game loop.
    @Override
    public void render(float deltaTime) {
        // buffer screen
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        // update here
        update(deltaTime);
        sprite.rotate(1);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        batch.begin();  // render
        sprite.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
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
    }
}
