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
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.conditionals.Conditional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 *
 */
public class TypedHavingTest {
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
    public void typed_having_1() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Long> p = qb.count(qb.get(a.getId())).equalTo(qb.literal(1L));

        query.having(p);

        strategy.visit(query.getHaving(), resolver);
        Assert.assertEquals("HAVING COUNT(a.id) = 1", resolver.getQueryString());
    }

    @Test
    public void typed_having_2() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        Conditional<Long> p1 = qb.count(qb.get(a.getId())).equalTo(qb.literal(1L));
        Conditional<Integer> p2 = qb.max(qb.get(a.getId())).equalTo(qb.literal(2));

        query.having(p1, p2);

        strategy.visit(query.getHaving(), resolver);
        Assert.assertEquals("HAVING COUNT(a.id) = 1 AND MAX(a.id) = 2", resolver.getQueryString());
    }
}
