package com.mygdx.platformer.characters.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents the player's health bar displayed as a UI element.
 * <p>
 * The health bar is rendered in the upper left corner of the screen and
 * visually
 * indicates the player's current health as a proportion of their maximum
 * health.
 * The bar automatically follows the camera position to remain visible during
 * gameplay, and its size scales with the player's current health percentage.
 * </p>
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class HealthBar {
    /** Reference to the player character to track health status. */
    private final Player player;

    /**
     * Reference to the game camera for positioning the health bar in screen space.
     */
    private final OrthographicCamera camera;

    /** Reference to the viewport for calculating screen dimensions. */
    private final Viewport viewport;

    /** The sprite used to render the health bar. */
    private final Sprite healthBarSprite;

    /** The maximum width of the health bar in world units. */
    private final float barWidth;

    /** The height of the health bar in world units. */
    private final float barHeight;

    /** The horizontal offset from the left edge of the screen. */
    private final float offsetX;

    /** The vertical offset from the top edge of the screen. */
    private final float offsetY;

    /**
     * Constructs a new health bar for the specified player.
     * <p>
     * Initializes the health bar with the appropriate size and position based on
     * configuration values and the provided UI scale factor.
     * </p>
     *
     * @param player   The player character whose health will be displayed
     * @param camera   The orthographic camera used for rendering the game view
     * @param viewport The viewport that defines the visible game area
     * @param UIScale  Scale factor for UI elements to adjust for different screen
     *                 sizes
     */
    public HealthBar(Player player, OrthographicCamera camera, Viewport viewport, float UIScale) {
        this.player = player;
        this.camera = camera;
        this.viewport = viewport;

        healthBarSprite = new Sprite(new Texture(Assets.HEALTHBAR_TEXTURE));

        barWidth = AppConfig.PLAYER_HEALTHBAR_WIDTH * UIScale;
        barHeight = AppConfig.PLAYER_HEALTHBAR_HEIGHT * UIScale;

        offsetX = AppConfig.PLAYER_HEALTHBAR_OFFSET_X;
        offsetY = AppConfig.PLAYER_HEALTHBAR_OFFSET_Y;
    }

    /**
     * Renders the health bar on screen.
     * <p>
     * Calculates the current health percentage and adjusts the width of the health
     * bar
     * accordingly. The bar is positioned relative to the camera's current position
     * to
     * ensure it stays in the upper left corner of the screen.
     * </p>
     *
     * @param batch The sprite batch used for rendering graphics
     */
    public void render(SpriteBatch batch) {
        float healthPercent = (float) player.getCurrentHealth() / player.getMaxHealth();

        float barX = camera.position.x - (viewport.getWorldWidth() / 2f) + offsetX;
        float barY = camera.position.y + (viewport.getWorldHeight() / 2f) - offsetY - barHeight;

        healthBarSprite.setSize(barWidth * healthPercent, barHeight);
        healthBarSprite.setPosition(barX, barY);

        healthBarSprite.draw(batch);
    }

    /**
     * Releases resources used by the health bar.
     * <p>
     * This method should be called when the health bar is no longer needed
     * to prevent memory leaks.
     * </p>
     */
    public void dispose() {
        if (healthBarSprite.getTexture() != null) {
            healthBarSprite.getTexture().dispose();
        }
    }
}
