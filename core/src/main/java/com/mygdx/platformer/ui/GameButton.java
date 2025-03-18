package com.mygdx.platformer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.platformer.sound.AudioManager;
import com.mygdx.platformer.sound.SoundType;

/**
 * A custom button implementation for the platformer game with enhanced
 * interactivity.
 * <p>
 * This class extends LibGDX's TextButton to provide additional game-specific
 * features:
 * <ul>
 * <li>Visual feedback when hovered (color change to red)</li>
 * <li>Cursor changes to hand icon when hovered</li>
 * <li>Sound effects for hover and click interactions</li>
 * <li>Automatic cursor management</li>
 * </ul>
 * </p>
 * <p>
 * GameButton integrates with the AudioManager to play appropriate sound effects
 * when the user interacts with the button, enhancing the user experience with
 * audio feedback.
 * </p>
 * <p>
 * Usage example:
 * 
 * <pre>
 * // Create a button with text and skin
 * GameButton startButton = new GameButton("Start Game", skin);
 * 
 * // Add click handler
 * startButton.addListener(new ClickListener() {
 *     public void clicked(InputEvent event, float x, float y) {
 *         // Handle button click
 *         game.startNewGame();
 *     }
 * });
 * 
 * // Add to stage
 * stage.addActor(startButton);
 * </pre>
 * </p>
 * 
 * @see com.badlogic.gdx.scenes.scene2d.ui.TextButton
 * @see com.mygdx.platformer.sound.AudioManager
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class GameButton extends TextButton {

    /**
     * Flag indicating whether the button is currently being hovered over.
     * <p>
     * This state is used to determine when to change the button's color
     * and is updated by the input listener's enter and exit methods.
     * </p>
     */
    private boolean hover;

    /**
     * Creates a new game button with the specified text and skin.
     * <p>
     * Initializes the button and sets up input listeners to handle hover effects,
     * cursor changes, and sound playback. The button will automatically:
     * <ul>
     * <li>Change color to red when hovered</li>
     * <li>Change the cursor to a hand icon when hovered</li>
     * <li>Play a hover sound when the cursor enters the button</li>
     * <li>Play a click sound when the button is pressed</li>
     * <li>Reset the cursor when the button is no longer hovered or clicked</li>
     * </ul>
     * </p>
     *
     * @param text The text to display on the button
     * @param skin The skin to use for the button's appearance
     */
    public GameButton(String text, Skin skin) {
        super(text, skin);
        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hover = true;
                Gdx.graphics.setSystemCursor(SystemCursor.Hand);
                AudioManager.playSound(SoundType.BUTTONHOVER);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hover = false;
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                AudioManager.playSound(SoundType.BUTTONCLICK);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }
        });
    }

    /**
     * Returns the current color of the button based on hover state.
     * <p>
     * Overrides the default getColor method to change the button's color to red
     * when it is being hovered over, providing visual feedback to the user.
     * When not hovered, returns to the default color from the parent class.
     * </p>
     *
     * @return Color.RED if the button is being hovered over, otherwise the default
     *         color
     */
    @Override
    public Color getColor() {
        return hover ? Color.RED : super.getColor();
    }

}
