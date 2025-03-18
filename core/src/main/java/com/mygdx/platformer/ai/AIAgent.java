package com.mygdx.platformer.ai;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.characters.BaseCharacter;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract base class for all AI agents in the game.
 * <p>
 * This class provides the core functionality for AI-controlled characters,
 * including target tracking, distance calculations, and behavior tree
 * integration.
 * It serves as the foundation for specialized AI implementations such as enemy
 * AI.
 * </p>
 * <p>
 * The AIAgent maintains information about:
 * <ul>
 * <li>The character being controlled</li>
 * <li>Target position and distance</li>
 * <li>Detection and attack ranges</li>
 * <li>Line-of-sight calculations</li>
 * </ul>
 * </p>
 * <p>
 * Concrete implementations must provide their own update logic and behavior
 * tree
 * configurations to define specific AI behaviors.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public abstract class AIAgent {
    /** The character entity controlled by this AI agent. */
    protected BaseCharacter character;

    /** The behavior tree that defines this agent's decision-making process. */
    protected BehaviorTree<AIAgent> behaviorTree;

    /** The current target position this agent is tracking. */
    protected Vector2 targetPosition;

    /** The maximum distance at which this agent can detect targets. */
    protected float detectionRange;

    /** The maximum distance at which this agent can attack targets. */
    protected float attackRange;

    /** Flag indicating whether the target is within detection range. */
    protected boolean isTargetInRange;

    /** Flag indicating whether the target is within attack range. */
    protected boolean isTargetInAttackRange;

    /** The current calculated distance to the target. */
    protected float distanceToTarget;

    /** Reference to the game world for physics and ray casting operations. */
    protected World world;

    /**
     * Constructor for the AIAgent class.
     *
     * @param character      The character that the AI is controlling.
     * @param detectionRange The range at which the AI can detect the target.
     * @param attackRange    The range at which the AI can attack the target.
     * @param world          The game world.
     */
    public AIAgent(BaseCharacter character, float detectionRange, float attackRange, World world) {
        this.character = character;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.isTargetInRange = false;
        this.isTargetInAttackRange = false;
        this.distanceToTarget = Float.MAX_VALUE;
        this.world = world;
    }

    /**
     * Updates the AI agent's state and behavior.
     * <p>
     * This method should be called every frame to update the agent's
     * decision-making process and actions based on the current game state.
     * </p>
     *
     * @param deltaTime The time in seconds since the last update.
     */
    public abstract void update(float deltaTime);

    /**
     * Sets a new target position for the AI to track.
     * <p>
     * This method updates the target position and recalculates
     * the distance and range flags accordingly.
     * </p>
     *
     * @param position The new target position.
     */
    public void setTargetPosition(Vector2 position) {
        this.targetPosition = position;
        updateTargetDistance();
    }

    /**
     * Updates the distance to the current target and range flags.
     * <p>
     * This method recalculates the distance to the target and updates
     * the flags indicating whether the target is within detection and attack
     * ranges.
     * </p>
     */
    protected void updateTargetDistance() {
        if (targetPosition != null && character != null) {
            Vector2 currentPos = character.getBody().getPosition();
            distanceToTarget = currentPos.dst(targetPosition);
            isTargetInRange = distanceToTarget <= detectionRange;
            isTargetInAttackRange = distanceToTarget <= attackRange;
        }
    }

    /**
     * Gets the current position of the AI-controlled character.
     *
     * @return The current position vector of the character
     */
    public Vector2 getPosition() {
        return character.getBody().getPosition();
    }

    /**
     * Gets the character that the AI is controlling.
     *
     * @return The character controlled by this AI agent
     */
    public BaseCharacter getCharacter() {
        return character;
    }

    /**
     * Gets the current target position.
     *
     * @return The position vector of the current target, or null if no target is
     *         set
     */
    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    /**
     * Checks if the target is within detection range.
     *
     * @return true if the target is within detection range, false otherwise
     */
    public boolean isTargetInRange() {
        return isTargetInRange;
    }

    /**
     * Checks if the target is within attack range and line of sight.
     * <p>
     * This method performs a more detailed check than the simple distance check,
     * including verifying that there is a clear line of sight to the target.
     * </p>
     *
     * @return true if the target is within attack range and line of sight, false
     *         otherwise
     */
    public boolean isTargetInAttackRange() {
        if (targetPosition == null) {
            return false;
        }

        Vector2 enemyPos = character.getBody().getPosition();
        Vector2 playerPos = targetPosition;

        if (Math.abs(enemyPos.x - playerPos.x) > attackRange) {
            return false;
        }

        Vector2 rayStart = new Vector2(enemyPos.x, enemyPos.y);
        Vector2 rayEnd = new Vector2(playerPos.x, enemyPos.y);

        return character.checkLineOfSight(world, rayStart, rayEnd);
    }

}
