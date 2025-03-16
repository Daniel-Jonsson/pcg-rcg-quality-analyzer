package com.mygdx.platformer.ai.enemy.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.ai.AIAgent;

/**
 * A behavior tree leaf task that checks if a target character is within range
 * of the AI agent.
 * This task can check either for general detection range or specific attack
 * range based on the
 * constructor parameter.
 * <p>
 * The task succeeds if the target is within the specified range, and fails
 * otherwise.
 * It also fails if the agent reference is null.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class CharacterInRangeTask extends LeafTask<AIAgent> {
    /**
     * Determines whether to check attack range ({@code true}) or general detection
     * range ({@code false}).
     */
    private boolean checkAttackRange;

    /**
     * Constructs a new CharacterInRangeTask.
     *
     * @param checkAttackRange If {@code true}, checks if target is within attack
     *                         range;
     *                         if {@code false}, checks if target is within general
     *                         detection range
     */
    public CharacterInRangeTask(boolean checkAttackRange) {
        this.checkAttackRange = checkAttackRange;
    }

    /**
     * Executes the range check task.
     *
     * @return {@code Status.SUCCEEDED} if the target
     * is within the specified range,
     *         {@code Status.FAILED} if the target is out of range or if the agent is null
     */
    @Override
    public Status execute() {
        AIAgent agent = getObject();
        if (agent == null) {
            return Status.FAILED;
        }

        boolean inRange = checkAttackRange ? agent.isTargetInAttackRange() : agent.isTargetInRange();

        return inRange ? Status.SUCCEEDED : Status.FAILED;
    }

    /**
     * Creates a copy of this task for the behavior tree.
     *
     * @param task The {@link Task} instance to copy to
     * @return The copied task
     */
    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        CharacterInRangeTask copy = (CharacterInRangeTask) task;
        copy.checkAttackRange = checkAttackRange;
        return task;
    }
}
