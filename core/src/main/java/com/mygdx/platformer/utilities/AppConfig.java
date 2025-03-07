package com.mygdx.platformer.utilities;

/**
 * A configuration class that defines constants used throughout the game.
 * It includes settings for screen dimensions, physics properties,
 * player attributes, procedural content generation, UI elements,
 * enemies, attacks, and animations.
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public final class AppConfig {

    /**
     * Private constructor to prevent instantiation.
     */
    private AppConfig() {
    }

    /* Physics Configuration */

    /** Pixels per meter conversion factor. */
    public static final float PPM = 50f;

    /** Time step used in physics simulation. */
    public static final float TIME_STEP = 1 / 60f;

    /** Physics world time scale. */
    public static final float TIME_SCALE = 1.5f;

    /** Number of velocity iterations for physics calculations. */
    public static final int VELOCITY_ITERATIONS = 6;

    /** Number of position iterations for physics calculations. */
    public static final int POSITION_ITERATIONS = 2;

    /** Gravity strength in the game world. */
    public static final float GRAVITY = -20f;

    /* Screen Configuration */

    /** Screen width in pixels. */
    public static final int SCREEN_WIDTH_PIXELS = 1280;

    /** Screen height in pixels. */
    public static final int SCREEN_HEIGHT_PIXELS = 720;

    /** Default screen width in world units. */
    public static final float SCREEN_WIDTH = SCREEN_WIDTH_PIXELS / PPM;

    /** Default screen height in world units. */
    public static final float SCREEN_HEIGHT = SCREEN_HEIGHT_PIXELS / PPM;

    /** Maximum frame time allowed to prevent lag spikes. */
    public static final float MAX_FRAME_TIME = 0.25f;

    /* Player Configuration */

    /** Initial spawn x-coordinate of the player. */
    public static final int PLAYER_SPAWN_X = 1;

    /** Initial spawn y-coordinate of the player. */
    public static final int PLAYER_SPAWN_Y = 4;

    /** Player movement speed. */
    public static final float PLAYER_MOVE_SPEED = 5f;

    /** Player jump force. */
    public static final float PLAYER_JUMP_FORCE = 1f;

    /** Maximum duration a jump can be held. */
    public static final float MAX_JUMP_HOLD_TIME = 0.15f;

    /** Additional force applied when holding the jump button. */
    public static final float JUMP_HOLD_FORCE = 25f;

    /** Player sprite scaling factor. */
    public static final float PLAYER_SCALE = 1.8f;

    /** Player sprite width in world units. */
    public static final float PLAYER_WIDTH = 0.7f;

    /** Player sprite height in world units. */
    public static final float PLAYER_HEIGHT = 0.7f;

    /** Offset for the player's attack position. */
    public static final float PLAYER_Y_ATTACK_OFFSET = PLAYER_HEIGHT / 3;

    /** Player hitbox width in world units. */
    public static final float PLAYER_HITBOX_SIZE_X = 0.3f;

    /** Player hitbox height in world units. */
    public static final float PLAYER_HITBOX_SIZE_Y = 0.65f;

    /** Player mass for physics simulation. */
    public static final float PLAYER_MASS = 0.35f;

    /** Player hitpoints */
    public static final int PLAYER_HP = 500;

    /* Procedural Content Generation (PCG) */

    /** Minimum gap between platforms. */
    public static final float MIN_GAP = 1.0f;

    /** Maximum gap between platforms. */
    public static final float MAX_GAP = 3.0f;

    /** Minimum platform width. */
    public static final float MIN_WIDTH = 3.0f;

    /** Maximum platform width. */
    public static final float MAX_WIDTH = 6.0f;

    /** Height of platforms. */
    public static final float PLATFORM_HEIGHT = 0.5f;

    /** Base y-position for platforms. */
    public static final float PLATFORM_BASE_Y_POSITION = 2.5f;

    /** Width of a middle segment of a platform. */
    public static final float PLATFORM_MIDDLE_SEGMENT_WIDTH = 0.5f;

    /** Base y-coordinate for generating platforms. */
    public static final float BASE_Y = 2.5f;

    /** Maximum allowed variation in platform y-coordinates. */
    public static final float MAX_Y_VARIATION = 0.5f;

    /** Right margin offscreen for platform spawning. */
    public static final float RIGHT_OFFSCREEN_MARGIN = 2.0f;

    /** Width of the first platform in the game. */
    public static final float FIRST_PLATFORM_WIDTH = 8f;

    /** X-coordinate of the first platform. */
    public static final float FIRST_PLATFORM_X = 4f;

    /* UI Configuration */

    /** Width of UI buttons. */
    public static final int BUTTON_WIDTH = 250;

    /** Height of UI buttons. */
    public static final int BUTTON_HEIGHT = 50;

    /** Padding for the timer UI element. */
    public static final int TIMER_PADDING = 20;

    /** Padding at the top of the start screen. */
    public static final int START_SCREEN_TOP_PADDING = 50;

    /** Bottom padding for the game title on the start screen. */
    public static final int TITLE_BOTTOM_PADDING = 100;

    /** Bottom padding for buttons on the UI. */
    public static final int BUTTON_BOTTOM_PADDING = 20;

    /* Enemy Configuration */

    /** Base probability of enemy spawning. */
    public static final float BASE_SPAWN_PROBABILITY = 0.5f;

    /** Enemy spawn height relative to platforms. */
    public static final float ENEMY_SPAWN_HEIGHT = 0.5f;

    /** Enemy mass for physics calculations. */
    public static final float ENEMY_MASS = 5f;

    /* Collision Categories */

    public static final short CATEGORY_PLAYER = 0x0001;   // 0001
    public static final short CATEGORY_ENEMY = 0x0002;    // 0010
    public static final short CATEGORY_PLATFORM = 0x0004; // 0100
    public static final short CATEGORY_ATTACK = 0x0008;   // 1000

    /* Goblin Enemy Configuration */

    public static final int GOBLIN_ATTACK_POWER = 15;
    public static final int GOBLIN_HEALTH = 250;
    public static final float GOBLIN_SPEED = 1f;
    public static final float GOBLIN_SCALE = 1.5f;
    public static final float GOBLIN_WIDTH = 0.5f;
    public static final float GOBLIN_HEIGHT = 0.5f;
    public static final float GOBLIN_HITBOX_SIZE_X = 0.2f;
    public static final float GOBLIN_HITBOX_SIZE_Y = 0.4f;
    public static final float GOBLIN_DETECTION_RANGE = 5f;
    public static final float GOBLIN_ATTACK_RANGE = 3f;
    public static final float GOBLIN_ATTACK_COOLDOWN = 1f;

    /* Necromancer Enemy Configuration */

    public static final int NECROMANCER_ATTACK_POWER = 20;
    public static final int NECROMANCER_HEALTH = 400;
    public static final float NECROMANCER_SPEED = 0.5f;
    public static final float NECROMANCER_SCALE = 3f;
    public static final float NECROMANCER_WIDTH = 0.5f;
    public static final float NECROMANCER_HEIGHT = 0.5f;
    public static final float NECROMANCER_HITBOX_SIZE_X = 0.4f;
    public static final float NECROMANCER_HITBOX_SIZE_Y = 0.65f;
    public static final float NECROMANCER_DETECTION_RANGE = 10f;
    public static final float NECROMANCER_ATTACK_RANGE = 8;
    public static final float NECROMANCER_ATTACK_COOLDOWN = 4f;

    /* UI Text Strings */

    public static final String QUIT = "Quit";
    public static final String MAIN_MENU = "Main Menu";
    public static final String SURVIVAL_TIME = "Survival Time: ";
    public static final String GAME_OVER = "Game Over";

    /* Attack Configuration */

    public static final float ATTACK_SPRITE_HEIGHT = 0.3f;
    public static final float ATTACK_SPRITE_WIDTH = 0.6f;
    public static final int BASE_ATTACK_DEFAULT_DMG = 20;
    public static final int BASE_ATTACK_DEFAULT_SPEED = 25;

    /* Animation Durations */

    public static final float STANDARD_FRAME_DURATION = 0.1f;
    public static final float WALK_FRAME_DURATION = 0.15f;
    public static final float ATTACK_FRAME_DURATION = 0.1f;

    /* HealthBar */
    public static final float HEALTHBAR_SPRITE_WIDTH = 1.5f;
    public static final float HEALTHBAR_SPRITE_HEIGHT = 0.3f;
}
