package com.mygdx.platformer.screens.overlays;

/**
 * A static utility class for displaying user guide information in the game.
 * This overlay can be shown from anywhere in the game by calling the static
 * methods.
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class UserGuideOverlay {

    /**
     * Shows a user guide dialog with the specified title and description.
     *
     * @param title       The title of the dialog
     * @param description The guide content to display
     */
    public static void show(String title, String description) {
        DialogOverlay.show(title, description);
    }

    /**
     * Hides the user guide dialog if it's currently shown.
     */
    public static void hide() {
        DialogOverlay.hide();
    }

    /**
     * Renders the user guide dialog if it is active.
     */
    public static void render() {
        DialogOverlay.render();
    }

    /**
     * Updates the stage viewport when the screen is resized.
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    public static void resize(int width, int height) {
        DialogOverlay.resize(width, height);
    }

    /**
     * Disposes of resources when the user guide dialog system is no longer needed.
     */
    public static void dispose() {
        DialogOverlay.dispose();
    }
}
