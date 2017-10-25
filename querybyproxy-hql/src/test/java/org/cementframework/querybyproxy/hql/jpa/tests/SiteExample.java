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
import java.util.List;

import junit.framework.Assert;

import org.cementframework.querybyproxy.hql.jpa.api.JpaProxyQueries;
import org.cementframework.querybyproxy.hql.jpa.tests.model.Customer;
import org.cementframework.querybyproxy.hql.jpa.tests.model.CustomerOrder;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.ProxyQueryFactory;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class SiteExample extends PersistenceTestBase {

    @Test(timeout = 2000)
    public void basic_operation() {
        ProxyQueryFactory qbpFactory = JpaProxyQueries.createQueryFactory(entityManager);
        ProxyQuery<Customer> query = qbpFactory.createQuery(Customer.class);
        Customer customer = query.getRootProxy();

        query.andWhere(customer.getFirstName()).equalTo("John");

        List<Customer> results = query.select().find();

        Assert.assertEquals(
                "SELECT a FROM Customer a WHERE a.firstName = 'John'",
                query.toString());

        Assert.assertEquals(1, results.size());
    }

    @Test(timeout = 2000)
    public void subquery() {
ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

ProxyQuery<Customer> multipleFirstNames = queryFactory.createQuery(Customer.class);
Customer b = multipleFirstNames.getRootProxy();

multipleFirstNames.groupBy(b.getLastName());
multipleFirstNames.having(qb.count(b.getFirstName()).greaterThan(2L));
Subquery<String> subquery = multipleFirstNames.subquery(b.getLastName());

ProxyQuery<Customer> query = queryFactory.createQuery(Customer.class);
Customer a = query.getRootProxy();

query.andWhere(a.getLastName()).in(subquery);

List<Customer> results = query.find();

Assert.assertEquals(0, results.size());
    }

    @Test(timeout = 2000)
    public void correlated_subquery() {
ProxyQuery<Customer> query = queryFactory.createQuery(Customer.class);
ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
Customer customer = query.getRootProxy();

ProxyQuery<CustomerOrder> pastDue =
        queryFactory.correlatedSubquery(customer.getOrders());
CustomerOrder order = pastDue.getRootProxy();

pastDue.andWhere(qb.param(new Date()).greaterThan(order.getDueDate()));

query.where(qb.exists(pastDue.subquery()));

List<Customer> results = query.find();

Assert.assertEquals(0, results.size());
    }
}
