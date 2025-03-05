package com.mygdx.platformer.ai.enemy;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.ai.tasks.CharacterInRangeTask;
import com.mygdx.platformer.ai.tasks.PursueTask;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.BaseCharacter;

public class EnemyAIAgent extends AIAgent {
    private AttackManager attackManager;
    private float attackCooldown;

    public EnemyAIAgent(BaseCharacter character, float detectionRange, float attackRange, AttackManager attackManager,
            float attackCooldown) {
        super(character, detectionRange, attackRange);
        this.attackManager = attackManager;
        this.attackCooldown = attackCooldown;
        setupBehaviorTree();
    }

    /**
     * Updates the AI agent by updating the target distance and resetting the
     * behavior tree and stepping it once again to see if it needs to pursue the
     * target or attack.
     * 
     * @param deltaTime The time since the last update.
     */
    @Override
    public void update(float deltaTime) {
        updateTargetDistance();
        if (behaviorTree != null) {
            behaviorTree.resetTask();
            behaviorTree.step();
        }
    }

    private void setupBehaviorTree() {
        Selector<AIAgent> root = new Selector<>();

        Sequence<AIAgent> pursueSequence = new Sequence<>();
        pursueSequence.addChild(new CharacterInRangeTask(false));
        pursueSequence.addChild(new PursueTask());
        root.addChild(pursueSequence);

        behaviorTree = new BehaviorTree<>(root);
        behaviorTree.setObject(this);
    }
}
