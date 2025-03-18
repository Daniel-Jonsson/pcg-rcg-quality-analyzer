package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;
import com.mygdx.platformer.utilities.Settings;

/**
 * Manages the in-game timer, displaying elapsed time in minutes and seconds.
 * The timer updates every frame and is rendered as a UI element on the
 * screen. It uses LibGDX's Scene2D for UI management.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class GameTimer {

    /** The elapsed game time in seconds. */
    private float elapsedTime;

    /** The UI stage that contains the timer label. */
    private Stage stage;

    /** Label displaying the elapsed time. */
    private Label timerLabel;

    /** Label for displaying framerate. **/
    private Label fpsLabel;

    /** The viewport for the stage. */
    private ScreenViewport viewport;

    /**
     * Creates a new game timer and initializes the UI. The timer starts at 0
     * and is displayed in the top-right corner of the screen.
     */
    public GameTimer(float UIScale) {
        elapsedTime = 0;

        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(1f / UIScale / AppConfig.UI_TIMER_MODIFIER);
        // Create the UI Stage
        stage = new Stage(viewport);

        // Load default skin
        Skin skin = new Skin(Gdx.files.internal(Assets.UI_PATH));

        timerLabel = new Label("Time: 00:00", skin);
        fpsLabel = new Label("FPS: 60", skin);
        timerLabel.setAlignment(Align.left);
        fpsLabel.setAlignment(Align.left);


        Table table = new Table();
        table.top().right();
        table.setFillParent(true);
        table.add(timerLabel).width(AppConfig.GAME_TIMER_WIDTH).pad(AppConfig.TIMER_PADDING).row();
        table.add(fpsLabel).width(AppConfig.GAME_TIMER_WIDTH);

        fpsLabel.setVisible(Settings.getShowFPS()); // set fps meter visibility

        stage.addActor(table);
    }

    /**
     * Updates the elapsed time and refreshes the timer label.
     * @param delta The time (in seconds) since the last frame update.
     */
    public void update(float delta) {
        elapsedTime += delta;

        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        String timeString = String.format("Time: %02d:%02d", minutes, seconds);

        timerLabel.setText(timeString);

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    /**
     * Renders the timer UI on the screen.
     */
    public void render() {
        stage.draw();
    }

    /**
     * Disposes of the UI stage to free up resources.
     */
    public void dispose() {
        stage.dispose();
    }

    /**
     * Retrieves the total elapsed time since the game started.
     *
     * @return The elapsed time in seconds.
     */
    public float getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Sets the visibility of the FPS label.
     * @param visible The visibility of the FPS label.
     */
    public void setFPSVisible(boolean visible) {
        fpsLabel.setVisible(visible);
    }

    /**
     * Sets the UI scale for the timer.
     * @param UIScale The UI scale to set.
     */
    public void setUIScale(float UIScale) {
        viewport.setUnitsPerPixel(1f / UIScale / AppConfig.UI_TIMER_MODIFIER);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            true);
        stage.setViewport(viewport);
    }
}
