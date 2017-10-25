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

import org.cementframework.querybyproxy.hql.hibernate.impl.HibernateProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;
import org.hibernate.Session;

/**
 * Bootstrap class for creating a hibernate proxy-query-factory.
 *
 * <p>
 * The is the primary starting point for create qbp queries.
 * </p>
 *
 * @author allenparslow
 */
public class HibernateProxyQueries {

    /**
     * Creates a hibernate proxy-query-factory.
     *
     * @param session
     *            the session to associate with the factory.
     *
     * @return the new factory.
     */
    public static ProxyQueryFactory createQueryFactory(Session session) {
        return new HibernateProxyQueryFactoryImpl(session);
    }
}
