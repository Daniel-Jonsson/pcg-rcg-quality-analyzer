package com.mygdx.platformer.ai;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.mygdx.platformer.characters.BaseCharacter;
import com.badlogic.gdx.math.Vector2;

public abstract class AIAgent {
    protected BaseCharacter character;
    protected BehaviorTree<AIAgent> behaviorTree;
    protected Vector2 targetPosition;
    protected float detectionRange;
    protected float attackRange;
    protected boolean isTargetInRange;
    protected boolean isTargetInAttackRange;
    protected float distanceToTarget;

    public AIAgent(BaseCharacter character, float detectionRange, float attackRange) {
        this.character = character;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.isTargetInRange = false;
        this.isTargetInAttackRange = false;
        this.distanceToTarget = Float.MAX_VALUE;
    }
} 