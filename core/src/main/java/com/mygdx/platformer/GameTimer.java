package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.utilities.AppConfig;

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

    /**
     * Creates a new game timer and initializes the UI. The timer starts at 0
     * and is displayed in the top-right corner of the screen.
     */
    public GameTimer() {
        elapsedTime = 0;

        // Create the UI Stage
        stage = new Stage(new ScreenViewport());

        // Load default skin
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        timerLabel = new Label("Time: 00:00", skin);


        Table table = new Table();
        table.top().right();
        table.setFillParent(true);
        table.add(timerLabel).pad(AppConfig.TIMER_PADDING);

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
}
