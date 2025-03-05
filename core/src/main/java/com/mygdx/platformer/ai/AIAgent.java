package com.mygdx.platformer.ai;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.mygdx.platformer.characters.BaseCharacter;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class for AI agents.
 * 
 * @author Daniel Jönsson, Robert Kullman
 */
public abstract class AIAgent {
    protected BaseCharacter character;
    protected BehaviorTree<AIAgent> behaviorTree;
    protected Vector2 targetPosition;
    protected float detectionRange;
    protected float attackRange;
    protected boolean isTargetInRange;
    protected boolean isTargetInAttackRange;
    protected float distanceToTarget;

    /**
     * Constructor for the AIAgent class.
     * 
     * @param character      The character that the AI is controlling.
     * @param detectionRange The range at which the AI can detect the target.
     * @param attackRange    The range at which the AI can attack the target.
     */
    public AIAgent(BaseCharacter character, float detectionRange, float attackRange) {
        this.character = character;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.isTargetInRange = false;
        this.isTargetInAttackRange = false;
        this.distanceToTarget = Float.MAX_VALUE;
    }

    /**
     * Updates the AI agent.
     * 
     * @param deltaTime The time since the last update.
     */
    public abstract void update(float deltaTime);

    /**
     * Updates the behavior tree if it exists.
     */
    protected void updateBehavior() {
        if (behaviorTree != null) {
            behaviorTree.step();
        }
    }

    /**
     * Sets a new target position for the AI.
     * 
     * @param position The new target position.
     */
    public void setTargetPosition(Vector2 position) {
        this.targetPosition = position;
        updateTargetDistance();
    }

    /**
     * Updates the distance to the current target and range flags.
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
     * @return The current position of the AI-controlled character
     */
    public Vector2 getPosition() {
        return character.getBody().getPosition();
    }

    /**
     * @return The current target position
     */
    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    /**
     * @return Whether the target is within detection range
     */
    public boolean isTargetInRange() {
        return isTargetInRange;
    }

    /**
     * @return Whether the target is within attack range
     */
    public boolean isTargetInAttackRange() {
        return isTargetInAttackRange;
    }

    /**
     * @return The current distance to the target
     */
    public float getDistanceToTarget() {
        return distanceToTarget;
    }

    /**
     * Sets the behavior tree for this AI agent.
     * 
     * @param tree The behavior tree to use
     */
    public void setBehaviorTree(BehaviorTree<AIAgent> tree) {
        this.behaviorTree = tree;
    }
}