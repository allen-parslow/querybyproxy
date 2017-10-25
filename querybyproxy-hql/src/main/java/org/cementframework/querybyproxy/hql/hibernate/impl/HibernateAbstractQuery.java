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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cementframework.querybyproxy.hql.api.ParameterBinding;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.hibernate.api.HibernateProxyQuery;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Abstract representation a query-interface that is able to produce a query
 * result (either single or list).
 *
 * @author allenparslow
 *
 * @param <T>
 *            type-inference
 */
public abstract class HibernateAbstractQuery<T> {

    private Log log = LogFactory.getLog(getClass());

    private final HibernateProxyQuery<T> query;
    private Integer limit;
    private Integer firstResult;

    protected HibernateAbstractQuery(HibernateProxyQuery<T> query) {
        this.query = query;
    }

    protected Query build() {
        Session session = query.getQueryFactory().getSession();

        QueryCompiler resolver = query.getQueryFactory().createQueryCompiler();

        QueryVisitorStrategy strategy = query.getQueryFactory().getQueryVisitorStrategy();

        strategy.visit(query, resolver);

        String hql = resolver.getQueryString();
        log.info(hql);
        Query hibQuery = session.createQuery(hql);

        for (ParameterBinding binding : resolver.getParameterResolver().getBindings()) {
            hibQuery.setParameter(binding.getParameterName(), binding.getValue());
        }

        if (limit != null) {
            hibQuery.setMaxResults(limit);
        }
        if (firstResult != null) {
            hibQuery.setFirstResult(firstResult);
        }

        return hibQuery;
    }

    protected void setLimit(int rowLimit) {
        limit = rowLimit;
    }

    protected void setFirstResult(int startPosition) {
        firstResult = startPosition;
    }
}
