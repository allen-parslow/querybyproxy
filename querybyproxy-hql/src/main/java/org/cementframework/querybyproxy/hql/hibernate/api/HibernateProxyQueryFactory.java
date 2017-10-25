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

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;
import org.hibernate.Session;

/**
 * Creates new <code>ProxyQuery</code> and <code>ProxyQueryBuilder</code>
 * instances.
 *
 * @author allenparslow
 */
public interface HibernateProxyQueryFactory extends ProxyQueryFactory {

    /**
     * Gets the session associated with the query-factory.
     *
     * @return the current session.
     */
    Session getSession();

    /**
     * Sets the session to associate with the query-factory.
     *
     * @param session
     *            the current session.
     */
    void setSession(Session session);

    /**
     * Creates a new query-compiler to be used in the rendering of jpql.
     *
     * @return a new query-compiler.
     */
    QueryCompiler createQueryCompiler();

    /**
     * Gets the HQL query-fragment visitor strategy for mapping query-fragments
     * to HQL.
     *
     * @return the HQL query-fragment visitor strategy.
     */
    QueryVisitorStrategy getQueryVisitorStrategy();
}
