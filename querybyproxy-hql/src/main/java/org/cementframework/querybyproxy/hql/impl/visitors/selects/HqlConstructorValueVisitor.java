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
package org.cementframework.querybyproxy.hql.impl.visitors.selects;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.impl.model.selections.ConstructorValueImpl;

/**
 * A representation of a jpql constuctor [new] syntax.
 *
 * <p>
 * Example: select new com.example.SomeDto(a.id, a.name)
 * </p>
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlConstructorValueVisitor implements QueryFragmentVisitor<ConstructorValueImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(ConstructorValueImpl constructor,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append("NEW ");
        resolver.append(constructor.getConstructorClass().getName());
        resolver.append("(");
        boolean first = true;
        for (Object obj : constructor.getArguments()) {
            QueryValue value = (QueryValue) obj;
            if (!first) {
                resolver.append(", ");
            }
            first = false;
            strategy.visit(value, resolver);
        }
        resolver.append(")");
    }
}
