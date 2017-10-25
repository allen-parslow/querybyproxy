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
import org.cementframework.querybyproxy.shared.impl.model.joins.ThetaJoinImpl;

/**
 * Represents a theta (non-relationship) for a from-clause.
 *
 * <p>
 * Example: "from Entity a1, Entity a2 where a1.id = a2.id"
 * </p>
 *
 * @author allenparslow
 */
public class HqlThetaJoinVisitor implements QueryFragmentVisitor<ThetaJoinImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(ThetaJoinImpl join,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append(", ");

        resolver.append(join.getJoinClass().getSimpleName());

        resolver.append(" ");

        String id = resolver.getAliasResolver().getIdentifactionVariable(
                join.getJoinProxy()).getName();
        resolver.append(id);
    }

}
