package com.mygdx.platformer.ai.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;


public class PatrolTask extends LeafTask<AIAgent> {
    private float patrolTime;
    private float switchDirectionTime = 2f;
    private float moveDirection = 1f;

    @Override
    public Status execute() {
        AIAgent agent = getObject();
        BaseCharacter character = agent.getCharacter();
        if (character == null) {
            return Status.FAILED;
        }
        
        BaseEnemy enemy = (BaseEnemy) character;

        enemy.setMoveDirection(moveDirection);

        patrolTime += Gdx.graphics.getDeltaTime();
        if (patrolTime >= switchDirectionTime) {
            moveDirection *= -1;
            patrolTime = 0;
        }

        return Status.RUNNING;
    }

    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        PatrolTask patrolTask = (PatrolTask) task;
        patrolTask.patrolTime = patrolTime;
        patrolTask.switchDirectionTime = switchDirectionTime;
        patrolTask.moveDirection = moveDirection;
        return task;
    }
}
