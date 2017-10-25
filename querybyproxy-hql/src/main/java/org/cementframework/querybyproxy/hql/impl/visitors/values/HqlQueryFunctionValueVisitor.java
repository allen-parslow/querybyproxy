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

import java.util.List;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.api.model.QueryFragment;
import org.cementframework.querybyproxy.shared.api.model.QueryStaticTextOption;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryFunctionValueImpl;

/**
 * A visitor the rends arithmetic expressions.
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlQueryFunctionValueVisitor implements QueryFragmentVisitor<QueryFunctionValueImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(QueryFunctionValueImpl value,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append(value.getFunction().name());
        if (value.getArguments().size() > 0) {
            resolver.append("(");
            boolean first = true;
            List<QueryFragment> values = value.getArguments();
            for (QueryFragment fragment : values) {
                if (!first && value.isCommaSeparated()) {
                    resolver.append(", ");
                }
                first = false;
                if (fragment instanceof QueryStaticTextOption) {
                    QueryStaticTextOption text = (QueryStaticTextOption) fragment;
                    resolver.append(text.name());
                    resolver.append(" ");
                } else {
                    strategy.visit(fragment, resolver);
                }
            }
            resolver.append(")");
        }
    }
}
