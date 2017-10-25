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

import junit.framework.Assert;

import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoinModifier;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoinType;
import org.cementframework.querybyproxy.shared.impl.model.joins.PathJoinImpl;
import org.cementframework.querybyproxy.shared.impl.model.joins.ThetaJoinImpl;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionItem;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.NestedNested;
import org.cementframework.querybyproxy.shared.impl.testmodel.NestedProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.OneProperty;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class TerseFromTest extends AbstractSessionTestBase {

    @Test
    public void join_terse_nested() {
        ProxyQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.join(a.getNested());

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
    public void join_terse_nested_left() {
        ProxyQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.leftJoin(a.getNested());

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
    public void join_terse_nested_right() {
        ProxyQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.rightJoin(a.getNested());

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
    public void join_terse_nested_fetch() {
        ProxyQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        NestedProperty joined = query.joinFetch(a.getNested());

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
    public void join_terse_nested_nested() {
        ProxyQuery<NestedNested> query = queryFactory.createQuery(NestedNested.class);
        NestedNested a = query.getRootProxy();

        OneProperty joined = query.join(a.getNested().getProp());

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.INNER, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("nested.prop",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_terse_collection() {
        ProxyQuery<CollectionProperty> query = queryFactory.createQuery(CollectionProperty.class);
        CollectionProperty a = query.getRootProxy();

        CollectionItem joined = query.join(a.getItems());

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.INNER, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("items",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_terse_collection_fetch() {
        ProxyQuery<CollectionProperty> query = queryFactory.createQuery(CollectionProperty.class);
        CollectionProperty a = query.getRootProxy();

        CollectionItem joined = query.joinFetch(a.getItems());

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.INNER, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.FETCH, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("items",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_terse_collection_left() {
        ProxyQuery<CollectionProperty> query = queryFactory.createQuery(CollectionProperty.class);
        CollectionProperty a = query.getRootProxy();

        CollectionItem joined = query.leftJoin(a.getItems());

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.LEFT, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("items",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_terse_collection_right() {
        ProxyQuery<CollectionProperty> query = queryFactory.createQuery(CollectionProperty.class);
        CollectionProperty a = query.getRootProxy();

        CollectionItem joined = query.rightJoin(a.getItems());

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        PathJoinImpl join = (PathJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.RIGHT, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Object root = MethodCallUtils.findRoot(join.getCall());
        Assert.assertSame(a, root);

        // path from identification variable
        Assert.assertEquals("items",
                MethodCallUtils.path(join.getCall()));

        // need for identification variable
        Assert.assertEquals(joined, join.getProxy());
    }

    @Test
    public void join_terse_theta() {
        ProxyQuery<OneProperty> query = queryFactory.createQuery(OneProperty.class);

        OneProperty b = query.thetaJoin(OneProperty.class);

        Assert.assertEquals(2, query.getFrom().getJoins().size());

        ThetaJoinImpl join = (ThetaJoinImpl) query.getFrom().getJoins().get(1);
        Assert.assertEquals(QueryJoinType.THETA, join.getJoinType());
        Assert.assertEquals(QueryJoinModifier.NONE, join.getJoinModifier());

        // need for identification variable
        Assert.assertEquals(OneProperty.class, join.getJoinClass());

        // need for identification variable
        Assert.assertEquals(b, join.getJoinProxy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void join_terse_theta_null() {
        ProxyQuery<OneProperty> query = queryFactory.createQuery(OneProperty.class);

        query.thetaJoin(null);
    }

    @Test
    public void test() {
    }

}
