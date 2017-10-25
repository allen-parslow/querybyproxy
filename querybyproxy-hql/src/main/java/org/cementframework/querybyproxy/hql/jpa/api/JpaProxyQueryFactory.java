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
package org.cementframework.querybyproxy.hql.jpa.api;

import javax.persistence.EntityManager;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;

/**
 * Creates new <code>ProxyQuery</code> and <code>ProxyQueryBuilder</code>
 * instances.
 *
 * <p>
 * Thread safe, may be stored as member variable (<b>ASSUMING</b> that the bound
 * EntityManager is thread safe (i.e. EntityManager are thread-safe when
 * injected by @PersistenceContext on an application server or where a
 * thread-safe EnitityManager-proxy has been injected, e.g. create with Spring's
 * SharedEntityManagerCreator).
 * </p>
 *
 * @see javax.persistence.EntityManager
 * @see javax.persistence.PersistenceContext
 *
 * @author allenparslow
 */
public interface JpaProxyQueryFactory extends ProxyQueryFactory {

    /**
     * Gets the entity-manager attached to the factory.
     *
     * @return the <code>EntityManager</code>.
     */
    EntityManager getEntityManager();

    /**
     * Sets the entity-manager attached to the factory.
     *
     * @param entityManager
     *            an <code>EntityManager</code>.
     */
    void setEntityManager(EntityManager entityManager);

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
