package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Settings;

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

    private float UI_SCALE = 1.0f;

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
                game.setScreen(new GameScreen(game, false, UI_SCALE));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        TextButton autoPlayButton = new TextButton("Auto-play", skin);
        autoPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, true, UI_SCALE));
            }
        });

        // UI scale slider
        Label scaleLabel = new Label("UI Scale", skin);
        Slider scaleSlider = new Slider(0.5f, 2.0f, 0.1f, false, skin);

        UI_SCALE = Settings.getUIScale();

        scaleSlider.setValue(UI_SCALE);


        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                UI_SCALE = scaleSlider.getValue();
                Settings.saveUIScale(UI_SCALE);
            }
        });



        // Arrange UI with Table
        Table table = new Table();
        table.setFillParent(true);

        table.top().padTop(AppConfig.START_SCREEN_TOP_PADDING);

        table.add(titleLabel).padBottom(AppConfig.TITLE_BOTTOM_PADDING).row();

        table.add(startButton)
            .width(AppConfig.BUTTON_WIDTH)
            .height(AppConfig.BUTTON_HEIGHT)
            .padBottom(AppConfig.BUTTON_BOTTOM_PADDING)
            .row();
        table.add(autoPlayButton)
            .width(AppConfig.BUTTON_WIDTH)
            .height(AppConfig.BUTTON_HEIGHT)
            .padBottom(AppConfig.BUTTON_BOTTOM_PADDING)
            .row();
        table.add(quitButton)
            .width(AppConfig.BUTTON_WIDTH)
            .height(AppConfig.BUTTON_HEIGHT)
            .padBottom(AppConfig.BUTTON_BOTTOM_PADDING)
            .row();

        table.add(scaleLabel).padTop(AppConfig.UI_SCALE_SLIDER_PADDING).row();
        table.add(scaleSlider).width(AppConfig.UI_SCALE_SLIDER_WIDTH).padBottom(AppConfig.UI_SCALE_SLIDER_PADDING);



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
