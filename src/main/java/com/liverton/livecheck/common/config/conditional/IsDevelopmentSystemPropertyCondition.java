package com.liverton.livecheck.common.config.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by sshah on 31/08/2016.
 */
public class IsDevelopmentSystemPropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return "development".equals(conditionContext.getEnvironment().getProperty("environment")) || conditionContext.getEnvironment().getProperty("environment") == null;
    }
}