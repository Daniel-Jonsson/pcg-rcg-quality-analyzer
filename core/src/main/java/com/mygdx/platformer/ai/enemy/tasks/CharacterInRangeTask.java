package com.mygdx.platformer.ai.enemy.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.ai.AIAgent;

public class CharacterInRangeTask extends LeafTask<AIAgent> {
    private boolean checkAttackRange;

    public CharacterInRangeTask(boolean checkAttackRange) {
        this.checkAttackRange = checkAttackRange;
    }

    @Override
    public Status execute() {
        AIAgent agent = getObject();
        if (agent == null) {
            return Status.FAILED;
        }

        boolean inRange = checkAttackRange ? agent.isTargetInAttackRange() : agent.isTargetInRange();

        return inRange ? Status.SUCCEEDED : Status.FAILED;
    }

    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        CharacterInRangeTask copy = (CharacterInRangeTask) task;
        copy.checkAttackRange = checkAttackRange;
        return task;
    }
}
