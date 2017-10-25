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
package org.cementframework.querybyproxy.hql.hibernate.tests;

import java.util.List;

import junit.framework.Assert;

import org.cementframework.querybyproxy.hql.hibernate.api.HibernateProxyQueries;
import org.cementframework.querybyproxy.hql.hibernate.impl.HibernateProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.impl.compiler.BasicQueryCompilerFactory;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CollectionReferencedEntity;
import org.cementframework.querybyproxy.hql.jpa.tests.model.Customer;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.hibernate.Query;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class PersistenceTest extends PersistenceTestBase {


    @Test(timeout = 2000)
    public void basic_operation() {
        ProxyQueryFactory qbpfactory = HibernateProxyQueries.createQueryFactory(session);
        ProxyQuery<Customer> query = qbpfactory.createQuery(Customer.class);
        Customer customer = query.getRootProxy();

        query.andWhere(customer.getFirstName()).equalTo("John");

        List<Customer> results = query.select().find();

        Assert.assertEquals(
                "SELECT a FROM Customer a WHERE a.firstName = 'John'",
                query.toString());

        Assert.assertEquals(1, results.size());
    }

    @Test(timeout = 2000)
    public void empty() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(3, results.size());
    }

    @Test(timeout = 2000)
    public void parameterized() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(qb.param(1));

        List<SimpleEntity> results = query.select().find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("foo", results.get(0).getName());
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
    public void parameterized_single() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(qb.param(1));

        SimpleEntity result = query.select().findSingleResult();

        Assert.assertEquals("foo", result.getName());
    }

    @Test(timeout = 2000)
    public void typedQuery_list() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(qb.param(1));

        List<Integer> results = query.select(qb.literal(2)).find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Integer.valueOf(2), results.get(0));
    }

    @Test(timeout = 2000)
    public void typedQuery_single() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(qb.param(1));

        Integer result = query.select(qb.literal(2)).findSingleResult();

        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test(timeout = 2000)
    public void dynamicQuery_list() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(qb.param(1));

        List<Object[]> results = query.select(qb.literal(2), qb.literal(3)).find();

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Integer.valueOf(2), results.get(0)[0]);
        Assert.assertEquals(Integer.valueOf(3), results.get(0)[1]);
    }

    @Test(timeout = 2000)
    public void dynamicQuery_single() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        query.andWhere(a.getId()).equalTo(qb.param(1));

        Object[] result = query.select(qb.literal(2), qb.literal(3)).findSingleResult();

        Assert.assertEquals(Integer.valueOf(2), result[0]);
        Assert.assertEquals(Integer.valueOf(3), result[1]);
    }

    @Test(timeout = 2000)
    public void test() {
        HibernateProxyQueryFactoryImpl impl = new HibernateProxyQueryFactoryImpl();
        Assert.assertNull(impl.getSession());

        impl.setQueryCompilerFactory(new BasicQueryCompilerFactory());

        impl.setSession(session);
        Assert.assertEquals(session, impl.getSession());

        new HibernateProxyQueries();
    }

    @Test(timeout = 2000)
    public void correlated_subquery() {
        ProxyQuery<CollectionEntity> query = queryFactory.createQuery(CollectionEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        CollectionEntity a = query.getRootProxy();

        ProxyQuery<CollectionReferencedEntity> subquery = queryFactory.correlatedSubquery(a.getEntities());
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


        Query hibQuery = session.createQuery(query.toString());
        hibQuery.setParameter("xyz1", 1);

        SimpleEntity result = (SimpleEntity) hibQuery.uniqueResult();
        Assert.assertEquals("foo", result.getName());
    }
}
