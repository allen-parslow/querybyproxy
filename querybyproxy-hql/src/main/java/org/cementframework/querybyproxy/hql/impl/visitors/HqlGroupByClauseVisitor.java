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
import org.cementframework.querybyproxy.shared.api.model.selections.Selection;
import org.cementframework.querybyproxy.shared.impl.model.GroupByClauseImpl;

/**
 * Represents a "group by" clause.
 *
 * @author allenparslow
 */
public class HqlGroupByClauseVisitor implements QueryFragmentVisitor<GroupByClauseImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(GroupByClauseImpl groupBy,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {
        if (groupBy.getSelections().size() == 0) {
            return;
        }
        resolver.appendSpaceIfRequired();

        resolver.append("GROUP BY ");

        boolean first = true;
        for (Selection selection : groupBy.getSelections()) {
            if (!first) {
                resolver.append(", ");
            }
            first = false;
            strategy.visit(selection, resolver);
        }
    }
}
