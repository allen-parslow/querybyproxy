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

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.SubqueryConditionalImpl;

/**
 * Represents a subquery used in a conditional expression.
 *
 * @author allenparslow
 */
public class HqlSubqueryCondVisitor
        extends AbstractHqlCondVisitor
        implements QueryFragmentVisitor<SubqueryConditionalImpl<?>> {

    /**
     * {@inheritDoc}
     */
    public void visit(SubqueryConditionalImpl<?> conditional,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append(getComparisonString(conditional.getOperation()));
        strategy.visit(conditional.getSubquery(), resolver);
    }

}
