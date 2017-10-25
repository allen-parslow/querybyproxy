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
package org.cementframework.querybyproxy.hql.jpa.impl;

import java.util.List;

import org.cementframework.querybyproxy.hql.jpa.api.JpaProxyQuery;
import org.cementframework.querybyproxy.shared.api.TypedQuery;

/**
 * A typed qbp (query-by-proxy) query.
 *
 * @author allenparslow
 * @param <T>
 *            the object-type for rows returned by the query.
 */
public class JpaTypedQueryImpl<T> extends JpaAbstractQuery<T> implements TypedQuery<T> {

    /**
     * Create a new <code>TypedQueryImpl</code> instance.
     *
     * @param query
     *            the target proxy-query.
     */
    public JpaTypedQueryImpl(JpaProxyQuery<T> query) {
        super(query);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> find() {
        List<T> results = build().getResultList();

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T findSingleResult() {

        T result = (T) build().getSingleResult();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public TypedQuery<T> limit(int rowLimit) {
        setLimit(rowLimit);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public TypedQuery<T> first(int startPosition) {
        setFirstResult(startPosition);
        return this;
    }
}
