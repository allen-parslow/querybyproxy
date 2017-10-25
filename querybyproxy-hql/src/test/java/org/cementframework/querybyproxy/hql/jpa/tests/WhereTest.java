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

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionReferencedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.EmbeddedIdEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEmbeddedIdEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedNestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.conditionals.Conditional;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class WhereTest {
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
    public void toString_empty() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("", resolver.getQueryString());
    }

    @Test
    public void where_0() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("", resolver.getQueryString());
    }

    @Test
    public void where_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Integer> p = qb.get(a.getId()).equalTo(3);

        query.where(p);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = 3", resolver.getQueryString());
    }

    @Test
    public void where_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1),
                qb.get(a.getName()).equalTo("foo")
                );

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id = 1 AND a.name = 'foo'",
                resolver.getQueryString());
    }

    @Test
    public void where_group() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<?> g1 = qb.group(qb.get(a.getId()).equalTo(3));

        query.where(g1);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE (a.id = 3)", resolver.getQueryString());
    }


    @Test
    public void where_or_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1),
                qb.or(),
                qb.get(a.getName()).equalTo("foo"));


        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = 1 OR a.name = 'foo'", resolver.getQueryString());

    }


    @SuppressWarnings("unchecked")
    @Test
    public void where_or_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional conditional1 = qb.get(a.getId()).equalTo(1);

        qb.or();
        Conditional conditional2 = qb.get(a.getName()).equalTo("foo");

        query.where(conditional1,
                conditional2);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = 1 OR a.name = 'foo'", resolver.getQueryString());
    }

    @Test
    public void where_inline_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(1);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = 1", resolver.getQueryString());
    }

    @Test
    public void where_inline_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(1);
        query.andWhere(a.getName()).equalTo("foo");

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = 1 AND a.name = 'foo'", resolver.getQueryString());
    }

    @Test
    public void where_or_inline_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(1);
        query.orWhere(a.getName()).equalTo("foo");

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = 1 OR a.name = 'foo'", resolver.getQueryString());
    }

    @Test
    public void where_literal() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();


        query.where(qb.get(a.getId()).equalTo(qb.param(1)));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = :p1", resolver.getQueryString());
    }

    @Test
    public void where_literal_equals() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.where(qb.param(1).equalTo(1));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE :p1 = 1", resolver.getQueryString());
    }

    @Test
    public void where_literal_equals_literal() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.where(qb.param(1).equalTo(qb.param(1)));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE :p1 = :p2", resolver.getQueryString());
    }

    @Test
    public void where_constant_equals_constant() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.where(qb.literal(1).equalTo(qb.literal(1)));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE 1 = 1", resolver.getQueryString());
    }

    @Test
    public void where_self() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> id = qb.get(a.getId());

        query.where(id.equalTo(id));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = a.id", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void where_group_or() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional conditional1 = qb.get(a.getId()).equalTo(1);
        Conditional conditional2 = qb.get(a.getName()).equalTo("foo");
        Conditional conditional3 = qb.get(a.getStartDate()).equalTo(
                    qb.param(new Date()));

        qb.or();
        Conditional g1 = qb.group(conditional1, conditional2);

        query.where(conditional3, g1);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.startDate = :p1 OR (a.id = 1 AND a.name = 'foo')",
                resolver.getQueryString());
    }

    @Test
    public void where_collection_join() {
        ProxyQuery<CollectionEntity> query = queryFactory
                .createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        CollectionReferencedEntity b = query.join(a.getEntities());

        Conditional<String> p = qb.get(b.getText()).equalTo("foo");
        query.where(p);

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "FROM CollectionEntity a JOIN a.entities b WHERE b.text = 'foo'",
                resolver.getQueryString());
    }

    @Test
    public void where_cartesian_join() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        SimpleEntity b = query.thetaJoin(SimpleEntity.class);

        query.where(qb.get(a.getId()).equalTo(qb.get(b.getId())));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.id = b.id",
                resolver.getQueryString());
    }

    @Test
    public void where_cartesian_join_nested_property() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        NestedEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        NestedEntity b = query.thetaJoin(NestedEntity.class);

        query.where(qb.get(a.getSimpleEntity())
                        .equalTo(qb.get(b.getSimpleEntity())));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a.simpleEntity = b.simpleEntity",
                resolver.getQueryString());
    }

    @Test
    public void where_cartesian_join_by_entities() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        NestedEntity b = query.thetaJoin(NestedEntity.class);

        query.where(
                qb.get(a).equalTo(qb.get(b.getSimpleEntity())
                ));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE a = b.simpleEntity",
                resolver.getQueryString());
    }


    @Test
    public void where_nested_join() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        SimpleEntity b = query.join(qb.get(a.getSimpleEntity()));

        query.where(qb.get(b.getId()).equalTo(1)
        );

        // NOTE: "a" not "b", the actual "a" has not been resolved yet (no from)
        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id = 1",
                resolver.getQueryString());
    }

    @Test
    public void where_nested_nested_join() {

        ProxyQuery<NestedNestedEntity> query = queryFactory.createQuery(NestedNestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedNestedEntity a = query.getRootProxy();

        SimpleEntity b = query.join(qb.get(a.getNestedItem().getSimpleEntity()));

        query.andWhere(b.getId()).equalTo(1);

        // NOTE: "a" not "b", the actual "a" has not been resolved yet (no from)
        strategy.visit(query, resolver);
        Assert.assertEquals(
                "FROM NestedNestedEntity a JOIN a.nestedItem.simpleEntity b "
                + "WHERE b.id = 1",
                resolver.getQueryString());
    }

    @Test
    public void where_nested_join_NestedEmbeddedIdEntity() {

        ProxyQuery<NestedEmbeddedIdEntity> query = queryFactory.createQuery(NestedEmbeddedIdEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEmbeddedIdEntity a = query.getRootProxy();

        EmbeddedIdEntity b = query.join(qb.get(a.getEntity()));

        query.andWhere(b.getId().getIdValue()).equalTo(1);

        strategy.visit(query, resolver);
        Assert.assertEquals(
                "FROM NestedEmbeddedIdEntity a JOIN a.entity b "
                + "WHERE b.id.idValue = 1",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        Subquery<Integer> subquery = query2.subquery(qb.get(b.getId()));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id = (SELECT b.id FROM SimpleEntity b WHERE b.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery_all_operator() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        QueryValue<Integer> subquery = qb.all(
                query2.subquery(qb.get(b.getId())));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id = ALL(SELECT b.id FROM SimpleEntity b WHERE b.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery_in_operator() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        Subquery<Integer> subquery = query2.subquery(qb.get(b.getId()));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).in(subquery));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id IN (SELECT b.id FROM SimpleEntity b WHERE b.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery_any_operator() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        QueryValue<Integer> subquery = qb.any(
                query2.subquery(qb.get(b.getId())));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id = ANY(SELECT b.id FROM SimpleEntity b WHERE b.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery_some_operator() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        QueryValue<Integer> subquery = qb.some(
                query2.subquery(qb.get(b.getId())));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE a.id = SOME(SELECT b.id FROM SimpleEntity b WHERE b.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery_exists() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        Conditional<Integer> subquery = qb.exists(query2.subquery(qb.get(b.getId())));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.where(subquery);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE EXISTS(SELECT a.id FROM SimpleEntity a WHERE a.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_subquery_not_exists() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        Conditional<Integer> subquery = qb.notExists(query2.subquery(qb.get(b.getId())));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        query.where(subquery);

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals(
                "WHERE NOT EXISTS(SELECT a.id FROM SimpleEntity a WHERE a.id = 2)",
                resolver.getQueryString());
    }

    @Test
    public void where_group_empty() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        // Don't do this in a query
        query.where(qb.group());

        strategy.visit(query.getWhere(), resolver);
        Assert.assertEquals("WHERE ", resolver.getQueryString());
    }
}
