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
import org.cementframework.querybyproxy.hql.jpa.tests.model.AliasedPropertyEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionReferencedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.EmbeddedIdEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEmbeddedIdEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntityDto;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 *
 */
public class TerseProxyQueryTest {
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
    public void select() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a FROM SimpleEntity a", resolver.getQueryString());
    }

    @Test
    public void select_toString() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.select();

        resolver.appendSpaceIfRequired();
        resolver.append(" ");
        resolver.appendSpaceIfRequired();
        strategy.visit(query, resolver);
        Assert.assertEquals(" SELECT a FROM SimpleEntity a", resolver.toString());
    }

    @Test
    public void select_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.select(a.getId());

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.id", resolver.getQueryString());
    }

    @Test
    public void select_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.select(a.getId(), a.getName());

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.id, a.name", resolver.getQueryString());
    }

    @Test
    public void select_nested() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        NestedEntity a = query.getRootProxy();

        query.select(a.getSimpleEntity().getName());

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.simpleEntity.name", resolver.getQueryString());
    }

    @Test
    public void select_where_param() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(a.getName());
        query.where(qb.get(a.getStartDate()).equalTo(qb.param(new Date())));

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a.name FROM SimpleEntity a WHERE a.startDate = :p1",
                resolver.getQueryString());
    }

    @Test
    public void select_where_literal_null() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(a.getName());
        Date date = null;
        query.where(qb.get(a.getStartDate()).equalTo(qb.literal(date)));

        strategy.visit(query, resolver);
        // NOTE: this would be bad sql (should use is null)!
        Assert.assertEquals(
                "SELECT a.name FROM SimpleEntity a WHERE a.startDate = null",
                resolver.getQueryString());
    }

    @Test
    public void select_find_terse_nested_property() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        NestedEntity a = query.getRootProxy();

        query.select(a.getSimpleEntity());

        strategy.visit(query.getSelect(), resolver);
        Assert.assertEquals("SELECT a.simpleEntity", resolver.getQueryString());
    }

    @Test
    public void select_where_aliased() {
        ProxyQuery<AliasedPropertyEntity> query = queryFactory
                .createQuery(AliasedPropertyEntity.class);
        AliasedPropertyEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.select(a.getFlag());
        query.where(qb.get(a.getFlag()).equalTo(true));

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a.flagText FROM AliasedPropertyEntity a WHERE a.flagText = true",
                resolver.getQueryString());
    }

    @Test
    public void select_constructor() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.select(SimpleEntityDto.class, a.getId(), a.getName());

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT NEW org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntityDto"
                + "(a.id, a.name) FROM SimpleEntity a",
                resolver.getQueryString());
    }

    @Test
    public void terse_orderBy() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.orderBy(a.getName());

        strategy.visit(query.getOrderBy(), resolver);
        Assert.assertEquals(
                "ORDER BY a.name",
                resolver.getQueryString());
    }

    @Test
    public void terse_orderBy_desc() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.orderBy(qb.desc(a.getName()));

        strategy.visit(query.getOrderBy(), resolver);
        Assert.assertEquals(
                "ORDER BY a.name DESC",
                resolver.getQueryString());
    }


    @Test
    public void terse_groupBy_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());

        strategy.visit(query.getGroupBy(), resolver);
        Assert.assertEquals(
                "GROUP BY a.name",
                resolver.getQueryString());
    }

    @Test
    public void terse_groupBy_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName(), a.getId());

        strategy.visit(query.getGroupBy(), resolver);
        Assert.assertEquals(
                "GROUP BY a.name, a.id",
                resolver.getQueryString());
    }

    @Test
    public void terse_join_nested_NestedEmbeddedIdEntity() {

        ProxyQuery<NestedEmbeddedIdEntity> query = queryFactory.createQuery(NestedEmbeddedIdEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEmbeddedIdEntity a = query.getRootProxy();

        EmbeddedIdEntity b = query.join(qb.get(a.getEntity()));

        query.select(b);
        query.andWhere(b.getId().getIdText()).equalTo("z");

        strategy.visit(query, resolver);
        Assert.assertEquals("SELECT a "
                + "FROM NestedEmbeddedIdEntity b JOIN b.entity a "
                + "WHERE a.id.idText = 'z'",
                resolver.getQueryString());
    }

    @Test
    public void correlated_subquery() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        ProxyQuery<CollectionReferencedEntity> subquery  = queryFactory.correlatedSubquery(a.getEntities());
        CollectionReferencedEntity b = subquery.getRootProxy();

        subquery.andWhere(b.getId()).equalTo(a.getId());

        query.where(qb.exists(subquery.subquery()));

        query.select();

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "SELECT a FROM CollectionEntity a WHERE EXISTS("
                + "SELECT b FROM a.entities b WHERE b.id = a.id)",
                resolver.getQueryString());
    }

    @Test
    public void parameter_name_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> param1 = qb.param("xyz", Integer.class);

        query.select();
        query.andWhere(a.getId()).equalTo(param1);
        query.andWhere(a.getId()).equalTo(param1);

        Assert.assertEquals(
                "SELECT a FROM SimpleEntity a WHERE a.id = :xyz AND a.id = :xyz",
                query.toString());
    }

    @Test
    public void parameter_name_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> param1 = qb.param("xyz1", Integer.class);
        QueryValue<Double> param2 = qb.param("xyz2", Double.class);

        query.select();
        query.andWhere(a.getId()).equalTo(param1);
        query.andWhere(a.getValue()).equalTo(param2);

        Assert.assertEquals(
                "SELECT a FROM SimpleEntity a WHERE a.id = :xyz1 AND a.value = :xyz2",
                query.toString());
    }
}
