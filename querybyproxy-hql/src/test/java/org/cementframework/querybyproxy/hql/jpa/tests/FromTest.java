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
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedCollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedNestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class FromTest {
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
    public void from_basic() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM SimpleEntity a", resolver.getQueryString());
    }

    @Test
    public void from_collection_not_joined() {
        ProxyQuery<CollectionEntity> query = queryFactory
                .createQuery(CollectionEntity.class);

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM CollectionEntity a", resolver.getQueryString());
    }

    @Test
    public void from_collection_join() {
        ProxyQuery<CollectionEntity> query = queryFactory
                .createQuery(CollectionEntity.class);
        CollectionEntity a = query.getRootProxy();

        query.join(a.getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals(
                "FROM CollectionEntity a JOIN a.entities b",
                resolver.getQueryString());
    }

    @Test
    public void from_NestedCollectionEntity_join() {
        ProxyQuery<NestedCollectionEntity> query = queryFactory
                .createQuery(NestedCollectionEntity.class);
        NestedCollectionEntity a = query.getRootProxy();

        query.join(a.getEntity().getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals(
                "FROM NestedCollectionEntity a JOIN a.entity.entities b",
                resolver.getQueryString());
    }

    @Test
    public void from_NestedCollectionEntity_join_2() {
        ProxyQuery<NestedCollectionEntity> query = queryFactory
                .createQuery(NestedCollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedCollectionEntity a = query.getRootProxy();

        CollectionEntity joined = query.join(qb.get(a.getEntity()));
        query.join(joined.getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals(
                "FROM NestedCollectionEntity a "
                + "JOIN a.entity b "
                + "JOIN b.entities c",
                resolver.getQueryString());
    }

    @Test
    public void from_collection_left_join() {
        ProxyQuery<CollectionEntity> query = queryFactory
                .createQuery(CollectionEntity.class);
        CollectionEntity a = query.getRootProxy();

        query.leftJoin(a.getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals(
                "FROM CollectionEntity a LEFT JOIN a.entities b",
                resolver.getQueryString());
    }

    @Test
    public void from_collection_right_join() {
        ProxyQuery<CollectionEntity> query = queryFactory
                .createQuery(CollectionEntity.class);
        CollectionEntity a = query.getRootProxy();

        query.rightJoin(a.getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals(
                "FROM CollectionEntity a RIGHT JOIN a.entities b",
                resolver.getQueryString());
    }

    @Test
    public void from_collection_fetch_join() {
        ProxyQuery<CollectionEntity> query = queryFactory
                .createQuery(CollectionEntity.class);
        CollectionEntity a = query.getRootProxy();

        query.joinFetch(a.getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals(
                "FROM CollectionEntity a JOIN FETCH a.entities b",
                resolver.getQueryString());
    }

    @Test
    public void from_join_cartesian() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.thetaJoin(SimpleEntity.class);

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM SimpleEntity a, SimpleEntity b",
                resolver.getQueryString());
    }

    @Test
    public void from_join_nested() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.join(qb.get(a.getSimpleEntity()));

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedEntity a JOIN a.simpleEntity b",
                resolver.getQueryString());
    }

    @Test
    public void from_join_nested_NestedNestedEntity_1() {

        ProxyQuery<NestedNestedEntity> query = queryFactory.createQuery(NestedNestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedNestedEntity a = query.getRootProxy();

        NestedEntity nested = query.join(qb.get(a.getNestedItem()));
        query.join(qb.get(nested.getSimpleEntity()));

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedNestedEntity a "
                + "JOIN a.nestedItem b "
                + "JOIN b.simpleEntity c",
                resolver.getQueryString());
    }

    @Test
    public void from_join_nested_NestedNestedEntity_2() {

        ProxyQuery<NestedNestedEntity> query = queryFactory.createQuery(NestedNestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedNestedEntity a = query.getRootProxy();

        query.join(qb.get(a.getNestedItem()));
        query.join(qb.get(a.getNestedItem().getSimpleEntity()));

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedNestedEntity a "
                + "JOIN a.nestedItem b "
                + "JOIN a.nestedItem.simpleEntity c",
                resolver.getQueryString());
    }


    @Test
    public void from_join_nested_NestedNestedEntity_collection() {

        ProxyQuery<NestedNestedEntity> query = queryFactory.createQuery(NestedNestedEntity.class);
        NestedNestedEntity a = query.getRootProxy();

        query.join(a.getNestedListItem().getEntity().getEntities());

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedNestedEntity a "
        		+ "JOIN a.nestedListItem.entity.entities b",
                resolver.getQueryString());
    }

    @Test
    public void from_left_join_nested() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.leftJoin(qb.get(a.getSimpleEntity()));

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedEntity a LEFT JOIN a.simpleEntity b",
                resolver.getQueryString());
    }

    @Test
    public void from_right_join_nested() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.rightJoin(qb.get(a.getSimpleEntity()));

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedEntity a RIGHT JOIN a.simpleEntity b",
                resolver.getQueryString());
    }

    @Test
    public void from_fetch_join_nested() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.joinFetch(qb.get(a.getSimpleEntity()));

        strategy.visit(query.getFrom(), resolver);
        Assert.assertEquals("FROM NestedEntity a JOIN FETCH a.simpleEntity b",
                resolver.getQueryString());
    }
}