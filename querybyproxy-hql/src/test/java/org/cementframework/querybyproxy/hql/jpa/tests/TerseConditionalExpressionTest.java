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

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.conditionals.Conditional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TerseConditionalExpressionTest {
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

    @SuppressWarnings("unchecked")
    @Test
    public void conditional_equals() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional conditional = qb.get(a.getId()).equalTo(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id = 3", resolver.getQueryString());
    }

    @Test
    public void conditional_between() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).between(3, 4);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id BETWEEN 3 AND 4", resolver.getQueryString());
    }

    @Test
    public void conditional_not_between() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).notBetween(3, 4);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id NOT BETWEEN 3 AND 4", resolver.getQueryString());
    }

    @Test
    public void conditional_like() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).like(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id LIKE 3", resolver.getQueryString());
    }

    @Test
    public void conditional_like_escape() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).like(3, 'a');

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id LIKE 3 ESCAPE 'a'", resolver.getQueryString());
    }

    @Test
    public void conditional_like_escape_null() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).like(3, null);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id LIKE 3", resolver.getQueryString());
    }

    @Test
    public void conditional_not_like() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).notLike(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id NOT LIKE 3", resolver.getQueryString());
    }

    @Test
    public void conditional_equals_nested() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getSimpleEntity().getId()).equalTo(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.simpleEntity.id = 3", resolver.getQueryString());
    }

    @Test
    public void conditional_notEqualTo() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).notEqualTo(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id != 3", resolver.getQueryString());
    }

    @Test
    public void conditional_greaterThan() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).greaterThan(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id > 3", resolver.getQueryString());
    }

    @Test
    public void conditional_greaterThanOrEqualTo() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).greaterThanOrEqualTo(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id >= 3", resolver.getQueryString());
    }

    @Test
    public void conditional_lessThan() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).lessThan(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id < 3", resolver.getQueryString());
    }

    @Test
    public void conditional_lessThanOrEqualTo() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).lessThanOrEqualTo(3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id <= 3", resolver.getQueryString());
    }

    @Test
    public void conditional_isNull() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).isNull();

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id IS NULL", resolver.getQueryString());
    }

    @Test
    public void conditional_isNotNull() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).isNotNull();

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id IS NOT NULL", resolver.getQueryString());

    }

    @Test
    public void conditional_in_string() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<String> conditional = qb.get(a.getName()).in("1", "2", "3");

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.name IN ('1','2','3')", resolver.getQueryString());
    }

    @Test
    public void conditional_in() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).in(1, 2, 3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id IN (1,2,3)", resolver.getQueryString());
    }

    @Test
    public void conditional_not_in() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> conditional = qb.get(a.getId()).notIn(1, 2, 3);

        strategy.visit(conditional, resolver);
        Assert.assertEquals("a.id NOT IN (1,2,3)", resolver.getQueryString());
    }
}