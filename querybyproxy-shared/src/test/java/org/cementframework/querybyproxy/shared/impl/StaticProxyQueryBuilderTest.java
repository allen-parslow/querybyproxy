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

import static org.cementframework.querybyproxy.shared.impl.StaticProxyQueryBuilder.*;

import junit.framework.Assert;

import net.sf.cglib.proxy.Enhancer;

import org.cementframework.querybyproxy.shared.api.ProxyQuerySessions;
import org.cementframework.querybyproxy.shared.api.model.conditionals.Conditional;
import org.cementframework.querybyproxy.shared.api.model.conditionals.LogicGate;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BinaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.ComparisonOperator;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.GroupConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxyPathExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxySelectExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryParameter;
import org.cementframework.querybyproxy.shared.impl.testmodel.NestedProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.OneProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class StaticProxyQueryBuilderTest {

    @Test
    public void qb_literal() {
        QueryLiteral<Integer> literal = (QueryLiteral<Integer>) literal(1);
        Assert.assertEquals(1, literal.getTarget());
    }

    @Test
    public void qb_param() {
        QueryParameter<Integer> literal = (QueryParameter<Integer>) param(1);
        Assert.assertEquals(1, literal.getTarget());
    }

    @Test
    public void qb_param_name() {
        QueryParameter<Integer> literal = (QueryParameter<Integer>)
                        param("xyz", Integer.class);
        Assert.assertEquals(null, literal.getTarget());
        Assert.assertEquals("xyz", literal.getName());
    }

    @Test
    public void qb_createProxy() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        Assert.assertNotNull(proxy);
        Assert.assertTrue(Enhancer.isEnhanced(proxy.getClass()));
    }

    @Test
    public void qb_get_simple_property() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        QueryValue<String> actual = get(proxy.getText());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<String> property = (ProxyPathExpressionImpl<String>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test
    public void get_class() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        QueryValue<Integer> actual = get(proxy.getText(), Integer.class);

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<Integer> property = (ProxyPathExpressionImpl<Integer>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test
    public void qb_get_nested_property() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);
        QueryValue<String> actual = get(proxy.getProp().getText());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<String> property = (ProxyPathExpressionImpl<String>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("prop.", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test
    public void qb_get_nested_proxy() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);
        QueryValue<OneProperty> actual = get(proxy.getProp());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<OneProperty> property = (ProxyPathExpressionImpl<OneProperty>) actual;
        Assert.assertEquals("prop", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(OneProperty.class, property.getCall().getElementType());
    }

    @Test
    public void qb_get_proxy() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);
        QueryValue<NestedProperty> actual = get(proxy);

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxySelectExpressionImpl.class, actual.getClass());
        ProxySelectExpressionImpl<NestedProperty> property = (ProxySelectExpressionImpl<NestedProperty>) actual;
        Assert.assertNotNull(property.getProxy());
    }

    @Test
    public void qb_createQueryValue_literal() {
        QueryValue<Integer> actual = createQueryValue(1);

        Assert.assertNotNull(actual);
        Assert.assertEquals(QueryLiteral.class, actual.getClass());
        QueryLiteral<Integer> property = (QueryLiteral<Integer>) actual;
        Assert.assertEquals(1, property.getTarget());
    }

    @Test
    public void qb_createQueryValue_queryvalue() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        QueryValue<String> actual = createQueryValue(proxy.getText());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<String> property = (ProxyPathExpressionImpl<String>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void qb_createQueryValue_proxy() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        createQueryValue(proxy);
    }

    @Test
    public void qb_createQueryValue_value() {
        QueryValue<Integer> actual = createQueryValue(1);

        Assert.assertNotNull(actual);
        Assert.assertEquals(QueryLiteral.class, actual.getClass());
        QueryLiteral<Integer> property = (QueryLiteral<Integer>) actual;
        Assert.assertEquals(1, property.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_or() {
        Assert.assertEquals(LogicGate.AND, ProxyQuerySessions.get().getNextLogicGate());
        Assert.assertNull(or());

        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        BinaryConditionalImpl<String> predicate = (BinaryConditionalImpl) get(proxy.getText()).equalTo(literal("x"));
        Assert.assertEquals(LogicGate.OR, predicate.getLogicGate());

        Assert.assertEquals(LogicGate.AND, ProxyQuerySessions.get().getNextLogicGate());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_group() {

        Conditional conditional = group(literal(1L).equalTo(literal(2L)));
        GroupConditionalImpl group = (GroupConditionalImpl) conditional;

        Assert.assertEquals(LogicGate.AND, group.getLogicGate());
        Assert.assertEquals(1, group.getConditionalExpressions().size());
        BinaryConditionalImpl predicate = (BinaryConditionalImpl)
                group.getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, predicate.getLogicGate());

        QueryLiteral value1 = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals(1L, value1.getTarget());

        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());

        QueryLiteral value2 = (QueryLiteral) predicate.getRightValue();
        Assert.assertEquals(2L, value2.getTarget());
    }

    @Test
    public void qb() {
        new StaticProxyQueryBuilder();
    }

}
