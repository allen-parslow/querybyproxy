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
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.StrictQueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.TrimOption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TypedSelectionFunctionTest {
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
    public void count() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Long> sel = qb.count(qb.get(a.getId()));

        strategy.visit(sel, resolver);
        Assert.assertEquals("COUNT(a.id)", resolver.getQueryString());
    }


    @Test
    public void min() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.min(qb.get(a.getId()));

        strategy.visit(sel, resolver);
        Assert.assertEquals("MIN(a.id)", resolver.getQueryString());
    }

    @Test
    public void max() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.max(qb.get(a.getId()));

        strategy.visit(sel, resolver);
        Assert.assertEquals("MAX(a.id)", resolver.getQueryString());
    }

    @Test
    public void sum() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Integer> sel = qb.sum(qb.get(a.getId()));

        strategy.visit(sel, resolver);
        Assert.assertEquals("SUM(a.id)", resolver.getQueryString());
    }

    @Test
    public void avg() {
        ProxyQuery<SimpleEntity> query = queryFactory.createQuery(SimpleEntity.class);
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
        SimpleEntity a = query.getRootProxy();

        QueryValue<Double> sel = qb.avg(qb.get(a.getId()));

        strategy.visit(sel, resolver);
        Assert.assertEquals("AVG(a.id)", resolver.getQueryString());
    }


    @Test
    public void abs() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<Integer> value = qb.abs(qb.literal(3));

        strategy.visit(value, resolver);
        Assert.assertEquals("ABS(3)", resolver.getQueryString());
    }

    @Test
    public void concat() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<Integer> value = qb.concat(
                qb.literal("a"), qb.literal("b"));

        strategy.visit(value, resolver);
        Assert.assertEquals("CONCAT('a', 'b')", resolver.getQueryString());
    }

    @Test
    public void currentDate() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<Date> value = qb.currentDate();

        strategy.visit(value, resolver);
        Assert.assertEquals("CURRENT_DATE", resolver.getQueryString());
    }

    @Test
    public void currentTime() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<Date> value = qb.currentTime();

        strategy.visit(value, resolver);
        Assert.assertEquals("CURRENT_TIME", resolver.getQueryString());
    }

    @Test
    public void currentTimestamp() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<Date> value = qb.currentTimestamp();

        strategy.visit(value, resolver);
        Assert.assertEquals("CURRENT_TIMESTAMP", resolver.getQueryString());
    }

    @Test
    public void length() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<Integer> value = qb.length(qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("LENGTH('a')", resolver.getQueryString());
    }

    @Test
    public void trim() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<String> value = qb.trim(qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("TRIM('a')", resolver.getQueryString());
    }

    @Test
    public void trim_Option_LEADING() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<String> value = qb.trim(TrimOption.LEADING, qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("TRIM(LEADING 'a')", resolver.getQueryString());
    }

    @Test
    public void trim_Option_TRAILING() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<String> value = qb.trim(TrimOption.TRAILING, qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("TRIM(TRAILING 'a')", resolver.getQueryString());
    }

    @Test
    public void trim_Option_BOTH() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<String> value = qb.trim(TrimOption.BOTH, qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("TRIM(BOTH 'a')", resolver.getQueryString());
    }

    @Test
    public void trim_Option_BOTH_character() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue<String> value = qb.trim(
                TrimOption.BOTH,
                qb.literal(' '),
                qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("TRIM(BOTH ' ' FROM 'a')", resolver.getQueryString());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void locate() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.locate(qb.literal("a"), qb.literal("b"));

        strategy.visit(value, resolver);
        Assert.assertEquals("LOCATE('a', 'b')", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void locate_start() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.locate(qb.literal("a"), qb.literal("b"), 2);

        strategy.visit(value, resolver);
        Assert.assertEquals("LOCATE('a', 'b', 2)", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void lower() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.lower(qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("LOWER('a')", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void upper() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.upper(qb.literal("a"));

        strategy.visit(value, resolver);
        Assert.assertEquals("UPPER('a')", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void substring() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.substring(qb.literal("a"), 1, 2);

        strategy.visit(value, resolver);
        Assert.assertEquals("SUBSTRING('a', 1, 2)", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void mod() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.mod(qb.literal(1), qb.literal(2));

        strategy.visit(value, resolver);
        Assert.assertEquals("MOD(1, 2)", resolver.getQueryString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sqrt() {
         ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        StrictQueryValue value = qb.sqrt(qb.literal(1));

        strategy.visit(value, resolver);
        Assert.assertEquals("SQRT(1)", resolver.getQueryString());
    }
}
