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

import org.cementframework.querybyproxy.hql.api.QueryCompilerFactory;
import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.hibernate.api.HibernateProxyQuery;
import org.cementframework.querybyproxy.hql.hibernate.api.HibernateProxyQueryFactory;
import org.cementframework.querybyproxy.hql.impl.compiler.BasicQueryCompilerFactory;
import org.cementframework.querybyproxy.hql.impl.visitors.HqlQueryVisitorStrategyImpl;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoin;
import org.cementframework.querybyproxy.shared.impl.AbstractProxyQueryFactory;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.hibernate.Session;

/**
 * Creates new <code>ProxyQuery</code> and <code>ProxyQuerySession</code>
 * instances.
 *
 * @author allenparslow
 */
public class HibernateProxyQueryFactoryImpl extends AbstractProxyQueryFactory implements
        HibernateProxyQueryFactory {

    private Session session;
    private QueryCompilerFactory queryCompilerFactory = new BasicQueryCompilerFactory();

    /**
     * Create a new query-factory instance.
     */
    public HibernateProxyQueryFactoryImpl() {
    }

    /**
     * Create a new <code>HibProxyQueryFactoryImpl</code> instance.
     *
     * @param session
     *            the session to attach the factory.
     */
    public HibernateProxyQueryFactoryImpl(Session session) {
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    public <T> HibernateProxyQuery<T> createQuery(Class<T> entityClass) {
        return new HibernateProxyQueryImpl<T>(
                MethodCallUtils.proxy(entityClass), entityClass, this);
    }

    /**
     * {@inheritDoc}
     */
    public QueryCompiler createQueryCompiler() {
        return queryCompilerFactory.createQueryComplier();
    }

    /**
     * Sets the factory that will produce query-compilers that will be used to
     * transform query-by-proxy criteria models into jpql.
     *
     * @param queryResolverFactory
     *            the query-resolver factory.
     */
    public void setQueryCompilerFactory(QueryCompilerFactory queryResolverFactory) {
        this.queryCompilerFactory = queryResolverFactory;
    }

    /**
     * {@inheritDoc}
     */
    public Session getSession() {
        return session;
    }

    /**
     * {@inheritDoc}
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    public QueryVisitorStrategy getQueryVisitorStrategy() {
        HqlQueryVisitorStrategyImpl strategy = new HqlQueryVisitorStrategyImpl();
        strategy.add(HibernateProxyQueryImpl.class, new HibernateProxyQueryVisitor());
        return strategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> ProxyQuery<T> createCorrelated(
            T proxy,
            QueryJoin join) {
        return new HibernateProxyQueryImpl<T>(proxy, join, this);
    }
}
