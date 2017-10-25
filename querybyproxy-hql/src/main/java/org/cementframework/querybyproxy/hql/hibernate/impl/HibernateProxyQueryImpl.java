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
package org.cementframework.querybyproxy.hql.hibernate.impl;

import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.hibernate.api.HibernateProxyQuery;
import org.cementframework.querybyproxy.hql.hibernate.api.HibernateProxyQueryFactory;
import org.cementframework.querybyproxy.shared.api.DynamicQuery;
import org.cementframework.querybyproxy.shared.api.TypedQuery;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoin;
import org.cementframework.querybyproxy.shared.impl.AbstractProxyQueryImpl;

/**
 * The primary entry point into creating by-proxy criteria queries.
 *
 * @author allenparslow
 * @param <T>
 *            the root type of the query (may be effectively modified by calling
 *            select(proxy)).
 */
public class HibernateProxyQueryImpl<T> extends AbstractProxyQueryImpl<T>
        implements HibernateProxyQuery<T> {
    private static final long serialVersionUID = 4553631614884233561L;

    private final HibernateProxyQueryFactory queryFactory;

    /**
     * Create a new instance.
     *
     * @param proxy
     *            the target proxy.
     * @param rootProxyClass
     *            the target proxy's actual class.
     * @param queryFactory
     *            the instantiating query factory.
     */
    public HibernateProxyQueryImpl(
            T proxy,
            Class<?> rootProxyClass,
            HibernateProxyQueryFactory queryFactory) {
        super(proxy, rootProxyClass);
        this.queryFactory = queryFactory;
    }

    /**
     * Create a new instance.
     *
     * @param proxy
     *            the target proxy.
     * @param join
     *            the root query join for the correlated subquery.
     * @param queryFactory
     *            the instantiating query factory.
     */
    public HibernateProxyQueryImpl(
            T proxy,
            QueryJoin join,
            HibernateProxyQueryFactory queryFactory) {
        super(proxy, join);
        this.queryFactory = queryFactory;
    }

    /**
     * {@inheritDoc}
     */
    public HibernateProxyQueryFactory getQueryFactory() {
        return queryFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DynamicQuery createDynamicQuery() {
        return new HibernateDynamicQueryImpl(this);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected TypedQuery createTypedQuery() {
        return new HibernateTypedQueryImpl<T>(this);
    }

    /**
     * Renders the query into HQL.
     *
     * @return a string containing HQL.
     */
    @Override
    public String toString() {
        QueryCompiler resolver = getQueryFactory().createQueryCompiler();
        QueryVisitorStrategy strategy = getQueryFactory().getQueryVisitorStrategy();
        strategy.visit(this, resolver);
        return resolver.getQueryString();
    }
}
