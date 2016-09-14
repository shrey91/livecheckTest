package com.liverton.livecheck.common.config.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by sshah on 31/08/2016.
 */
public class IsProductionSystemPropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return "production".equals(conditionContext.getEnvironment().getProperty("environment"));
    }
}