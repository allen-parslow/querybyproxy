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
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;

/**
 * Represents a query literal (non-parameter) value.
 *
 * <p>
 * SQL Injection Warning: Do NOT use with end-user specified text as it is
 * treated as a literal string during query compilation
 * </p>
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlQueryLiteralVisitor implements QueryFragmentVisitor<QueryLiteral> {

    /**
     * {@inheritDoc}
     */
    public void visit(QueryLiteral literal,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        if (literal.getTarget() != null) {
            if (literal.getTarget() instanceof String
                    || literal.getTarget() instanceof Character) {
                resolver.append("'");
                resolver.append(literal.getTarget().toString());
                resolver.append("'");
            } else {
                resolver.append(literal.getTarget().toString());
            }
        } else {
            resolver.append("null");
        }
    }
}
