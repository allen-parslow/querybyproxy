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
import org.cementframework.querybyproxy.shared.impl.model.conditionals.LikeBinaryConditional;

/**
 * Represents a like binary expression using in where and having clauses.
 *
 * @author allenparslow
 * @param <T>
 *            type-inference.
 */
@SuppressWarnings("unchecked")
public class HqlLikeBinaryCondVisitor<T>
        extends AbstractHqlCondVisitor
        implements QueryFragmentVisitor<LikeBinaryConditional> {

    /**
     * {@inheritDoc}
     */
    public void visit(LikeBinaryConditional conditional,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        strategy.visit(conditional.getLeftValue(), resolver);
        resolver.append(getComparisonString(conditional.getOperation()));
        strategy.visit(conditional.getRightValue(), resolver);
        if (conditional.getEscape() != null) {
            resolver.append(" ");
            resolver.append("ESCAPE");
            resolver.append(" ");
            resolver.append("'");
            resolver.append(conditional.getEscape().toString());
            resolver.append("'");
        }
    }
}
