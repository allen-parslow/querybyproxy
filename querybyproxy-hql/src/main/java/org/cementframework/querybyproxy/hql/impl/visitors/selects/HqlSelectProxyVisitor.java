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
import org.cementframework.querybyproxy.shared.impl.model.selections.SelectProxyImpl;

/**
 * A selection of a proxy/entity.
 *
 * <p>
 * Example: "select a"
 * </p>
 *
 * @author allenparslow
 */
public class HqlSelectProxyVisitor implements QueryFragmentVisitor<SelectProxyImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(SelectProxyImpl selection,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        String alias = resolver.getAliasResolver().getIdentifactionVariable(selection.getProxy())
                .getName();
        resolver.append(alias);
    }
}
