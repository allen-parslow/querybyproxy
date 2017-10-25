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
package org.cementframework.querybyproxy.hql.impl.visitors.values;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryDecoratorImpl;

/**
 * Represents a subquery.
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlSubQueryModifierDecVisitor
        implements QueryFragmentVisitor<SubqueryDecoratorImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(SubqueryDecoratorImpl subquery,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append(subquery.getSubqueryOperator().name());
        resolver.append("(");
        strategy.visit(subquery.getSelect(), resolver);
        strategy.visit(subquery.getFrom(), resolver);
        strategy.visit(subquery.getWhere(), resolver);
        strategy.visit(subquery.getGroupBy(), resolver);
        strategy.visit(subquery.getHaving(), resolver);
        strategy.visit(subquery.getOrderBy(), resolver);
        resolver.append(")");
    }

}
