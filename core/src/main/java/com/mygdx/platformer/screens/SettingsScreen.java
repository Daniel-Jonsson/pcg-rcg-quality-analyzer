package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.GameTimer;
import com.mygdx.platformer.PlatformerGame;
import com.mygdx.platformer.sound.AudioManager;
import com.mygdx.platformer.sound.SoundType;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;
import com.mygdx.platformer.utilities.Settings;
import com.mygdx.platformer.ui.GameButton;

/**
 * The settings screen of the platformer game.
 * <p>
 * This screen provides a user interface for adjusting various game settings,
 * including:
 * <ul>
 * <li>UI scale - Controls the size of UI elements like the health bar and
 * timer</li>
 * <li>Music volume - Adjusts the volume level of background music</li>
 * <li>Sound effects volume - Adjusts the volume level of game sound
 * effects</li>
 * <li>FPS display toggle - Enables or disables the frames-per-second
 * counter</li>
 * </ul>
 * </p>
 * <p>
 * The screen provides real-time previews of UI scaling changes by displaying
 * a sample timer and health bar that update as settings are adjusted. All
 * settings
 * are persisted between game sessions using the Settings utility class.
 * </p>
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class SettingsScreen extends ScreenAdapter {
    /**
     * The Scene2D stage that contains and manages all UI elements.
     * <p>
     * This stage handles the rendering of UI components and processes
     * user input for interactive elements like sliders and buttons.
     * </p>
     */
    private final Stage stage;

    /**
     * The UI skin used for styling all interface components.
     * <p>
     * Loaded from the Assets.UI_PATH, this skin provides consistent
     * visual styling for all UI elements including sliders, buttons,
     * labels, and checkboxes.
     * </p>
     */
    private final Skin skin;

    /**
     * A preview instance of the game timer.
     * <p>
     * This timer is displayed on the settings screen to provide a
     * real-time preview of how UI scaling affects the timer's appearance.
     * It updates dynamically when the UI scale setting is changed.
     * </p>
     */
    private final GameTimer timerPreview;

    /**
     * SpriteBatch for rendering the health bar preview.
     * <p>
     * Used to draw the health bar sprite that demonstrates
     * how UI scaling affects the health bar's appearance in-game.
     * </p>
     */
    private final SpriteBatch batch;

    /**
     * Texture for the health bar preview.
     * <p>
     * The base texture loaded from Assets.HEALTHBAR_TEXTURE that
     * is used to create the health bar sprite for preview purposes.
     * </p>
     */
    private final Texture healthBarTexture;

    /**
     * Sprite for the health bar preview.
     * <p>
     * A sprite created from the health bar texture that can be
     * scaled and positioned to demonstrate UI scaling effects.
     * Its size updates dynamically when the UI scale setting is changed.
     * </p>
     */
    private final Sprite healthBarSprite;

    /**
     * The button for moving left.
     */
    private GameButton moveLeftButton;

    /**
     * The button for moving right.
     */
    private GameButton moveRightButton;

    /**
     * The button for jumping.
     */
    private GameButton jumpButton;

    /**
     * The button for attacking.
     */
    private GameButton attackButton;
    /**
     * Creates a new settings screen with UI components for adjusting game settings.
     * <p>
     * Initializes the stage, skin, and preview elements, and sets up all UI
     * components
     * including sliders for UI scale and volume controls, a checkbox for FPS
     * display,
     * and a back button to return to the start screen.
     * </p>
     *
     * @param game The main game instance, used for switching back to the start
     *             screen
     */
    public SettingsScreen(final PlatformerGame game) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal(Assets.UI_PATH));

        // Preview timer
        timerPreview = new GameTimer(Settings.getUIScale());

        batch = new SpriteBatch();
        healthBarTexture = new Texture(Gdx.files.internal(Assets.HEALTHBAR_TEXTURE));
        healthBarSprite = new Sprite(healthBarTexture);

        healthBarSprite.setSize(
                AppConfig.HEALTHBAR_SPRITE_WIDTH * AppConfig.PPM * Settings.getUIScale(),
                AppConfig.HEALTHBAR_SPRITE_HEIGHT * AppConfig.PPM * Settings.getUIScale());

        float barX = stage.getViewport().getWorldWidth() / 2f;
        float barY = stage.getViewport().getWorldHeight() / 2f;

        healthBarSprite.setPosition(barX, barY);

        // UI scale slider
        Label scaleLabel = new Label(AppConfig.UI_SCALE_LABEL, skin);
        Slider scaleSlider = createScaleSlider();

        // music volume slider
        Label musicVolumeLabel = new Label(AppConfig.MUSIC_VOLUME_LABEL, skin);
        Slider musicVolumeSlider = createMusicVolumeSlider();

        // effects volume slider
        Label effectsVolumeLabel = new Label(AppConfig.EFFECTS_VOLUME_LABEL, skin);
        Slider effectsVolumeSlider = createEffectsVolumeSlider();

        // fps checkbox
        CheckBox fpsCheckbox = new CheckBox(AppConfig.FPS_CHECKBOX_LABEL, skin);
        fpsCheckbox.setChecked(Settings.getShowFPS());
        fpsCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveShowFPS(fpsCheckbox.isChecked());
                AudioManager.playSound(SoundType.CHECKBOXCLICK);
            }
        });

        final Table keybindingTable = initKeybindingTable();

        // back button
        GameButton backButton = new GameButton(AppConfig.BACK_BUTTON_LABEL, skin);
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
        table.add(musicVolumeLabel).padTop(AppConfig.UI_SCALE_SLIDER_PADDING).row();
        table.add(musicVolumeSlider).width(AppConfig.UI_SCALE_SLIDER_WIDTH)
                .padBottom(AppConfig.UI_SCALE_SLIDER_PADDING)
                .row();
        table.add(effectsVolumeLabel).padTop(AppConfig.UI_SCALE_SLIDER_PADDING).row();
        table.add(effectsVolumeSlider).width(AppConfig.UI_SCALE_SLIDER_WIDTH)
                .padBottom(AppConfig.UI_SCALE_SLIDER_PADDING)
                .row();

        table.add(fpsCheckbox)
                .padTop(AppConfig.UI_SCALE_SLIDER_PADDING)
                .padBottom(AppConfig.UI_SCALE_SLIDER_PADDING)
                .row();

        table.add(keybindingTable).row();
        table.add(backButton).width(AppConfig.BUTTON_WIDTH).height(AppConfig.BUTTON_HEIGHT)
                .padTop(AppConfig.BUTTON_BOTTOM_PADDING);

        stage.addActor(table);
    }

    /**
     * Creates and configures the UI scale slider.
     * <p>
     * Sets up a slider with appropriate min/max values and step size for adjusting
     * the UI scale. Adds a listener that updates the Settings, preview timer, and
     * health bar sprite in real-time as the slider value changes.
     * </p>
     *
     * @return A configured Slider for adjusting UI scale
     */
    private Slider createScaleSlider() {
        Slider scaleSlider = new Slider(AppConfig.SETTING_SCALE_SLIDER_MIN_VALUE,
                AppConfig.SETTING_SCALE_SLIDER_MAX_VALUE, AppConfig.SETTING_SCALE_SLIDER_STEP_VALUE, false, skin);
        scaleSlider.setValue(Settings.getUIScale());
        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveUIScale(scaleSlider.getValue());
                float scale = scaleSlider.getValue();
                timerPreview.setUIScale(scale);
                healthBarSprite.setSize(
                        AppConfig.HEALTHBAR_SPRITE_WIDTH * AppConfig.PPM * scale,
                        AppConfig.HEALTHBAR_SPRITE_HEIGHT * AppConfig.PPM * scale);
                AudioManager.playSound(SoundType.SLIDERCHANGE);
            }
        });
        return scaleSlider;
    }

    /**
     * Creates and configures the music volume slider.
     * <p>
     * Sets up a slider with appropriate min/max values and step size for adjusting
     * the music volume. Adds a listener that updates the Settings and plays a sound
     * effect when the slider value changes.
     * </p>
     *
     * @return A configured Slider for adjusting music volume
     */
    private Slider createMusicVolumeSlider() {
        Slider musicVolumeSlider = new Slider(AppConfig.SETTING_MUSIC_SLIDER_MIN_VALUE,
                AppConfig.SETTING_MUSIC_SLIDER_MAX_VALUE,
                AppConfig.SETTING_MUSIC_SLIDER_STEP_VALUE, false,
                skin);
        musicVolumeSlider.setValue(Settings.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveMusicVolume(musicVolumeSlider.getValue());
                AudioManager.playSound(SoundType.SLIDERCHANGE);
                AudioManager.getBackgroundMusic().setVolume(Settings.getMusicVolume());
            }
        });
        return musicVolumeSlider;
    }

    /**
     * Creates and configures the sound effects volume slider.
     * <p>
     * Sets up a slider with appropriate min/max values and step size for adjusting
     * the sound effects volume. Adds a listener that updates the Settings and plays
     * a sound effect when the slider value changes.
     * </p>
     *
     * @return A configured Slider for adjusting sound effects volume
     */
    private Slider createEffectsVolumeSlider() {
        Slider effectsVolumeSlider = new Slider(AppConfig.SETTING_MUSIC_SLIDER_MIN_VALUE,
                AppConfig.SETTING_MUSIC_SLIDER_MAX_VALUE, AppConfig.SETTING_MUSIC_SLIDER_STEP_VALUE,
                false, skin);
        effectsVolumeSlider.setValue(Settings.getEffectsVolume());
        effectsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveEffectsVolume(effectsVolumeSlider.getValue());
                AudioManager.playSound(SoundType.SLIDERCHANGE);
                AudioManager.setEffectsVolume(Settings.getEffectsVolume());
            }
        });
        return effectsVolumeSlider;
    }

    /**
     * Renders the settings screen.
     * <p>
     * Clears the screen, draws the UI components using the stage, renders the
     * timer preview, and draws the health bar preview sprite. The health bar
     * position and size are updated based on the current UI scale setting.
     * </p>
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
        timerPreview.render();

        // draw dummy healthbar sprite
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();

        float UIScale = Settings.getUIScale();

        float barWidth = AppConfig.PLAYER_HEALTHBAR_WIDTH * UIScale * AppConfig.PPM;
        float barHeight = AppConfig.PLAYER_HEALTHBAR_HEIGHT * UIScale * AppConfig.PPM;

        float offsetX = AppConfig.PLAYER_HEALTHBAR_OFFSET_X * AppConfig.PPM;
        float offsetY = AppConfig.PLAYER_HEALTHBAR_OFFSET_Y * AppConfig.PPM;

        float barX = stage.getCamera().position.x - (stage.getViewport().getWorldWidth() / 2f) + offsetX;
        float barY = stage.getCamera().position.y + (stage.getViewport().getWorldHeight() / 2f) - offsetY - barHeight;

        healthBarSprite.setSize(barWidth, barHeight);
        healthBarSprite.setPosition(barX, barY);

        healthBarSprite.draw(batch);
        batch.end();
    }

    /**
     * Updates the viewport when the screen is resized.
     * <p>
     * Ensures that UI elements maintain their proper positions and proportions
     * when the window size changes.
     * </p>
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Releases resources when the screen is no longer needed.
     * <p>
     * Disposes of the stage, skin, timer preview, and health bar texture
     * to prevent memory leaks.
     * </p>
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        timerPreview.dispose();
        healthBarTexture.dispose();
    }

    private Table initKeybindingTable() {
        final Table keybindingTable = new Table();
        keybindingTable.defaults().pad(AppConfig.SETTINGS_KEYBINDING_TABLE_PADDING)
                .width(AppConfig.SETTINGS_KEYBINDING_TABLE_WIDTH).height(AppConfig.SETTINGS_KEYBINDING_TABLE_HEIGHT);

        keybindingTable.add(new Label(AppConfig.SETTINGS_MOVE_LEFT_LABEL, skin)).left();

        moveLeftButton = new GameButton(Settings.getKeyName(Settings.getMoveLeftKey()), skin);
        moveLeftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waitForKeyInput(moveLeftButton, new KeyCallback() {
                    @Override
                    public boolean onKeyPressed(int keycode) {
                        Settings.saveMoveLeftKey(keycode);
                        moveLeftButton.setText(Settings.getKeyName(keycode));
                        return true;
                    }

                    @Override
                    public Key getKey() {
                        return Key.MOVE_LEFT;
                    }
                });
            }
        });

        keybindingTable.add(moveLeftButton).right().row();

        keybindingTable.add(new Label(AppConfig.SETTINGS_MOVE_RIGHT_LABEL, skin)).left();

        moveRightButton = new GameButton(Settings.getKeyName(Settings.getMoveRightKey()), skin);
        moveRightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waitForKeyInput(moveRightButton, new KeyCallback() {
                    @Override
                    public boolean onKeyPressed(int keycode) {
                        Settings.saveMoveRightKey(keycode);
                        moveRightButton.setText(Settings.getKeyName(keycode));
                        return true;
                    }

                    @Override
                    public Key getKey() {
                        return Key.MOVE_RIGHT;
                    }
                });
            }
        });

        keybindingTable.add(moveRightButton).right().row();

        keybindingTable.add(new Label(AppConfig.SETTINGS_JUMP_LABEL, skin)).left();

        jumpButton = new GameButton(Settings.getKeyName(Settings.getJumpKey()), skin);
        jumpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waitForKeyInput(jumpButton, new KeyCallback() {
                    @Override
                    public boolean onKeyPressed(int keycode) {
                        Settings.saveJumpKey(keycode);
                        jumpButton.setText(Settings.getKeyName(keycode));
                        return true;
                    }

                    @Override
                    public Key getKey() {
                        return Key.JUMP;
                    }
                });
            }
        });

        keybindingTable.add(jumpButton).right().row();

        keybindingTable.add(new Label(AppConfig.SETTINGS_ATTACK_LABEL, skin)).left();

        attackButton = new GameButton(Settings.getKeyName(Settings.getAttackKey()), skin);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waitForKeyInput(attackButton, new KeyCallback() {
                    @Override
                    public boolean onKeyPressed(int keycode) {
                        Settings.saveAttackKey(keycode);
                        attackButton.setText(Settings.getKeyName(keycode));
                        return true;
                    }

                    @Override
                    public Key getKey() {
                        return Key.ATTACK;
                    }
                });
            }
        });

        keybindingTable.add(attackButton).right().row();

        return keybindingTable;
    }

    /**
     * Updates all keybinding buttons to show the current key assignments.
     * This ensures all buttons reflect the current state accurately after any
     * changes.
     */
    private void updateAllKeybindingButtons() {
        moveLeftButton.setText(Settings.getKeyName(Settings.getMoveLeftKey()));
        moveRightButton.setText(Settings.getKeyName(Settings.getMoveRightKey()));
        jumpButton.setText(Settings.getKeyName(Settings.getJumpKey()));
        attackButton.setText(Settings.getKeyName(Settings.getAttackKey()));
    }

    /**
     * Waits for a key input and handles the key binding process.
     * <p>
     * This method displays a prompt to the user to press a key, and then assigns
     * the pressed key to the specified action.
     * </p>
     * 
     * @param button   The button to update with the key input
     * @param callback The callback to handle the key input
     */
    private void waitForKeyInput(GameButton button, KeyCallback callback) {
        final String originalText = button.getText().toString();

        button.setText("Press a key...");
        InputProcessor originalProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    button.setText(originalText);
                    Gdx.input.setInputProcessor(originalProcessor);
                    return true;
                }

                boolean isAlreadyAssigned = false;

                if (keycode == Settings.getMoveLeftKey() && callback.getKey() != Key.MOVE_LEFT) {
                    isAlreadyAssigned = true;
                } else if (keycode == Settings.getMoveRightKey() && callback.getKey() != Key.MOVE_RIGHT) {
                    isAlreadyAssigned = true;
                } else if (keycode == Settings.getJumpKey() && callback.getKey() != Key.JUMP) {
                    isAlreadyAssigned = true;
                } else if (keycode == Settings.getAttackKey() && callback.getKey() != Key.ATTACK) {
                    isAlreadyAssigned = true;
                }

                if (isAlreadyAssigned) {
                    button.setText(originalText);
                    Gdx.input.setInputProcessor(originalProcessor);
                    return true;
                }

                boolean result = callback.onKeyPressed(keycode);
                Gdx.input.setInputProcessor(originalProcessor);

                updateAllKeybindingButtons();

                AudioManager.playSound(SoundType.BUTTONCLICK);
                return result;
            }
        });
    }

    /**
     * Interface for handling key callbacks with knowledge of which key action is
     * being modified.
     * This allows the callback to know which action it's assigned to for proper
     * debouncing.
     */
    private interface KeyCallback {
        /**
         * Called when a key is pressed during key binding.
         * 
         * @param keycode The key code that was pressed
         * @return True if the key was successfully assigned, false otherwise
         */
        boolean onKeyPressed(int keycode);

        /**
         * Gets the action type this callback is for.
         * 
         * @return The Key enum value representing this action
         */
        Key getKey();
    }

    /**
     * Enum representing the available key actions for binding.
     * Used to identify which action is being modified during key binding.
     */
    private enum Key {
        MOVE_LEFT,
        MOVE_RIGHT,
        JUMP,
        ATTACK
    }
}
