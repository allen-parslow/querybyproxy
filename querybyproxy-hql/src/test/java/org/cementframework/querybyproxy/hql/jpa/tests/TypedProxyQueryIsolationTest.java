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

import junit.framework.Assert;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TypedProxyQueryIsolationTest {
    protected QueryVisitorStrategy strategy;
    protected QueryCompiler resolver;
    protected JpaProxyQueryFactoryImpl queryFactory;

    @Before
    public void setup() {
        queryFactory = new JpaProxyQueryFactoryImpl();

        resolver = queryFactory.createQueryCompiler();
        strategy = queryFactory.getQueryVisitorStrategy();
    }

    // ---------------------------------------------------------


    @Test
    public void select_isolation_between_queries() {

        ProxyQuery<SimpleEntity> query1 = queryFactory.createQuery(SimpleEntity.class);
        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query1.getRootProxy();

        query1.select(qb.get(a.getId()));

        strategy.visit(query1.getSelect(), resolver);
        Assert.assertEquals("SELECT a.id", resolver.getQueryString());

        query1.select(qb.get(a.getName()));

        resolver = queryFactory.createQueryCompiler();
        strategy.visit(query1.getSelect(), resolver);
        Assert.assertEquals("SELECT a.name", resolver.getQueryString());


        query2.select(qb.get(a.getId()));

        // query1 remains the same
        resolver = queryFactory.createQueryCompiler();
        strategy.visit(query1.getSelect(), resolver);
        Assert.assertEquals("SELECT a.name", resolver.getQueryString());


        // query2 is updated
        resolver = queryFactory.createQueryCompiler();
        strategy.visit(query2.getSelect(), resolver);
        Assert.assertEquals("SELECT a.id", resolver.getQueryString());
    }

}