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
package org.cementframework.querybyproxy.hql.impl.visitors.joins;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.impl.model.joins.RootJoinImpl;

/**
 * Represents a proxy join for a from-clause.
 *
 * @author allenparslow
 */
public class HqlRootJoinVisitor implements QueryFragmentVisitor<RootJoinImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(RootJoinImpl join,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append(join.getProxyClass().getSimpleName());
        resolver.append(" ");

        // need for identification variable
        resolver.append(resolver.getAliasResolver().getIdentifactionVariable(
                join.getProxy()).getName());
    }
}
