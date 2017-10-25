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
package org.cementframework.querybyproxy.hql.impl.visitors;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.shared.api.model.sorts.QuerySort;
import org.cementframework.querybyproxy.shared.impl.model.OrderByClauseImpl;

/**
 * Represents a "order by" clause.
 *
 * @author allenparslow
 */
public class HqlOrderByClauseVisitor implements QueryFragmentVisitor<OrderByClauseImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(OrderByClauseImpl orderBy,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {
        if (orderBy.getSorts().size() == 0) {
            return;
        }

        resolver.appendSpaceIfRequired();
        resolver.append("ORDER BY ");

        boolean first = true;
        for (QuerySort sort : orderBy.getSorts()) {
            if (!first) {
                resolver.append(", ");
            }
            first = false;
            strategy.visit(sort, resolver);
        }
    }
}
