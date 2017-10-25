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
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TypedSelectTest {
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
    public void empty() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

                strategy.visit(query, resolver);
        Assert.assertEquals("FROM SimpleEntity a", resolver.getQueryString());
    }

    @Test
    public void select() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.select();

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a", resolver.getQueryString());
    }

    @Test
    public void select_distinct() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.distinct().select();

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT DISTINCT a", resolver.getQueryString());
    }

    @Test
    public void select_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(qb.get(a.getId()), qb.get(a.getName()));

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.id, a.name", resolver.getQueryString());
    }

    @Test
    public void select_2_aggregate() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(
                a.getName(), qb.max(a.getId())
                );

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals(
                "SELECT a.name, MAX(a.id)",
                resolver.getQueryString());
    }

    @Test
    public void select_nested() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.select(qb.get(a.getSimpleEntity().getName()));

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.simpleEntity.name",
                resolver.getQueryString());
    }

    @Test
    public void select_subquery_exists() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.get(a.getId())));

        Subquery<Integer> subquery = query2.subquery(qb.get(b.getId()));

        query.where(qb.exists(subquery));

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a FROM SimpleEntity a WHERE "
                + "EXISTS(SELECT b.id FROM SimpleEntity b WHERE b.id = a.id)",
                resolver.getQueryString());
    }

    @Test
    public void select_typed_nested() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.select(qb.get(a.getSimpleEntity()));

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.simpleEntity", resolver.getQueryString());
    }

}
