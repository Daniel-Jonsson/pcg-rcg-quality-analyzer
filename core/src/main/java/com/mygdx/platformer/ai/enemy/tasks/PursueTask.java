package com.mygdx.platformer.ai.enemy.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.characters.enemies.Goblin;

/**
 * A behavior tree leaf task that implements pursuit behavior for enemy
 * characters.
 * <p>
 * This task makes an enemy move toward the player's location.
 * The enemy will stop pursuing if it gets too close to the target or encounters
 * an edge.
 * Special behavior is implemented for Goblin enemies, allowing them to jump
 * across platforms
 * when pursuing the target.
 * </p>
 * <p>
 * The task continuously returns {@code Status.RUNNING} while pursuing, and only
 * returns
 * {@code Status.FAILED} if the target position is null.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class PursueTask extends LeafTask<AIAgent> {

    /**
     * Executes the pursuit behavior.
     * <p>
     * Moves the enemy toward the target position, handling special cases such as:
     * <ul>
     * <li>Stopping when too close to the target</li>
     * <li>Stopping at platform edges</li>
     * <li>Jumping across platforms (for Goblin enemies)</li>
     * </ul>
     * </p>
     *
     * @return {@code Status.RUNNING} while pursuit continues,
     *         {@code Status.FAILED} if the target position is null
     */
    @Override
    public Status execute() {
        AIAgent agent = getObject();
        BaseCharacter character = agent.getCharacter();
        Vector2 targetPosition = agent.getTargetPosition();

        if (targetPosition == null) {
            return Status.FAILED;
        }

        BaseEnemy enemy = (BaseEnemy) character;
        Vector2 enemyPosition = enemy.getBody().getPosition();

        float distanceToTarget = enemyPosition.dst(targetPosition);
        float targetDirection = targetPosition.x > enemyPosition.x ? 1f : -1f;

        if (enemy instanceof Goblin && !enemy.isGroundAhead(targetDirection) && enemy.canJumpToPlatform(targetDirection)
                && enemy.isGrounded()) {
            enemy.jump();
            return Status.RUNNING;
        }

        if (distanceToTarget <= 1.5f || !enemy.isGroundAhead(targetDirection)) {
            enemy.setMoveDirection(0);
            enemy.setFacingDirection(targetPosition.x > enemyPosition.x ? 1f : -1f);
            return Status.RUNNING;
        }

        float pursueDirection = targetPosition.x > enemyPosition.x ? 1f : -1f;
        enemy.setMoveDirection(pursueDirection);

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
        return task;
    }
}
