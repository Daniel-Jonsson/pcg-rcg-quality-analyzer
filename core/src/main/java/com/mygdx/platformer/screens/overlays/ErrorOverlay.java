package com.mygdx.platformer.screens.overlays;

/**
 * A static utility class for displaying error messages in the game.
 * This overlay can be shown from anywhere in the game by calling the static
 * methods.
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class ErrorOverlay {

    /**
     * Shows an error dialog with the specified title and description.
     *
     * @param title       The title of the error dialog
     * @param description The error message to display
     * @param fatal       Whether the application should exit when the dialog is
     *                    dismissed
     */
    public static void showError(String title, String description, boolean fatal) {
        DialogOverlay.showError(title, description, fatal);
    }

    /**
     * Shows a standard error dialog with the specified title and description.
     *
     * @param title       The title of the error dialog
     * @param description The error message to display
     */
    public static void show(String title, String description) {
        DialogOverlay.show(title, description);
    }

    /**
     * Shows a fatal error dialog that will exit the application when dismissed.
     *
     * @param title       The title of the error dialog
     * @param description The error message to display
     */
    public static void showFatal(String title, String description) {
        DialogOverlay.showFatal(title, description);
    }

    /**
     * Hides the error dialog if it's currently shown.
     */
    public static void hide() {
        DialogOverlay.hide();
    }

    /**
     * Renders the error dialog if it is active.
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
     * Disposes of resources when the error dialog system is no longer needed.
     */
    public static void dispose() {
        DialogOverlay.dispose();
    }
}
