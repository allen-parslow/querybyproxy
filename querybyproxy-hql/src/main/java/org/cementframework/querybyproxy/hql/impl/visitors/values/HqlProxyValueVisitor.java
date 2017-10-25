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
import org.cementframework.querybyproxy.shared.impl.model.values.ProxySelectExpressionImpl;

/**
 * Represent proxy itself used as value (as opposed to the invocation of proxy's
 * property).
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlProxyValueVisitor implements QueryFragmentVisitor<ProxySelectExpressionImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(ProxySelectExpressionImpl property,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        String alias = resolver.getAliasResolver().getIdentifactionVariable(property.getProxy())
                .getName();

        resolver.append(alias);
    }
}
