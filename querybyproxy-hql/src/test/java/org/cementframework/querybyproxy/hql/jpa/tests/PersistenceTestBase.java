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
package org.cementframework.querybyproxy.hql.jpa.tests;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import junit.framework.AssertionFailedError;

import org.cementframework.querybyproxy.hql.jpa.api.JpaProxyQueries;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * @author allenparslow
 */
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@ContextConfiguration(locations = { "/spring/test-context.xml" })
public class PersistenceTestBase {

    protected final ProxyQueryFactory queryFactory;

    @PersistenceUnit(unitName = "testingPU")
    protected EntityManagerFactory emf;

    protected EntityManager entityManager;

    public PersistenceTestBase() {
        try {
            TestContextManager testContextManager = new TestContextManager(getClass());
            testContextManager.prepareTestInstance(this);

            entityManager = emf.createEntityManager();

            ProxyQueryFactory queryFactoryImpl
                    = JpaProxyQueries.createQueryFactory(entityManager);
            this.queryFactory = queryFactoryImpl;
            entityManager.clear();

        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionFailedError(e.getMessage());
        }

    }
}
