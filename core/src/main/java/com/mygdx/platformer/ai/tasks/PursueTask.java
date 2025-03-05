package com.mygdx.platformer.ai.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;

public class PursueTask extends LeafTask<AIAgent> {

    @Override
    public Status execute() {
        AIAgent agent = getObject();
        BaseCharacter character = agent.getCharacter();
        Vector2 targetPosition = agent.getTargetPosition();

        BaseEnemy enemy = (BaseEnemy) character;

        float pursueDirection = targetPosition.x > enemy.getBody().getPosition().x ? 1f : -1f;

        enemy.setMoveDirection(pursueDirection);

        return Status.RUNNING;

    }

    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        return task;
    }
}
