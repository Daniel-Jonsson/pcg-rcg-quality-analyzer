package com.mygdx.platformer.characters.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.platformer.utilities.Assets;

/**
 * This class represents the player health bar, rendered in the upper left corner of the screen.
 */
public class HealthBar {
    private final Player player;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Sprite healthBarSprite;

    private final float barWidth;
    private final float barHeight;
    private final float offsetX;
    private final float offsetY;

    /**
     * Constructor for the HealthBar.
     *
     * @param player Player instance.
     * @param camera Orthographic camera instance.
     * @param viewport Viewport instance.
     */
    public HealthBar(Player player, OrthographicCamera camera, Viewport viewport) {
        this.player = player;
        this.camera = camera;
        this.viewport = viewport;

        healthBarSprite = new Sprite(new Texture(Assets.HEALTHBAR_TEXTURE));

        barWidth = 4f;
        barHeight = 1f;

        offsetX = 0.5f;
        offsetY = 0.5f;
    }

    /**
     * Renders the health bar using SpriteBatch.
     *
     * @param batch The game's SpriteBatch instance.
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
     * Dispose of resources used by the health bar.
     */
    public void dispose() {
    }
}
