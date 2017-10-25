package org.cementframework.querybyproxy.shared.impl;

import junit.framework.Assert;
import net.sf.cglib.proxy.Enhancer;

import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.values.QueryValue;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxyPathExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxySelectExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.testmodel.NestedProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.OneProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Test;

public class ProxyQueryBuilderImpl_get_Test extends AbstractProxyQueryImplTest {
    ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

    @Test
    public void createProxy() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        Assert.assertNotNull(proxy);
        Assert.assertTrue(Enhancer.isEnhanced(proxy.getClass()));
    }

    @Test
    public void get_simple_property() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        QueryValue<String> actual = qb.get(proxy.getText());

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
        QueryValue<Integer> actual = qb.get(proxy.getText(), Integer.class);

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<Integer> property = (ProxyPathExpressionImpl<Integer>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test
    public void get_nested_property() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);
        QueryValue<String> actual = qb.get(proxy.getProp().getText());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<String> property = (ProxyPathExpressionImpl<String>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("prop.", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test
    public void get_nested_proxy() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);
        QueryValue<OneProperty> actual = qb.get(proxy.getProp());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<OneProperty> property = (ProxyPathExpressionImpl<OneProperty>) actual;
        Assert.assertEquals("prop", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(OneProperty.class, property.getCall().getElementType());
    }

    @Test
    public void get_proxy() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);
        QueryValue<NestedProperty> actual = qb.get(proxy);

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxySelectExpressionImpl.class, actual.getClass());
        ProxySelectExpressionImpl<NestedProperty> property = (ProxySelectExpressionImpl<NestedProperty>) actual;
        Assert.assertNotNull(property.getProxy());
    }

    @Test
    public void createQueryValue_literal() {
        QueryValue<Integer> actual = qb.createQueryValue(1);

        Assert.assertNotNull(actual);
        Assert.assertEquals(QueryLiteral.class, actual.getClass());
        QueryLiteral<Integer> property = (QueryLiteral<Integer>) actual;
        Assert.assertEquals(1, property.getTarget());
    }

    @Test
    public void createQueryValue_queryvalue() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        QueryValue<String> actual = qb.createQueryValue(proxy.getText());

        Assert.assertNotNull(actual);
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getClass());
        ProxyPathExpressionImpl<String> property = (ProxyPathExpressionImpl<String>) actual;
        Assert.assertEquals("text", property.getCall().getName());
        Assert.assertEquals("", MethodCallUtils.nestedPath(property.getCall()));
        Assert.assertEquals(String.class, property.getCall().getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createQueryValue_proxy() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        qb.createQueryValue(proxy);
    }

    @Test
    public void createQueryValue_value() {
        QueryValue<Integer> actual = qb.createQueryValue(1);

        Assert.assertNotNull(actual);
        Assert.assertEquals(QueryLiteral.class, actual.getClass());
        QueryLiteral<Integer> property = (QueryLiteral<Integer>) actual;
        Assert.assertEquals(1, property.getTarget());
    }
    @Test
    public void test() {
    }
}
