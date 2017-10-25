/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cementframework.querybyproxy.hql.impl.visitors.conditionals;

import java.util.List;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.api.model.conditionals.Conditional;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.GroupConditionalImpl;

/**
 * Groups conditional expressions.
 *
 * <p>
 * Example: "(a.id = 2 and a.name = 'foo')"
 * </p>
 *
 * @author allenparslow
 */
public class HqlGroupConditionalVisitor
        extends AbstractHqlCondVisitor
        implements QueryFragmentVisitor<GroupConditionalImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(GroupConditionalImpl group,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        List<Conditional<?>> conditionals = group.getConditionalExpressions();
        if (conditionals.size() == 0) {
            return;
        }

        resolver.append("(");
        appendBaseQueryString(group, strategy, resolver);
        resolver.append(")");
    }

    protected void appendBaseQueryString(
            GroupConditionalImpl group,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {
        boolean first = true;

        List<Conditional<?>> list = group.getConditionalExpressions();
        for (Conditional<?> conditional : list) {
            if (!first) {
                resolver.append(" ");
                resolver.append(conditional.getLogicGate().name());
                resolver.append(" ");
            }
            first = false;
            strategy.visit(conditional, resolver);
        }
    }
}
