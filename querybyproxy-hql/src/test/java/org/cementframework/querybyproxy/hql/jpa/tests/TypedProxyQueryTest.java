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

import java.util.Date;

import junit.framework.Assert;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TypedProxyQueryTest {
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
    public void query_select_empty() {
        JpaProxyQueryFactoryImpl qb = new JpaProxyQueryFactoryImpl();

        ProxyQuery<SimpleEntity> query = qb.createQuery(SimpleEntity.class);

        strategy.visit(query, resolver);
        Assert.assertEquals("FROM SimpleEntity a", resolver.getQueryString());
    }

    @Test
    public void query_select_root() {
        JpaProxyQueryFactoryImpl qb = new JpaProxyQueryFactoryImpl();

        ProxyQuery<SimpleEntity> query = qb.createQuery(SimpleEntity.class);

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a FROM SimpleEntity a", resolver.getQueryString());
    }

    @Test
    public void query_select_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(qb.get(a.getName()));

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a.name FROM SimpleEntity a", resolver.getQueryString());
    }

    @Test
    public void query_select_where() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(qb.get(a.getName()));
        query.where(qb.get(a.getStartDate()).equalTo(qb.param(new Date())));

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a.name FROM SimpleEntity a WHERE a.startDate = :p1",
                resolver.getQueryString());
    }

    @Test
    public void select_nested_property() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        QueryValue<SimpleEntity> select = qb.get(a.getSimpleEntity());

        query.select(select);

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a.simpleEntity FROM NestedEntity a",
                resolver.getQueryString());
    }

    @Test
    public void query_collection_not_joined() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a FROM CollectionEntity a", resolver.getQueryString());
    }

    @Test
    public void query_collection_join_only() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        CollectionEntity a = query.getRootProxy();

        query.join(a.getEntities());

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a FROM CollectionEntity a JOIN a.entities b",
                resolver.getQueryString());
    }

    @Test
    public void query_collection_join_select_where() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();
        query.join(a.getEntities());

        query.select(qb.get(a.getId()));
        query.where(qb.get(a.getId()).equalTo(2));

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a.id FROM CollectionEntity a JOIN a.entities b WHERE a.id = 2",
                resolver.getQueryString());
    }

    @Test
    public void query_join_cartesian() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        SimpleEntity b = query.thetaJoin(SimpleEntity.class);

        query.where(qb.get(a.getId()).equalTo(qb.get(b.getId())));

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals(
               "SELECT a FROM SimpleEntity a, SimpleEntity b WHERE a.id = b.id",
               resolver.getQueryString());
    }

    @Test
    public void query_join_nested() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        SimpleEntity b = query.join(qb.get(a.getSimpleEntity()));

        query.where(
            qb.get(b.getId()).equalTo(1)
        );

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals(
            "SELECT a FROM NestedEntity a JOIN a.simpleEntity b WHERE b.id = 1",
            resolver.getQueryString());
    }

    @Test
    public void query_subquery() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        Subquery<Integer> subquery = query2.subquery(qb.get(b.getId()));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a FROM SimpleEntity a WHERE a.id = "
                + "(SELECT b.id FROM SimpleEntity b WHERE b.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void typed_orderBy_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.orderBy(qb.get(a.getName()));

        strategy.visit(query.getOrderBy(), resolver);
        Assert.assertEquals(
                "ORDER BY a.name",
                resolver.getQueryString());
    }

    @Test
    public void typed_orderBy_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.orderBy(qb.get(a.getName()), qb.get(a.getId()));

        strategy.visit(query.getOrderBy(), resolver);
        Assert.assertEquals(
                "ORDER BY a.name, a.id",
                resolver.getQueryString());
    }

    @Test
    public void typed_orderBy_desc() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.orderBy(qb.get(a.getName()), qb.desc(qb.get(a.getId())));

        strategy.visit(query.getOrderBy(), resolver);
        Assert.assertEquals(
                "ORDER BY a.name, a.id DESC",
                resolver.getQueryString());
    }


    @Test
    public void typed_groupBy_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(qb.get(a.getName()));

        strategy.visit(query.getGroupBy(), resolver);
        Assert.assertEquals(
                "GROUP BY a.name",
                resolver.getQueryString());
    }

    @Test
    public void typed_select_groupBy() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(qb.get(a.getName()));

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a FROM SimpleEntity a GROUP BY a.name",
                resolver.getQueryString());
    }

    @Test
    public void typed_groupBy_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(
                qb.get(a.getName()),
                qb.get(a.getId())
                );

        strategy.visit(query.getGroupBy(), resolver);
        Assert.assertEquals(
                "GROUP BY a.name, a.id",
                resolver.getQueryString());
    }


}
