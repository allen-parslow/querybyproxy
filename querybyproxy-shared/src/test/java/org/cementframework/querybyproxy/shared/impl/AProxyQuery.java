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
package org.cementframework.querybyproxy.shared.impl;

import org.cementframework.querybyproxy.shared.api.DynamicQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;
import org.cementframework.querybyproxy.shared.api.TypedQuery;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoin;

public class AProxyQuery<T> extends AbstractProxyQueryImpl<T> {
    private static final long serialVersionUID = -6539189283718427991L;

    public AProxyQuery(T proxy, Class<T> rootProxyClass) {
        super(proxy, rootProxyClass);
    }

    public AProxyQuery(T proxy, QueryJoin join) {
        super(proxy, join);
    }

    @Override
    public DynamicQuery createDynamicQuery() {
        return new ADynamicQuery();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TypedQuery createTypedQuery() {
        return new ATypedQuery<T>();
    }

    /**
     * {@inheritDoc}
     */
    public ProxyQueryFactory getQueryFactory() {
        return null;
    }
}