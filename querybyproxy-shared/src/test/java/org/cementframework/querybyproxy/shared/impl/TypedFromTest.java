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
package org.cementframework.querybyproxy.shared.impl;

import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.StrictQuery;
import org.cementframework.querybyproxy.shared.api.StrictQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoinModifier;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoinType;
import org.cementframework.querybyproxy.shared.impl.model.joins.PathJoinImpl;
import org.cementframework.querybyproxy.shared.impl.model.joins.RootJoinImpl;
import org.cementframework.querybyproxy.shared.impl.testmodel.NestedNested;
import org.cementframework.querybyproxy.shared.impl.testmodel.NestedProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TypedFromTest extends AbstractProxyQueryImplTest {
    StrictQueryBuilder qb = queryFactory.getQueryBuilder();

    @Test
    public void from_basic() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        Assert.assertEquals(1, query.getFrom().getJoins().size());
        RootJoinImpl join = (RootJoinImpl) query.getFrom().getJoins().get(0);

        // class name for root join
        Assert.assertSame(SimpleCase.class, join.getProxyClass());
        // need for identification variable
        Assert.assertSame(a, join.getProxy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void join_strict_nested_not_valid() {
        StrictQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        query.join(qb.literal(a.getNested()));
    }


    @Test
    public void join_strict_nested() {
        StrictQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.join(qb.get(a.getNested()));

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.INNER, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("nested",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_strict_nested_left() {
        StrictQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.leftJoin(qb.get(a.getNested()));

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.LEFT, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("nested",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_strict_nested_right() {
        StrictQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.rightJoin(qb.get(a.getNested()));

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.RIGHT, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("nested",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_strict_nested_fetch() {
        StrictQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.joinFetch(qb.get(a.getNested()));

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.INNER, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.FETCH, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("nested",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }
    @Test
    public void test() {
    }
}
