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

    /** The name of the application displayed in window title. */
    public static final String APP_NAME = "VEM VARE SOM KASTA?";

    /** Label text for the start game button. */
    public static final String START_GAME_LABEL = "Start Game";

    /** Label text for the auto-play button. */
    public static final String AUTO_PLAY = "Auto-Play";

    /** Label text for the settings button. */
    public static final String SETTINGS = "Settings";

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

    /** Player attack power. **/
    public static final int PLAYER_ATTACK_POWER = 40;

    /** Player attack speed. **/
    public static final float PLAYER_ATTACK_SPEED = 7f;

    /** Player attack spawn offset for x-axis. **/
    public static final float PLAYER_ATTACK_X_OFFSET = 0.3f;

    /** Player attack spawn offset for y-axis. **/
    public static final float PLAYER_ATTACK_Y_OFFSET = 0.1f;

    /** Player attack scale modifier. **/
    public static final float PLAYER_ATTACK_SCALE = 1.0f;

    /** Player healthbar offset for y-axis. **/
    public static final float PLAYER_HEALTHBAR_Y_OFFSET = 0.3f;

    /** Player hitbox width in world units. */
    public static final float PLAYER_HITBOX_SIZE_X = 0.3f;

    /** Player hitbox height in world units. */
    public static final float PLAYER_HITBOX_SIZE_Y = 0.65f;

    /** Player mass for physics simulation. */
    public static final float PLAYER_MASS = 0.35f;

    /** Player hitpoints. */
    public static final int PLAYER_HP = 500;

    /** Offset for player ground detection raycasting. **/
    public static final float PLAYER_GROUNDCHECK_FORWARD_OFFSET = 0.15f;

    /**
     * Length of the player's raycast for ground detection and obstacle avoidance.
     */
    public static final float PLAYER_RAYCAST_LENGTH = 3f;

    /* Procedural Content Generation (PCG) */

    /** Minimum gap between platforms. */
    public static final float MIN_GAP = 1.0f;

    /** Maximum gap between platforms. */
    public static final float MAX_GAP = 3.0f;

    /** Minimum platform width. */
    public static final float MIN_WIDTH = 2.0f;

    /** Maximum platform width. */
    public static final float MAX_WIDTH = 8.0f;

    /** Height of platforms. */
    public static final float PLATFORM_HEIGHT = 0.5f;

    /** Width of the platform start sprite segment. */
    public static final float PLATFORM_START_SPRITE_WIDTH = 0.1f;

    /** Width of the platform end sprite segment. */
    public static final float PLATFORM_END_SPRITE_WIDTH = 0.1f;

    /** Offset for the platform start sprite positioning. */
    public static final float PLATFORM_START_SPRITE_OFFSET = 0.05f;

    /** Base y-position for platforms. */
    public static final float PLATFORM_BASE_Y_POSITION = 5f;

    /** Initial spawn y-coordinate of the player. */
    public static final float PLAYER_SPAWN_Y = PLATFORM_BASE_Y_POSITION + 1f;

    /** Width of a middle segment of a platform. */
    public static final float PLATFORM_MIDDLE_SEGMENT_WIDTH = 0.5f;

    /**
     * Maximum allowed variation in platform y-coordinates at the start of a game.
     */
    public static final float INITIAL_MAX_Y_VARIATION = 1.5f;

    /**
     * Maximum allowed variation in platform y-coordinates at the maximum difficulty
     * level.
     */
    public static final float FINAL_MAX_Y_VARIATION = 3f;

    /** Right margin offscreen for platform spawning. */
    public static final float RIGHT_OFFSCREEN_MARGIN = 2.0f;

    /** Width of the first platform in the game. */
    public static final float FIRST_PLATFORM_WIDTH = 8f;

    /** X-coordinate of the first platform. */
    public static final float FIRST_PLATFORM_X = 4f;

    /** Maximum y-coordinate of platforms. */
    public static final float PLATFORM_MAX_Y_POSITION = SCREEN_HEIGHT * 0.6f;

    /** Minimum y-coordinate of platforms. */
    public static final float PLATFORM_MIN_Y_POSITION = 1.0f;

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

    /** Base width of the player health bar. **/
    public static final float PLAYER_HEALTHBAR_WIDTH = 4f;

    /** Height of the player health bar. **/
    public static final float PLAYER_HEALTHBAR_HEIGHT = 1f;

    /** X-axis offset for the player health bar. **/
    public static final float PLAYER_HEALTHBAR_OFFSET_X = 0.5f;

    /** Y-axis offset for the player health bar. **/
    public static final float PLAYER_HEALTHBAR_OFFSET_Y = 0.5f;

    /** Modifies the scale of the game timer. **/
    public static final float UI_TIMER_MODIFIER = 1.5f;

    /** Default scaling factor for UI elements. */
    public static final float DEFAULT_UI_SCALE = 1.0f;

    /** Padding for the UI scale slider in the settings screen. */
    public static final int UI_SCALE_SLIDER_PADDING = 20;

    /** Width of the UI scale slider in the settings screen. */
    public static final int UI_SCALE_SLIDER_WIDTH = 300;

    /** Width of the game timer display in pixels. */
    public static final float GAME_TIMER_WIDTH = 80f;

    /** Speed at which the background image scrolls. */
    public static final float BACKGROUND_IMAGE_SCROLL_SPEED = 0.3f;

    /* Enemy Configuration */

    /** Base probability of enemy spawning. */
    public static final float BASE_SPAWN_PROBABILITY = 0.5f;

    /** Enemy spawn height relative to platforms. */
    public static final float ENEMY_SPAWN_HEIGHT = 0.5f;

    /** Enemy mass for physics calculations. */
    public static final float ENEMY_MASS = 5f;

    /**
     * The distance to cast ray for checking next platform.
     */
    public static final float JUMP_CHECK_DISTANCE = 6f;

    /** Forward in-air speed multiplier for enemy jumps. **/
    public static final int ENEMY_JUMP_FORWARD_BOOST_MULTIPLIER = 3;

    /** Enemy jump force. **/
    public static final float ENEMY_JUMP_FORCE = 70f;

    /** Enemy forward jump impulse. **/
    public static final float ENEMY_JUMP_FORWARD_BOOST = 20;

    /** Offset for enemy ground detection raycasting. **/
    public static final float ENEMY_GROUNDCHECK_FORWARD_OFFSET = 0.3f;

    /** Update interval for enemy behavior tree in seconds. */
    public static final float ENEMY_BT_UPDATE_INTERVAL = 0.2f;

    /* Collision Categories */

    /** Category bit for players. **/
    public static final short CATEGORY_PLAYER = 0x0001; // 0001

    /** Category bit for enemies. **/
    public static final short CATEGORY_ENEMY = 0x0002; // 0010

    /** Category bit for platforms. **/
    public static final short CATEGORY_PLATFORM = 0x0004; // 0100

    /** Category bit for attacks. **/
    public static final short CATEGORY_ATTACK = 0x0008; // 1000

    /* Goblin Enemy Configuration */

    /** The attack power of a goblin. **/
    public static final int GOBLIN_ATTACK_POWER = 15;

    /** The speed at which a goblin attack moves. **/
    public static final float GOBLIN_ATTACK_SPEED = 3f;

    /** The horizontal offset for the goblin's attack spawn position. **/
    public static final float GOBLIN_ATTACK_X_OFFSET = 0.3f;

    /** The vertical offset for the goblin's attack spawn position. **/
    public static final float GOBLIN_ATTACK_Y_OFFSET = 0.1f;

    /** The scale factor for goblin attack animations. **/
    public static final float GOBLIN_ATTACK_SCALE = 1.0f;

    /** The maximum health points of a goblin. **/
    public static final int GOBLIN_HEALTH = 100;

    /** The movement speed of a goblin. **/
    public static final float GOBLIN_SPEED = 1f;

    /** The scaling factor applied to the goblin's sprite. **/
    public static final float GOBLIN_SCALE = 1.5f;

    /** The width of the goblin in world units. **/
    public static final float GOBLIN_WIDTH = 0.5f;

    /** The height of the goblin in world units. **/
    public static final float GOBLIN_HEIGHT = 0.5f;

    /** The hitbox width of the goblin. **/
    public static final float GOBLIN_HITBOX_SIZE_X = 0.2f;

    /** The hitbox height of the goblin. **/
    public static final float GOBLIN_HITBOX_SIZE_Y = 0.4f;

    /** The detection range at which a goblin can sense the player. **/
    public static final float GOBLIN_DETECTION_RANGE = 5f;

    /** The attack range within which a goblin can attack the player. **/
    public static final float GOBLIN_ATTACK_RANGE = 3f;

    /** The cooldown time (in seconds) between goblin attacks. **/
    public static final float GOBLIN_ATTACK_COOLDOWN = 1f;

    /** Animation key for goblin idle state. */
    public static final String GOBLIN_IDLE = "goblin_idle";

    /** Animation key for goblin walking state. */
    public static final String GOBLIN_WALK = "goblin_walk";

    /** Animation key for goblin attack state. */
    public static final String GOBLIN_ATTACK = "goblin_attack";

    /* Necromancer Enemy Configuration */

    /** The attack power of a necromancer. **/
    public static final int NECROMANCER_ATTACK_POWER = 40;

    /** The speed at which a necromancer attack moves. **/
    public static final float NECROMANCER_ATTACK_SPEED = 4f;

    /** The horizontal offset for the necromancer's attack spawn position. **/
    public static final float NECROMANCER_ATTACK_X_OFFSET = 1.3f;

    /** The vertical offset for the necromancer's attack spawn position. **/
    public static final float NECROMANCER_ATTACK_Y_OFFSET = 0.4f;

    /** The scale factor for necromancer attack animations. **/
    public static final float NECROMANCER_ATTACK_SCALE = 2.0f;

    /** The maximum health points of a necromancer. **/
    public static final int NECROMANCER_HEALTH = 200;

    /** The movement speed of a necromancer. **/
    public static final float NECROMANCER_SPEED = 0.5f;

    /** The scaling factor applied to the necromancer's sprite. **/
    public static final float NECROMANCER_SCALE = 3f;

    /** The width of the necromancer in world units. **/
    public static final float NECROMANCER_WIDTH = 0.5f;

    /** The height of the necromancer in world units. **/
    public static final float NECROMANCER_HEIGHT = 0.5f;

    /** The hitbox width of the necromancer. **/
    public static final float NECROMANCER_HITBOX_SIZE_X = 0.4f;

    /** The hitbox height of the necromancer. **/
    public static final float NECROMANCER_HITBOX_SIZE_Y = 0.65f;

    /** The detection range at which a necromancer can sense the player. **/
    public static final float NECROMANCER_DETECTION_RANGE = 10f;

    /** The attack range within which a necromancer can attack the player. **/
    public static final float NECROMANCER_ATTACK_RANGE = 8;

    /** The cooldown time (in seconds) between necromancer attacks. **/
    public static final float NECROMANCER_ATTACK_COOLDOWN = 4f;

    /** Animation key for necromancer idle state. */
    public static final String NECROMANCER_IDLE = "necromancer_idle";

    /** Animation key for necromancer attack state. */
    public static final String NECROMANCER_ATTACK = "necromancer_attack";

    /* Player Animation Region Names */

    /** Region name for player idle animation. */
    public static final String PLAYER_IDLE_REGION = "player_idle";

    /** Region name for player walk animation. */
    public static final String PLAYER_WALK_REGION = "player_walk";

    /** Region name for player jump animation. */
    public static final String PLAYER_JUMP_REGION = "player_jump";

    /** Region name for player attack animation. */
    public static final String PLAYER_ATTACK_REGION = "player_attack";

    /* UI Text Strings */

    /** UI text for quitting the game. **/
    public static final String QUIT = "Quit";

    /** UI text for returning to the main menu. **/
    public static final String MAIN_MENU = "Main Menu";

    /** UI text label for displaying survival time. **/
    public static final String SURVIVAL_TIME = "Survival Time: ";

    /** UI text displayed when the player loses the game. **/
    public static final String GAME_OVER = "Game Over";

    /* Attack Configuration */

    /** The height of an attack sprite in world units. **/
    public static final float ATTACK_SPRITE_HEIGHT = 0.3f;

    /** The width of an attack sprite in world units. **/
    public static final float ATTACK_SPRITE_WIDTH = 0.6f;

    /** The default damage dealt by an attack. **/
    public static final int BASE_ATTACK_DEFAULT_DMG = 20;

    /** The default speed of an attack projectile. **/
    public static final int BASE_ATTACK_DEFAULT_SPEED = 25;

    /* Animation Durations */

    /** The standard frame duration for animations. **/
    public static final float STANDARD_FRAME_DURATION = 0.1f;

    /** The frame duration for walk animations. **/
    public static final float WALK_FRAME_DURATION = 0.15f;

    /** The frame duration for attack animations. **/
    public static final float ATTACK_FRAME_DURATION = 0.1f;

    /* HealthBar */

    /** The width of the health bar sprite. **/
    public static final float HEALTHBAR_SPRITE_WIDTH = 1.5f;

    /** The height of the health bar sprite. **/
    public static final float HEALTHBAR_SPRITE_HEIGHT = 0.3f;

    /* Attack types */

    /**
     * Enum representing different types of attacks available in the game.
     */
    public enum AttackType {

        /** The player's throwing dagger attack. **/
        PLAYER_THROWING_DAGGER,

        /** The goblin's throwing dagger attack. **/
        GOBLIN_THROWING_DAGGER,

        /** The necromancer's magical death bolt attack. **/
        DEATH_BOLT
    }

    /* DIFFICULTY ADJUSTMENT CONFIGURATIONS */

    /**
     * Enum representing different types of platform generators available in the
     * game.
     */
    public enum PlatformGeneratorType {
        /** Standard platform generator that creates basic platforms. */
        STANDARD,
    }

    /**
     * Enum representing different character types in the game.
     */
    public enum CharacterType {
        /** The player character controlled by the user or AI. */
        PLAYER,
        /** The goblin enemy type. */
        GOBLIN,
        /** The necromancer enemy type. */
        NECROMANCER,
    }

    /**
     * The maximum difficulty level in the game.
     */
    public static final int MAX_DIFFICULTY_LEVEL = 20;

    /** The time interval between difficulty increases in seconds. */
    public static final float DIFFICULTY_INCREASE_INTERVAL = 10f;

    /**
     * The time interval (in seconds) for difficulty level increase.
     */
    public static final float DIFFICULTY_INCREASE_TIME_INTERVAL = 10.0f;

    /**
     * The amount of difficulty level to increase per time interval (5%).
     */
    public static final float DIFFICULTY_INCREASE_AMOUNT = 0.05f;

    /** The minimum multiplier for platform width at maximum difficulty. */
    public static final float MIN_PLATFORM_WIDTH_MULTIPLIER = 0.8f;

    /* Auto-play */

    /** Cooldown for player attacks during auto play. **/
    public static final float AUTO_PLAY_ATTACK_COOLDOWN = 0.2f;

    /** Platform detection tolerance for landing during autoplay. **/
    public static final float AUTO_PLAY_PLATFORM_DETECTION_TOLERANCE = 0.5f;

    /** Backward limit (in game distance units) for autoplay movement. **/
    public static final float AUTO_PLAY_BACKWARD_MOVEMENT_LIMIT = 4f;

    /** Forward limit (in game distance units) for autoplay movement. **/
    public static final float AUTO_PLAY_FORWARD_MOVEMENT_LIMIT = 4f;

    /** Range at which the AI can detect enemies during auto-play. */
    public static final float AUTO_PLAY_ENEMY_DETECTION_RANGE = 10f;

    /** Range at which the AI can detect incoming projectiles during auto-play. */
    public static final float AUTO_PLAY_PROJECTILE_DETECTION_RANGE = 2.5f;

    /** Number of rays used for projectile detection in auto-play mode. */
    public static final int AUTO_PLAY_NUMBER_OF_PROJECTILE_DETECTION_RAYS = 6;

    /** File path for the death bolt sound effect. */
    public static final String SOUND_DEATHBOLT = "sound/deathbolt.mp3";

    /** File path for the first swoosh sound effect. */
    public static final String SOUND_SWOOSH = "sound/swoosh.mp3";

    /** File path for the second swoosh sound effect. */
    public static final String SOUND_SWOOSH2 = "sound/swoosh2.mp3";

    /** File path for the button hover sound effect. */
    public static final String SOUND_BUTTON_HOVER = "sound/button-hover.mp3";

    /** File path for the button click sound effect. */
    public static final String SOUND_BUTTON_CLICK = "sound/button-click.mp3";

    /** File path for the checkbox click sound effect. */
    public static final String SOUND_CHECKBOX_CLICKED = "sound/checkbox-clicked.mp3";

    /** File path for the slider change sound effect. */
    public static final String SOUND_SLIDER_CHANGED = "sound/slider-changed.mp3";

    /* SETTINGS SCREEN CONFIGURATIONS */

    /** Label text for the UI scale setting. */
    public static final String UI_SCALE_LABEL = "UI scale";

    /** Label text for the music volume setting. */
    public static final String MUSIC_VOLUME_LABEL = "Music volume";

    /** Label text for the sound effects volume setting. */
    public static final String EFFECTS_VOLUME_LABEL = "Effects volume";

    /** Label text for the FPS display checkbox. */
    public static final String FPS_CHECKBOX_LABEL = "Show FPS";

    /** Label text for the back button. */
    public static final String BACK_BUTTON_LABEL = "Back";

    /** Minimum value for the UI scale slider. */
    public static final float SETTING_SCALE_SLIDER_MIN_VALUE = 0.5f;

    /** Maximum value for the UI scale slider. */
    public static final float SETTING_SCALE_SLIDER_MAX_VALUE = 2.0f;

    /** Step value for the UI scale slider. */
    public static final float SETTING_SCALE_SLIDER_STEP_VALUE = 0.1f;

    /** Minimum value for the music volume slider. */
    public static final float SETTING_MUSIC_SLIDER_MIN_VALUE = 0.0f;

    /** Maximum value for the music volume slider. */
    public static final float SETTING_MUSIC_SLIDER_MAX_VALUE = 1.0f;

    /** Step value for the music volume slider. */
    public static final float SETTING_MUSIC_SLIDER_STEP_VALUE = 0.05f;

    /** Name of the preferences file for game settings. */
    public static final String SETTINGS_PREFERENCES_NAME = "com.mygdx.platformer.settings";

    /** Key for storing UI scale setting. */
    public static final String SETTINGS_UI_SCALE_KEY = "uiScale";

    /** Key for storing show FPS setting. */
    public static final String SETTINGS_SHOW_FPS_KEY = "showFPS";

    /** Key for storing music volume setting. */
    public static final String SETTINGS_MUSIC_VOLUME_KEY = "musicVolume";

    /** Key for storing effects volume setting. */
    public static final String SETTINGS_EFFECTS_VOLUME_KEY = "effectsVolume";

    /** Default value for showing FPS. */
    public static final boolean SETTINGS_DEFAULT_SHOW_FPS = true;

    /** Default value for music volume. */
    public static final float SETTINGS_DEFAULT_MUSIC_VOLUME = 0.3f;

    /** Default value for effects volume. */
    public static final float SETTINGS_DEFAULT_EFFECTS_VOLUME = 0.5f;

    /* ERROR OVERLAY CONFIGURATIONS */

    /** The width of the error dialog. **/
    public static final int ERROR_DIALOG_WIDTH = SCREEN_WIDTH_PIXELS / 2;

    /** The height of the error dialog. **/
    public static final int ERROR_DIALOG_HEIGHT = SCREEN_HEIGHT_PIXELS / 2;

    /** The top padding of the error dialog content. **/
    public static final int ERROR_DIALOG_PADDING_TOP = 20;

    /** The left padding of the error dialog content. **/
    public static final int ERROR_DIALOG_PADDING_LEFT = 30;

    /** Scale factor for the error dialog scroll pane. */
    public static final float ERROR_DIALOG_SCROLL_SCALE = 1.1f;

    /** Width factor for the error dialog scroll pane. */
    public static final float ERROR_DIALOG_SCROLL_WIDTH = 0.7f;

    /** OK button text. **/
    public static final String OK_BUTTON_TEXT = "OK";

    /* User Guide Dialog */

    /** Title for the user guide dialog. */
    public static final String USER_GUIDE_TITLE = "User Guide";

    /**
     * Description text for the user guide dialog explaining game controls and
     * mechanics.
     */
    public static final String USER_GUIDE_DESCRIPTION = """
            Platformer Game User Guide

            Controls:

            - Movement Left: A
            - Movement Right: D
            - Jump: Space
            - Attack: R

            Objective:

            Survive as long as possible, your survival time is tracked and displayed.

            Game Elements:

            - Player: Has a health bar at the top of the screen if it reaches 0 the game is over
            - Platforms: Procedurally generated, falling off results in game over
            - Enemies: Goblins are fast and jumpy, Necromancers are slow but strong
            - Difficulty: Increases over time with more challenging platforms and enemies

            Good luck!
            """;
}
