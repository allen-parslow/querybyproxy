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
package org.cementframework.querybyproxy.hql.impl.visitors.sorts;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.api.model.sorts.QuerySortOperator;
import org.cementframework.querybyproxy.shared.impl.model.sorts.DirectionalQuerySortImpl;

/**
 * Represents a sort with an NO explicit direction (i.e. an ascending sort).
 *
 * @author allenparslow
 */
public class HqlDirectionalQuerySortVisitor
        implements QueryFragmentVisitor<DirectionalQuerySortImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(DirectionalQuerySortImpl sort,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {
        strategy.visit(sort.getSelection(), resolver);
        if (QuerySortOperator.DESC.equals(sort.getSortOperator())) {
            resolver.append(" ");
            resolver.append(sort.getSortOperator().name());
        }
    }
}
