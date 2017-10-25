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

import java.util.HashMap;
import java.util.Map;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoinModifier;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoinType;
import org.cementframework.querybyproxy.shared.impl.model.joins.PathJoinImpl;
import org.cementframework.recordingproxy.impl.MethodCallUtils;

/**
 * Represents a proxy join for a from-clause.
 *
 * @author allenparslow
 */
public class HqlPathJoinVisitor implements QueryFragmentVisitor<PathJoinImpl> {

    private Map<QueryJoinType, String> joinTypeStrings;

    /**
     * Create a new instance.
     */
    public HqlPathJoinVisitor() {
        joinTypeStrings = new HashMap<QueryJoinType, String>();
        joinTypeStrings.put(QueryJoinType.ROOT, "");
        joinTypeStrings.put(QueryJoinType.INNER, " JOIN ");
        joinTypeStrings.put(QueryJoinType.LEFT, " LEFT JOIN ");
        joinTypeStrings.put(QueryJoinType.RIGHT, " RIGHT JOIN ");
    }

    /**
     * {@inheritDoc}
     */
    public void visit(PathJoinImpl join,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        resolver.append(joinTypeStrings.get(join.getJoinType()));

        if (QueryJoinModifier.FETCH.equals(join.getJoinModifier())) {
            resolver.append(join.getJoinModifier().name());
            resolver.append(" ");
        }

        Object root = MethodCallUtils.findRoot(join.getCall());

        String rootId = resolver.getAliasResolver().getIdentifactionVariable(
                root).getName();
        resolver.append(rootId);
        resolver.append(".");
        resolver.append(MethodCallUtils.path(join.getCall()));

        resolver.append(" ");

        String proxyId = resolver.getAliasResolver().getIdentifactionVariable(
                join.getProxy()).getName();
        resolver.append(proxyId);
    }
}
