package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * This class represents the starting screen of the game, where the main game menu resides.
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class StartScreen implements Screen {
    private PlatformerGame game;
    private Skin skin;
    private Stage stage;

    /**
     * Constructor for the StartScreen, which initializes the UI elements.
     *
     * @param game The main game instance used to switch screens.
     */
    public StartScreen(PlatformerGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // game title
        Label titleLabel = new Label("GAME TITLE", skin);

        // Buttons
        TextButton startButton = new TextButton("Start Game", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });



        // Arrange UI with Table
        Table table = new Table();
        table.setFillParent(true);

        table.top().padTop(50);

        table.add(titleLabel).padBottom(100).row();

        table.add(startButton)
            .width(AppConfig.BUTTON_WIDTH)
            .height(AppConfig.BUTTON_HEIGHT)
            .padBottom(20)
            .row();
        table.add(quitButton)
            .width(AppConfig.BUTTON_WIDTH)
            .height(AppConfig.BUTTON_HEIGHT);

        stage.addActor(table);
    }

    /**
     * Called when the screen is first shown.
     */
    @Override
    public void show() {

    }

    /**
     * Renders the screen by clearing the background and drawing UI elements.
     *
     * @param delta Time elapsed since the last frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the game is paused (e.g., when the application is minimized).
     */
    @Override
    public void pause() {

    }

    /**
     * Called when the game is resumed from a paused state.
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the active screen.
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes of resources when the screen is no longer needed.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
