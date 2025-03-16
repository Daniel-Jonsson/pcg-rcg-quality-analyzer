package com.mygdx.platformer.ai.enemy.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;

/**
 * A behavior tree leaf task that implements a basic patrol behavior for enemy
 * characters.
 * <p>
 * This task makes an enemy move in a specific direction until it reaches the
 * edge of a platform, at which point it turns around and continues patrolling in the
 * opposite direction.
 * The task continuously returns {@code Status.RUNNING} while patrolling, and
 * only returns
 * {@code Status.FAILED} if the character reference is null.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class PatrolTask extends LeafTask<AIAgent> {
    /** Tracks the current patrol duration. */
    private float patrolTime;

    /**
     * The time interval after which the enemy will switch direction, in seconds.
     */
    private float switchDirectionTime = 2f;

    /** The current movement direction (1.0f for right, -1.0f for left). */
    private float moveDirection = 1f;

    /**
     * Executes the patrol behavior.
     * <p>
     * Moves the enemy in the current direction and checks if it needs to turn
     * around
     * when reaching an edge or obstacle.
     * </p>
     *
     * @return {@code Status.RUNNING} while patrolling continues,
     *         {@code Status.FAILED} if the character reference is null
     */
    @Override
    public Status execute() {
        AIAgent agent = getObject();
        BaseCharacter character = agent.getCharacter();

        if (character == null) {
            return Status.FAILED;
        }

        BaseEnemy enemy = (BaseEnemy) character;

        if (!enemy.isGroundAhead(moveDirection)) {
            moveDirection *= -1; // Turn around
            patrolTime = 0; // Reset patrol timer
            return Status.RUNNING;
        }

        enemy.setMoveDirection(moveDirection);

        return Status.RUNNING;
    }

    /**
     * Creates a copy of this task for the behavior tree.
     *
     * @param task The {@link Task} instance to copy to
     * @return The copied task
     */
    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        PatrolTask patrolTask = (PatrolTask) task;
        patrolTask.patrolTime = patrolTime;
        patrolTask.switchDirectionTime = switchDirectionTime;
        patrolTask.moveDirection = moveDirection;
        return task;
    }
}
