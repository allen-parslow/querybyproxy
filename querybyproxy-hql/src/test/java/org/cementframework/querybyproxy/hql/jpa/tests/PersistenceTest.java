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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import junit.framework.Assert;

import org.cementframework.querybyproxy.hql.impl.compiler.BasicQueryCompilerFactory;
import org.cementframework.querybyproxy.hql.jpa.api.JpaProxyQueries;
import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.jpa.tests.model.AliasedPropertyEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionReferencedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.ManyToManyEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.ManyToManyItem;
import org.cementframework.querybyproxy.hql.jpa.tests.model.NestedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntityDto;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.StrictQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.StrictQueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.cementframework.querybyproxy.shared.api.model.values.TrimOption;
import org.hibernate.LazyInitializationException;
import org.junit.Test;

/**
 * @author allenparslow
 */

public class PersistenceTest extends PersistenceTestBase {

    @Test(timeout = 2000)
    public void persistence_typed() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_typed_limit() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.orderBy(a.getId());

        List<SimpleEntity> results = query.select().limit(1).find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("foo", results.get(0).getName());
    }

    @Test(timeout = 2000)
    public void persistence_typed_first() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.orderBy(a.getId());

        List<SimpleEntity> results = query.select().limit(1).first(1).find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("bar", results.get(0).getName());
    }

    @Test(timeout = 2000)
    public void persistence_dynamic_limit() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.orderBy(a.getId());

        List<Object[]> results = query.select(a.getId(), a.getName()).limit(1).find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("foo", results.get(0)[1]);
    }

    @Test(timeout = 2000)
    public void persistence_dynamic_first() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();

        query.orderBy(a.getId());

        List<Object[]> results = query.select(a.getId(), a.getName()).limit(1).first(1).find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("bar", results.get(0)[1]);
    }

    @Test(timeout = 2000)
    public void persistence_terse_nested_property() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        NestedEntity a = query.getRootProxy();

        List<SimpleEntity> results = query.select(a.getSimpleEntity()).find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_terse_many_to_many() {
        ProxyQuery<ManyToManyEntity> query = queryFactory.createQuery(ManyToManyEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        ManyToManyEntity a = query.getRootProxy();

        query.orderBy(qb.desc(a.getText()));

        List<ManyToManyEntity> results = query.select().find();

        Assert.assertEquals(2, results.size());
        Assert.assertEquals("b", results.get(0).getText());
        Assert.assertEquals("a", results.get(1).getText());
        Assert.assertEquals(2, results.get(1).getEntities().size());
        ManyToManyItem item;

        item = results.get(1).getEntities().get(0);
        Assert.assertEquals("x", item.getLabel());

        item = results.get(1).getEntities().get(1);
        Assert.assertEquals("y", item.getLabel());
    }

    @Test(timeout = 2000)
    public void persistence_terse_many_to_many_join() {
        ProxyQuery<ManyToManyEntity> query = queryFactory.createQuery(ManyToManyEntity.class);
        ManyToManyEntity a = query.getRootProxy();
        ManyToManyItem b = query.join(a.getEntities());

        query.andWhere(b.getLabel()).equalTo("z");

        List<ManyToManyEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("b", results.get(0).getText());
    }

    @Test(timeout = 2000)
    public void persistence_terse() {
        ProxyQuery<SimpleEntity> query =
                queryFactory.createQuery(SimpleEntity.class);
        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_terse_groupBy() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());

        List<Object[]> results = query.select(
                a.getName(), qb.max(a.getId())
                ).find();

        Assert.assertEquals(2, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_terse_groupBy_having() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                qb.count(a.getId()).equalTo(1L)
                );

        List<Object[]> results = query.select(
                a.getName(), qb.max(a.getId())
                ).find();

        Assert.assertEquals(1, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_terse_select_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1));

        List<Integer> result = query.select(a.getId()).find();

        Assert.assertEquals(1, result.size());
        Integer row;

        row = result.get(0);
        Assert.assertEquals(Integer.valueOf(1), row);
    }

    @Test(timeout = 2000)
    public void persistence_orderBy() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.orderBy(qb.get(a.getId()));

        List<SimpleEntity> results = query.select().find();
        Assert.assertEquals(3, results.size());
        SimpleEntity row;

        row = results.get(0);
        Assert.assertEquals(Integer.valueOf(1), row.getId());
    }

    @Test(timeout = 2000)
    public void persistence_orderBy_desc() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.orderBy(
                qb.desc(a.getId())
                );

        List<SimpleEntity> results = query.select().find();
        Assert.assertEquals(3, results.size());
        SimpleEntity row;

        row = results.get(0);
        Assert.assertEquals(Integer.valueOf(3), row.getId());
    }

    @Test(timeout = 2000)
    public void persistence_collection() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_collection_join() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        query.join(a.getEntities());

        query.where(qb.get(a.getId()).equalTo(2));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(1, results.get(0).getEntities().size());
        Assert.assertEquals("c", results.get(0).getEntities().get(0).getText());

        entityManager.clear();
        results = query.select().find();
        entityManager.clear();
        Assert.assertEquals(1, results.size());

        try {
            results.get(0).getEntities().size();

            Assert.fail("Expected exception: " + LazyInitializationException.class);
        } catch (LazyInitializationException e) {
            // expected this exception
        }
    }

    @Test(timeout = 2000)
    public void persistence_collection_join_fetch() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        query.joinFetch(a.getEntities());

        query.where(qb.get(a.getId()).equalTo(2));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(1, results.get(0).getEntities().size());
        Assert.assertEquals("c", results.get(0).getEntities().get(0).getText());

        entityManager.clear();
        results = query.select().find();
        entityManager.clear();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(1, results.get(0).getEntities().size());
    }

    @Test(timeout = 2000)
    public void persistence_collection_where_id1_size_2() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Integer.valueOf(1), results.get(0).getId());
        Assert.assertEquals(2, results.get(0).getEntities().size());
    }

    @Test(timeout = 2000)
    public void persistence_collection_where_list_property() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();
        CollectionReferencedEntity b = query.join(a.getEntities());

        query.where(qb.get(b.getText()).equalTo(qb.literal("c")));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(1, results.get(0).getEntities().size());
        Assert.assertEquals("c", results.get(0).getEntities().get(0).getText());
    }

    @Test(timeout = 2000)
    public void persistence_collection_join_list_property_not_found() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();
        CollectionReferencedEntity b = query.join(a.getEntities());

        query.where(qb.get(b.getText()).equalTo(qb.literal("x")));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(0, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_collection_left_join() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();
        CollectionReferencedEntity b = query.leftJoin(a.getEntities());

        query.where(qb.get(b.getId()).isNull());

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Integer.valueOf(3), results.get(0).getId());
    }

    @Test(timeout = 2000)
    public void persistence_collection_where_id3_size_0() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(3));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(0, results.get(0).getEntities().size());
    }

    @Test(timeout = 2000)
    public void persistence_collection_where_id4() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(4));

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(0, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_nested() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);

        List<NestedEntity> results = query.select().find();

        Assert.assertEquals(4, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_nested_property() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        QueryValue<SimpleEntity> select = qb.get(a.getSimpleEntity());

        List<SimpleEntity> results = query.select(select).find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_nested_join() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        SimpleEntity b = query.join(qb.get(a.getSimpleEntity()));

        query.where(
                qb.get(b.getId()).equalTo(1)
                );

        List<NestedEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("foo", results.get(0).getSimpleEntity().getName());

        entityManager.clear();
        results = query.select().find();
        entityManager.clear();
        Assert.assertEquals(1, results.size());

        try {
            results.get(0).getSimpleEntity().getName();

            Assert.fail("Expected exception: " + LazyInitializationException.class);
        } catch (LazyInitializationException e) {
            // expected this exception
        }
    }

    @Test(timeout = 2000)
    public void persistence_nested_join_is_null() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        SimpleEntity b = query.join(qb.get(a.getSimpleEntity()));

        query.where(
                qb.get(b.getId()).isNull()
                );

        List<NestedEntity> results = query.select().find();

        Assert.assertEquals(0, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_nested_left_join_is_null() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        SimpleEntity b = query.leftJoin(qb.get(a.getSimpleEntity()));

        query.where(
                qb.get(b.getId()).isNull()
                );

        List<NestedEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Integer.valueOf(4), results.get(0).getId());
    }

    @Test(timeout = 2000)
    public void persistence_nested_fetch_join() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        SimpleEntity b = query.joinFetch(qb.get(a.getSimpleEntity()));

        query.where(
                qb.get(b.getId()).equalTo(1)
                );

        List<NestedEntity> results = query.select().find();
        entityManager.clear();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("foo", results.get(0).getSimpleEntity().getName());
    }

    @Test(timeout = 2000)
    public void persistence_where() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(2));

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        SimpleEntity row;

        row = results.get(0);

        Assert.assertEquals(Integer.valueOf(2), row.getId());
        Assert.assertEquals("bar", row.getName());
        Assert.assertEquals(42.0, row.getValue());
        Assert.assertEquals("2009-01-02 01:01:01.00001", row.getStartDate().toString());
    }

    @Test(timeout = 2000)
    public void persistence_where_multiply() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(
                qb.literal(1).times(qb.literal(2))));

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        SimpleEntity row;

        row = results.get(0);

        Assert.assertEquals(Integer.valueOf(2), row.getId());
    }

    @Test(timeout = 2000)
    public void persistence_trim_Option_BOTH_character() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        StrictQueryValue<String> value = qb.trim(
                TrimOption.BOTH,
                qb.literal('b'),
                qb.literal("bob"));

        query.where(qb.get(a.getId()).equalTo(2));

        String result = query.select(value).findSingleResult();

        Assert.assertEquals("o", result);
    }

    @Test(timeout = 2000)
    public void persistence_nested_where_1() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.where(
                qb.get(a.getId()).equalTo(1)
                );

        List<NestedEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("foo", results.get(0).getSimpleEntity().getName());
    }

    @Test(timeout = 2000)
    public void persistence_nested_where_null_nested() {
        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        NestedEntity a = query.getRootProxy();

        query.where(
                qb.get(a.getId()).equalTo(4)
                );

        List<NestedEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertNull(results.get(0).getSimpleEntity());
    }

    @Test(timeout = 2000)
    public void persistence_greater_than_date_2() {

        Calendar cal = Calendar.getInstance();
        cal.set(2009, 0, 1);
        Date firstDayOfYear = cal.getTime();

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getStartDate()).greaterThan(qb.param(firstDayOfYear)));

        List<SimpleEntity> result = query.select().find();

        Assert.assertEquals(2, result.size());
    }

    @Test(timeout = 2000)
    public void persistence_greater_than_date_1() {

        Calendar cal = Calendar.getInstance();
        cal.set(2009, 0, 2);
        Date firstDayOfYear = cal.getTime();

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getStartDate()).greaterThan(qb.param(firstDayOfYear)));

        List<SimpleEntity> result = query.select().find();

        Assert.assertEquals(1, result.size());
        SimpleEntity row;

        row = result.get(0);
        Assert.assertEquals(Integer.valueOf(3), row.getId());
    }

    @Test(timeout = 2000)
    public void persistence_greater_than_date_0() {

        Calendar cal = Calendar.getInstance();
        cal.set(2009, 0, 3);
        Date firstDayOfYear = cal.getTime();

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getStartDate()).greaterThan(qb.param(firstDayOfYear)));

        List<SimpleEntity> result = query.select().find();

        Assert.assertEquals(0, result.size());
    }

    @Test(timeout = 2000)
    public void persistence_result_is_object_array() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(qb.get(a.getName()), qb.get(a.getValue()));
        query.where(qb.get(a.getId()).equalTo(2));

        List<Object[]> result = query.build().find();

        Assert.assertEquals(1, result.size());
        Object[] row;

        row = result.get(0);
        Assert.assertEquals("bar", row[0]);
        Assert.assertEquals(Double.valueOf(42), row[1]);
    }

    @Test(timeout = 2000)
    public void persistence_select_literal_integer() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1));

        List<Integer> result = query.select(qb.literal(3)).find();

        Assert.assertEquals(1, result.size());
        Integer row;

        row = result.get(0);
        Assert.assertEquals(Integer.valueOf(3), row);
    }

    @Test(timeout = 2000)
    public void persistence_select_literal_double() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1));

        List<Double> result = query.select(qb.literal(3.33)).find();

        Assert.assertEquals(1, result.size());
        Double row;

        row = result.get(0);
        Assert.assertEquals(Double.valueOf(3.33), row);
    }

    @Test(timeout = 2000)
    public void persistence_select_literal_string() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1));

        List<String> result = query.select(qb.literal("fooze")).find();

        Assert.assertEquals(1, result.size());
        String row;

        row = result.get(0);
        Assert.assertEquals("fooze", row);
    }

    @Test(timeout = 2000)
    public void persistence_findSingleResult_string() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(qb.get(a.getName()));
        query.where(qb.get(a.getId()).equalTo(1));

        String result = query.select(qb.literal("foo")).findSingleResult();

        Assert.assertEquals("foo", result);
    }

    @Test(timeout = 2000)
    public void persistence_findSingleResult() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.select(qb.get(a.getName()), qb.get(a.getValue()));
        query.where(qb.get(a.getId()).equalTo(2));

        Object[] row = query.build().findSingleResult();

        Assert.assertEquals("bar", row[0]);
        Assert.assertEquals(Double.valueOf(42), row[1]);
    }

    @Test(timeout = 2000)
    public void persistence_cartesian_join_by_name_with_id() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        SimpleEntity b = query.thetaJoin(SimpleEntity.class);

        query.where(
                qb.get(a.getName()).equalTo(qb.get(b.getName())),
                qb.get(a.getId()).equalTo(2)
                );

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(2, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_cartesian_join_by_name() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        SimpleEntity b = query.thetaJoin(SimpleEntity.class);

        query.where(
                qb.get(a.getName()).equalTo(qb.get(b.getName()))
                );

        List<SimpleEntity> results = query.select().find();

        // (1 foo rows) + ((2 bar rows)*2 [cartesian])
        Assert.assertEquals(5, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_cartesian_join_by_entities() {

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        NestedEntity b = query.thetaJoin(NestedEntity.class);

        query.where(
                qb.get(a).equalTo(qb.get(b.getSimpleEntity())
                ));

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_cartesian_join_nested_property() {

        ProxyQuery<NestedEntity> query = queryFactory.createQuery(NestedEntity.class);
        NestedEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        NestedEntity b = query.thetaJoin(NestedEntity.class);

        query.where(qb.get(a.getSimpleEntity())
                .equalTo(qb.get(b.getSimpleEntity())));

        List<NestedEntity> results = query.select().find();

        // (1 foo rows) + ((2 bar rows)*2 [cartesian])
        Assert.assertEquals(5, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_subquery() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        Subquery<Integer> subquery = query2.subquery(qb.get(b.getId()));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());

    }

    @Test(timeout = 2000)
    public void persistence_subquery_any_operator() {
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        ProxyQuery<SimpleEntity> query2 = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity b = query2.getRootProxy();

        query2.where(qb.get(b.getId()).equalTo(qb.literal(2)));

        QueryValue<Integer> subquery = qb.any(query2.subquery(qb.get(b.getId())));

        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        SimpleEntity a = query.getRootProxy();
        query.where(qb.get(a.getId()).equalTo(subquery));
        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_isEmpty() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        query.where(qb.get(a.getEntities()).isEmpty());

        List<CollectionEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
    }

    @Test(timeout = 2000)
    public void persistence_typed_aliased() {
        ProxyQuery<AliasedPropertyEntity> query = queryFactory
                .createQuery(AliasedPropertyEntity.class);
        AliasedPropertyEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.where(qb.get(a.getFlag()).isNotNull());

        List<AliasedPropertyEntity> results = query.select().find();
        Assert.assertEquals(2, results.size());

        AliasedPropertyEntity result = results.get(0);
        Assert.assertNotNull(result.getFlag());
    }

    @Test(timeout = 2000)
    public void persistence_typed_aliased_conversion_check() {
        ProxyQuery<AliasedPropertyEntity> query = queryFactory
                .createQuery(AliasedPropertyEntity.class);
        AliasedPropertyEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.where(qb.get(a.getId()).equalTo(1));

        List<AliasedPropertyEntity> results = query.select().find();
        Assert.assertEquals(1, results.size());

        AliasedPropertyEntity result = results.get(0);
        Assert.assertTrue(result.getFlag());

        query.where(qb.get(a.getId()).equalTo(2));

        results = query.select().find();
        Assert.assertEquals(1, results.size());

        result = results.get(0);
        Assert.assertFalse(result.getFlag());

        query.where(qb.get(a.getId()).equalTo(3));

        results = query.select().find();
        Assert.assertEquals(1, results.size());

        result = results.get(0);
        Assert.assertNull(result.getFlag());
    }

    @Test(timeout = 2000)
    public void persistence_typed_aliased_alternate_return_type() {
        ProxyQuery<AliasedPropertyEntity> query = queryFactory
                .createQuery(AliasedPropertyEntity.class);
        AliasedPropertyEntity a = query.getRootProxy();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        query.where(qb.get(a.getFlag(), String.class)
                .equalTo(qb.literal("1")));

        List<String> results = query.select(qb.get(a.getFlag(), String.class)).find();
        Assert.assertEquals(1, results.size());

        String result = results.get(0);
        Assert.assertEquals("1", result);
    }

    @Test(timeout = 2000)
    public void persistence_terse_constructor() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.where(qb.get(a.getId()).equalTo(1));

        List<SimpleEntityDto> results = query.select(SimpleEntityDto.class,
                a.getId(), a.getName()).find();

        Assert.assertEquals(1, results.size());
        SimpleEntityDto result = results.get(0);
        Assert.assertEquals(Integer.valueOf(1), result.getId());
        Assert.assertEquals("foo", result.getName());
    }

    @Test(timeout = 2000)
    public void test() {
        JpaProxyQueryFactoryImpl impl =
                new JpaProxyQueryFactoryImpl(entityManager);
        Assert.assertEquals(entityManager, impl.getEntityManager());

        impl.setQueryCompilerFactory(new BasicQueryCompilerFactory());

        impl.setEntityManager(entityManager);
        Assert.assertEquals(entityManager, impl.getEntityManager());

        new JpaProxyQueries();
    }

    @Test(timeout = 2000)
    public void correlated_subquery() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        ProxyQuery<CollectionReferencedEntity> subquery =
                queryFactory.correlatedSubquery(a.getEntities());
        CollectionReferencedEntity b = subquery.getRootProxy();

        subquery.andWhere(b.getId()).equalTo(a.getId());

        query.where(qb.exists(subquery.subquery()));

        List<CollectionEntity> results = query.select().find();
        Assert.assertEquals(1, results.size());
    }

    @Test(timeout = 2000)
    public void parameter_name() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> param1 = qb.param("xyz1", Integer.class);

        query.select();
        query.andWhere(a.getId()).equalTo(param1);


        Query javaxQuery = entityManager.createQuery(query.toString());
        javaxQuery.setParameter("xyz1", 1);

        SimpleEntity result = (SimpleEntity) javaxQuery.getSingleResult();
        Assert.assertEquals("foo", result.getName());
    }

    @Test(timeout = 2000)
    public void persistence_count() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.count(a.getId()).equalTo(qb.param(2L))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_count_double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.count(a.getValue()).equalTo(qb.param(2L))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_count_string() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.count(a.getName()).equalTo(qb.param(2L))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_sum() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.sum(a.getValue()).equalTo(qb.param(85D))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_sum_integer_long() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.sum(a.getId()).equalTo(qb.param(5L))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_sum_integer_long__type_change() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        StrictQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.sum(qb.get(a.getId()), Long.class).equalTo(qb.param(5L))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_max_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.max(a.getId()).equalTo(qb.param(3))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_min_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.min(a.getId()).equalTo(qb.param(2))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }


    @Test(timeout = 2000)
    public void persistence_max_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.max(a.getValue()).equalTo(qb.param(43D))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_min_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.min(a.getValue()).equalTo(qb.param(42D))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_avg_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.avg(a.getId()).equalTo(qb.param(2.5D))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_avg_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.avg(a.getValue()).equalTo(qb.param(42.5D))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void persistence_avg_strict() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        StrictQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.groupBy(a.getName());
        query.having(
                    qb.avg(qb.get(a.getId())).equalTo(qb.param(2.5D))
                );

        Object result = query.select(a.getName()).findSingleResult();

        Assert.assertEquals("bar", result);
    }

    @Test(timeout = 2000)
    public void return_count() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Long result = query.select(qb.count(a.getId())).findSingleResult();

        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test(timeout = 2000)
    public void return_count_double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Long result = query.select(qb.count(a.getValue())).findSingleResult();

        Assert.assertEquals(Long.valueOf(2), result);
    }

    @Test(timeout = 2000)
    public void return_count_string() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Long result = query.select(qb.count(a.getName())).findSingleResult();

        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test(timeout = 2000)
    public void return_sum() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();


        Double result = query.select(qb.sum(a.getValue())).findSingleResult();

        Assert.assertEquals(85.0D, result);
    }

    @Test(timeout = 2000)
    public void return_sum_integer_long() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Long result = query.select(qb.sum(a.getId())).findSingleResult();

        Assert.assertEquals(Long.valueOf(6), result);
    }

    @Test(timeout = 2000)
    public void return_sum_integer_long__type_change() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        StrictQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Long result = query.select(qb.sum(qb.get(a.getId()), Long.class)).findSingleResult();

        Assert.assertEquals(Long.valueOf(6), result);
    }

    @Test(timeout = 2000)
    public void return_max_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Integer result = query.select(qb.max(a.getId())).findSingleResult();

        Assert.assertEquals(Integer.valueOf(3), result);
    }

    @Test(timeout = 2000)
    public void return_min_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Integer result = query.select(qb.min(a.getId())).findSingleResult();

        Assert.assertEquals(Integer.valueOf(1), result);
    }


    @Test(timeout = 2000)
    public void return_max_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Double result = query.select(qb.max(a.getValue())).findSingleResult();

        Assert.assertEquals(43D, result);
    }

    @Test(timeout = 2000)
    public void return_min_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Double result = query.select(qb.min(a.getValue())).findSingleResult();

        Assert.assertEquals(42D, result);
    }

    @Test(timeout = 2000)
    public void return_avg_Integer() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Double result = query.select(qb.avg(a.getId())).findSingleResult();

        Assert.assertEquals(2D, result);
    }

    @Test(timeout = 2000)
    public void return_avg_Double() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Double result = query.select(qb.avg(a.getValue())).findSingleResult();

        Assert.assertEquals(42.5D, result);
    }

    @Test(timeout = 2000)
    public void return_avg_strict() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        StrictQueryBuilder qb = query.getQueryFactory().getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Double result = query.select(qb.avg(qb.get(a.getId()))).findSingleResult();

        Assert.assertEquals(2D, result);
    }



}
