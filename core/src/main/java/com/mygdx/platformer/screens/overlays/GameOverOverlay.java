package com.mygdx.platformer.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.screens.StartScreen;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.ui.GameButton;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents the Game Over overlay that appears when the player dies.
 * This overlay displays the survival time and provides options to return to
 * the main menu or quit the game.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class GameOverOverlay {
    /** The Scene2D stage that contains and manages all UI elements of the game over
     * overlay. */
    private final Stage stage;
    /** Indicates if the overlay is currently active or not. */
    private boolean isActive = false;

    /**
     * Creates a new Game Over overlay.
     *
     * @param game         The main game instance, used for switching screens.
     * @param survivalTime The time the player survived before dying.
     */
    public GameOverOverlay(final PlatformerGame game,
            float survivalTime) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal(Assets.UI_PATH));

        Label titleLabel = new Label(AppConfig.GAME_OVER, skin);
        Label timeLabel = new Label(AppConfig.SURVIVAL_TIME + String.format("%.2f seconds",
                survivalTime), skin);

        GameButton restartButton = new GameButton(AppConfig.MAIN_MENU, skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new StartScreen(game));
            }
        });

        GameButton quitButton = new GameButton(AppConfig.QUIT, skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(AppConfig.TITLE_BOTTOM_PADDING).row();
        table.add(timeLabel).padBottom(AppConfig.TIMER_PADDING).row();
        table.add(restartButton).width(AppConfig.BUTTON_WIDTH).height(AppConfig.BUTTON_HEIGHT)
                .padBottom(AppConfig.BUTTON_BOTTOM_PADDING).row();
        table.add(quitButton).width(AppConfig.BUTTON_WIDTH).height(AppConfig.BUTTON_HEIGHT);

        stage.addActor(table);
    }

    /**
     * Activates the overlay, making it visible.
     */
    public void show() {
        isActive = true;
    }

    /**
     * Hides the overlay, making it inactive.
     */
    public void hide() {
        isActive = false;
    }

    /**
     * Renders the overlay if it is active.
     */
    public void render() {
        if (isActive) {
            stage.act();
            stage.draw();
        }
    }

    /**
     * Handles window resizing.
     * @param width The new window width.
     * @param height The new window height.
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of resources when the overlay is no longer needed.
     */
    public void dispose() {
        stage.dispose();
    }
}
