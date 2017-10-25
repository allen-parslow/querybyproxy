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

import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;

/**
 * Bootstrap class for creating a JPA proxy-query-factory.
 *
 * <p>
 * The is the primary starting point for create qbp queries.
 * </p>
 *
 * @author allenparslow
 */
public class JpaProxyQueries {

    /**
     * Creates a JPA proxy-query-factory.
     *
     * @param entityManager
     *            the entity manager to associate with the factory.
     *
     * @return the new factory.
     */
    public static ProxyQueryFactory createQueryFactory(EntityManager entityManager) {
        return new JpaProxyQueryFactoryImpl(entityManager);
    }
}
