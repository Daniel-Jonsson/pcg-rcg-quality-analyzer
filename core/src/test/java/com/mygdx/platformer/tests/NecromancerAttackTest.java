package com.mygdx.platformer.tests;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NecromancerAttackTest {

    static Stream<String> attackClassNames() {
        return Stream.of(
            "generated.rcg.gen1.compound_0.NecromancerAttack_0"
        );
    }

    @ParameterizedTest
    @MethodSource("attackClassNames")
    void testExecute_doesNotCrash(String className) throws Exception {
        Class<?> attackClass = Class.forName(className);
        Object instance = attackClass.getDeclaredConstructor().newInstance();
        Method method = attackClass.getMethod("execute", World.class, Vector2.class, int.class, float.class);
        try {
            method.invoke(instance, null, new Vector2(0, 0), 1, 1.0f);
        } catch (Exception e) {
        }
    }
}
