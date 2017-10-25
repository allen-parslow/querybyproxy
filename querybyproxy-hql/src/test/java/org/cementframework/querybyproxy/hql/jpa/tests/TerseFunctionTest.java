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
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TerseFunctionTest {
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
    public void count() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Long> sel = qb.count(a.getId());

        strategy.visit(sel, resolver);
        Assert.assertEquals("COUNT(a.id)", resolver.getQueryString());
    }


    @Test
    public void count_null() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        QueryValue<Long> sel = qb.count(null);


        strategy.visit(sel, resolver);
        Assert.assertEquals("COUNT(1)", resolver.getQueryString());
    }

    @Test
    public void min() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.min(a.getId());

        strategy.visit(sel, resolver);
        Assert.assertEquals("MIN(a.id)", resolver.getQueryString());
    }

    @Test
    public void max() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.max(a.getId());

        strategy.visit(sel, resolver);
        Assert.assertEquals("MAX(a.id)", resolver.getQueryString());
    }

    @Test
    public void sum_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Double> sel = qb.sum(a.getValue());

        strategy.visit(sel, resolver);
        Assert.assertEquals("SUM(a.value)", resolver.getQueryString());
    }

    @Test
    public void sum_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Long> sel = qb.sum(a.getId());

        strategy.visit(sel, resolver);
        Assert.assertEquals("SUM(a.id)", resolver.getQueryString());
    }

    @Test
    public void avg() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Double> sel = qb.avg(a.getId());

        strategy.visit(sel, resolver);
        Assert.assertEquals("AVG(a.id)", resolver.getQueryString());
    }

    @Test
    public void multiply() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.multiply(a.getId(), 2);

        strategy.visit(sel, resolver);
        Assert.assertEquals("a.id * 2", resolver.getQueryString());
    }

    @Test
    public void divide() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.divide(a.getId(), 2);

        strategy.visit(sel, resolver);
        Assert.assertEquals("a.id / 2", resolver.getQueryString());
    }

    @Test
    public void add() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.add(a.getId(), 2);

        strategy.visit(sel, resolver);
        Assert.assertEquals("a.id + 2", resolver.getQueryString());
    }

    @Test
    public void subtract() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.subtract(a.getId(), 2);

        strategy.visit(sel, resolver);
        Assert.assertEquals("a.id - 2", resolver.getQueryString());
    }
}

