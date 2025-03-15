package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.GameTimer;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.characters.player.HealthBar;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Settings;

/**
 * Settings screen.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class SettingsScreen extends ScreenAdapter {
    private final PlatformerGame game;
    private Stage stage;
    private Skin skin;

    private GameTimer timerPreview;
    private HealthBar previewHealthBar;



    /**
     * Settings screen constructor.
     * @param game The game instance.
     */
    public SettingsScreen(final PlatformerGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Preview timer
        timerPreview = new GameTimer(Settings.getUIScale());

        // UI scale slider
        Label scaleLabel = new Label("UI Scale", skin);
        Slider scaleSlider = new Slider(0.5f, 2.0f, 0.1f, false, skin);
        scaleSlider.setValue(Settings.getUIScale());
        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveUIScale(scaleSlider.getValue());
                timerPreview = new GameTimer(Settings.getUIScale());
            }
        });

        // fps checkbox
        CheckBox fpsCheckbox = new CheckBox("Show FPS", skin);
        fpsCheckbox.setChecked(Settings.getShowFPS());
        fpsCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveShowFPS(fpsCheckbox.isChecked());
            }
        });

        // back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StartScreen(game));
            }
        });

        // Arrange UI elements
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(scaleLabel).padTop(AppConfig.UI_SCALE_SLIDER_PADDING).row();
        table.add(scaleSlider).width(AppConfig.UI_SCALE_SLIDER_WIDTH)
            .padBottom(AppConfig.UI_SCALE_SLIDER_PADDING)
            .row();

        table.add(fpsCheckbox)
            .padTop(AppConfig.UI_SCALE_SLIDER_PADDING)
            .padBottom(AppConfig.UI_SCALE_SLIDER_PADDING)
            .row();

        table.add(backButton).width(AppConfig.BUTTON_WIDTH).height(AppConfig.BUTTON_HEIGHT).padTop(20);

        stage.addActor(table);
    }

    /**
     * Renders the screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
        timerPreview.render();
    }

    /**
     * Updates the viewport with new size parameters upon resizing.
     * @param width The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of loaded entities to free up resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        timerPreview.dispose();
        previewHealthBar.dispose();
    }
}
