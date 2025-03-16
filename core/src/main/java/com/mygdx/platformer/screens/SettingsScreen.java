package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
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
 * Settings screen.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class SettingsScreen extends ScreenAdapter {
    private final Stage stage;
    private final Skin skin;

    private final GameTimer timerPreview;

    private final SpriteBatch batch;
    private final Texture healthBarTexture;
    private final Sprite healthBarSprite;

    /**
     * Settings screen constructor.
     * @param game The game instance.
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
            AppConfig.HEALTHBAR_SPRITE_HEIGHT * AppConfig.PPM * Settings.getUIScale()
        );

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

        table.add(backButton).width(AppConfig.BUTTON_WIDTH).height(AppConfig.BUTTON_HEIGHT).padTop(20);

        stage.addActor(table);
    }

    /**
     * Creates the UI scale slider.
     * @return The UI scale slider.
     */
    private Slider createScaleSlider() {
        Slider scaleSlider = new Slider(AppConfig.SETTING_SCALE_SLIDER_MIN_VALUE,
                AppConfig.SETTING_SCALE_SLIDER_MAX_VALUE
                , AppConfig.SETTING_SCALE_SLIDER_STEP_VALUE, false, skin);
        scaleSlider.setValue(Settings.getUIScale());
        scaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveUIScale(scaleSlider.getValue());
                float scale = scaleSlider.getValue();
                timerPreview.setUIScale(scale);
                healthBarSprite.setSize(
                    AppConfig.HEALTHBAR_SPRITE_WIDTH * AppConfig.PPM * scale,
                    AppConfig.HEALTHBAR_SPRITE_HEIGHT * AppConfig.PPM * scale
                );
                AudioManager.playSound(SoundType.SLIDERCHANGE);
            }
        });
        return scaleSlider;
    }

    /**
     * Creates the music volume Slider.
     * @return Slider object representing the music volume slider.
     */
    private Slider createMusicVolumeSlider() {
        Slider musicVolumeSlider =
                new Slider(AppConfig.SETTING_MUSIC_SLIDER_MIN_VALUE,
                        AppConfig.SETTING_MUSIC_SLIDER_MAX_VALUE,
                        AppConfig.SETTING_MUSIC_SLIDER_STEP_VALUE, false,
                skin);
        musicVolumeSlider.setValue(Settings.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveMusicVolume(musicVolumeSlider.getValue());
                AudioManager.playSound(SoundType.SLIDERCHANGE);
            }
        });
        return musicVolumeSlider;
    }

    /**
     * Creates the effects volume slider.
     * @return Slider object representing the effects volume slider.
     */
    private Slider createEffectsVolumeSlider() {
        Slider effectsVolumeSlider =
                new Slider(AppConfig.SETTING_MUSIC_SLIDER_MIN_VALUE,
                        AppConfig.SETTING_MUSIC_SLIDER_MAX_VALUE, AppConfig.SETTING_MUSIC_SLIDER_STEP_VALUE,
                false, skin);
        effectsVolumeSlider.setValue(Settings.getEffectsVolume());
        effectsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.saveEffectsVolume(effectsVolumeSlider.getValue());
                AudioManager.playSound(SoundType.SLIDERCHANGE);
            }
        });
        return effectsVolumeSlider;
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
        healthBarTexture.dispose();
    }
}
