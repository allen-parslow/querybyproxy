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
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.AbstractHqlCondVisitor;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.impl.model.values.ValuesListImpl;

/**
 * Represents of list of values (e.g. for an "in" expression).
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlValuesListVisitor
        extends AbstractHqlCondVisitor
        implements QueryFragmentVisitor<ValuesListImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(ValuesListImpl conditional,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append("(");
        boolean first = true;
        for (Object obj : conditional.getValues()) {
            QueryValue value = (QueryValue) obj;
            if (!first) {
                resolver.append(",");
            }
            first = false;
            strategy.visit(value, resolver);
        }
        resolver.append(")");
    }

}
