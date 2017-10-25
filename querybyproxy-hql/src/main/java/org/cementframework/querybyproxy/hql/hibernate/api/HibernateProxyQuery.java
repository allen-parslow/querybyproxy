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
package org.cementframework.querybyproxy.hql.hibernate.api;

import org.cementframework.querybyproxy.shared.api.ProxyQuery;

/**
 * The Hibernate primary entry point into creating by-proxy criteria queries.
 *
 * @author allenparslow
 * @param <T>
 *            the root type of the query (may be effectively modified by calling
 *            select(proxy)).
 */
public interface HibernateProxyQuery<T> extends ProxyQuery<T> {

    /**
     * {@inheritDoc}
     */
    HibernateProxyQueryFactory getQueryFactory();
}
