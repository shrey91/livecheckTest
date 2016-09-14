package com.liverton.livecheck.dao.config.conditional;

import com.liverton.livecheck.common.config.conditional.IsProductionSystemPropertyCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by sshah on 31/08/2016.
 */
public class IsMySQLDatabaseCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Condition condition = new IsProductionSystemPropertyCondition();
        return condition.matches(conditionContext, annotatedTypeMetadata);
    }
}
