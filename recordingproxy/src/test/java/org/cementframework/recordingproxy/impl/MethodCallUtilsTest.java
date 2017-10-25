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
package org.cementframework.recordingproxy.impl;

import java.util.List;

import junit.framework.Assert;
import net.sf.cglib.proxy.Enhancer;

import org.cementframework.recordingproxy.api.PropertyMetaData;
import org.cementframework.recordingproxy.api.RecordedMethodCall;
import org.cementframework.recordingproxy.api.RecordingSessions;
import org.cementframework.recordingproxy.impl.testmodel.EqualityTestingMethodInterceptor;
import org.cementframework.recordingproxy.impl.testmodel.FieldAndReadMethodAnnotation;
import org.cementframework.recordingproxy.impl.testmodel.FieldAnnotationAndReadMethod;
import org.cementframework.recordingproxy.impl.testmodel.NestedNested;
import org.cementframework.recordingproxy.impl.testmodel.NoReadMethodClass;
import org.cementframework.recordingproxy.impl.testmodel.ReadMethodOnly;
import org.cementframework.recordingproxy.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.testmodel.SomeAnnotation;
import org.cementframework.recordingproxy.impl.testmodel.SuperField;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class MethodCallUtilsTest extends AbstractSessionTestBase {

    @Test(expected = IllegalStateException.class)
    public void getReadableProperties_null() throws Exception {
        MethodCallUtils.getReadableProperties(null);
    }

    @Test
    public void getReadableProperties() throws Exception {

        new MethodCallUtils();

        List<PropertyMetaData> result;
        PropertyMetaData prop;

        result = MethodCallUtils.getReadableProperties(Object.class);
        Assert.assertEquals(0, result.size());

        result = MethodCallUtils.getReadableProperties(NoReadMethodClass.class);
        Assert.assertEquals(0, result.size());

        result = MethodCallUtils.getReadableProperties(ReadMethodOnly.class);
        Assert.assertEquals(1, result.size());
        prop = result.get(0);
        Assert.assertEquals("text", prop.getName());
        Assert.assertNotNull(prop.getReadMethod());
        Assert.assertNull(prop.getAnnotation(SomeAnnotation.class));
        Assert.assertFalse(prop.isAnnotationPresent(SomeAnnotation.class));

        result = MethodCallUtils.getReadableProperties(FieldAnnotationAndReadMethod.class);
        Assert.assertEquals(1, result.size());
        prop = result.get(0);
        Assert.assertEquals("text", prop.getName());
        Assert.assertNotNull(prop.getReadMethod());
        Assert.assertNotNull(prop.getAnnotation(SomeAnnotation.class));
        Assert.assertTrue(prop.isAnnotationPresent(SomeAnnotation.class));

        result = MethodCallUtils.getReadableProperties(FieldAndReadMethodAnnotation.class);
        Assert.assertEquals(1, result.size());
        prop = result.get(0);
        Assert.assertEquals("text", prop.getName());
        Assert.assertNotNull(prop.getReadMethod());
        Assert.assertNotNull(prop.getAnnotation(SomeAnnotation.class));
        Assert.assertTrue(prop.isAnnotationPresent(SomeAnnotation.class));

        result = MethodCallUtils.getReadableProperties(SuperField.class);
        Assert.assertEquals(2, result.size());
        prop = result.get(0);
        Assert.assertEquals("anotherField", prop.getName());
        prop = result.get(1);
        Assert.assertEquals("text", prop.getName());
    }

    @Test
    public void isProxy() {
        Assert.assertFalse(MethodCallUtils.isProxy(null));
        Assert.assertFalse(MethodCallUtils.isProxy(new MethodCallUtilsTest()));

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SimpleCase.class);
        enhancer.setCallback(new EqualityTestingMethodInterceptor());
        SimpleCase proxyInstance1 = (SimpleCase) enhancer.create();
        Assert.assertTrue(MethodCallUtils.isProxy(proxyInstance1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIsProxy_null() {
        MethodCallUtils.validateIsProxy(null, "bad");
    }

    @Test(expected = IllegalStateException.class)
    public void validateIsProxy_not() {
        MethodCallUtils.validateIsProxy(new MethodCallUtilsTest(), "bad");
    }

    @Test
    public void validateIsProxy_is() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SimpleCase.class);
        enhancer.setCallback(new EqualityTestingMethodInterceptor());
        SimpleCase proxyInstance1 = (SimpleCase) enhancer.create();
        MethodCallUtils.validateIsProxy(proxyInstance1, "bad");
    }

    @Test
    public void proxy() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        Assert.assertTrue(MethodCallUtils.isProxy(proxy));
    }

    @Test
    public void proxy_nested_path() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        Assert.assertTrue(MethodCallUtils.isProxy(proxy));
    }

    @Test
    public void methodName() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        Assert.assertEquals("text", MethodCallUtils.methodName(proxy.getText()));
    }

    @Test
    public void method() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        Assert.assertEquals("getText", MethodCallUtils.method(proxy.getText()).getName());
    }

    @Test
    public void listParentCalls_0() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        proxy.getText();

        List<RecordedMethodCall> calls  = MethodCallUtils.listParentCalls(
                RecordingSessions.get().getLastCall());

        Assert.assertEquals(0, calls.size());

        Assert.assertEquals("",
                MethodCallUtils.nestedPath(calls));
    }

    @Test
    public void listParentCalls_1() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested().getProp();

        List<RecordedMethodCall> calls  = MethodCallUtils.listParentCalls(
                RecordingSessions.get().getLastCall());

        Assert.assertEquals(1, calls.size());
        RecordedMethodCall call;

        call = calls.get(0);
        Assert.assertEquals("nested", call.getName());

        Assert.assertEquals("nested.",
                MethodCallUtils.nestedPath(calls));
    }

    @Test
    public void listParentCalls_2() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested().getProp().getText();

        List<RecordedMethodCall> calls  = MethodCallUtils.listParentCalls(
                RecordingSessions.get().getLastCall());

        Assert.assertEquals(2, calls.size());
        RecordedMethodCall call;

        call = calls.get(0);
        Assert.assertEquals("nested", call.getName());

        call = calls.get(1);
        Assert.assertEquals("prop", call.getName());

        Assert.assertEquals("nested.prop.",
                MethodCallUtils.nestedPath(calls));
    }


    @Test
    public void listCalls_0() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        proxy.getText();

        RecordedMethodCall proxyCall = RecordingSessions.get().getLastCall();
        List<RecordedMethodCall> calls  = MethodCallUtils.listCalls(proxyCall);

        Assert.assertEquals(1, calls.size());
        RecordedMethodCall call;

        call = calls.get(0);
        Assert.assertEquals("text", call.getName());

        Assert.assertEquals("text",
                MethodCallUtils.path(calls));
        Assert.assertEquals("text",
                MethodCallUtils.path(proxyCall));
    }

    @Test
    public void listCalls_1() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested().getProp();

        RecordedMethodCall proxyCall = RecordingSessions.get().getLastCall();
        List<RecordedMethodCall> calls  = MethodCallUtils.listCalls(proxyCall);

        Assert.assertEquals(2, calls.size());
        RecordedMethodCall call;

        call = calls.get(0);
        Assert.assertEquals("nested", call.getName());

        call = calls.get(1);
        Assert.assertEquals("prop", call.getName());

        Assert.assertEquals("nested.prop",
                MethodCallUtils.path(calls));
        Assert.assertEquals("nested.prop",
                MethodCallUtils.path(proxyCall));
    }

    @Test
    public void listCalls_2() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested().getProp().getText();

        RecordedMethodCall proxyCall = RecordingSessions.get().getLastCall();
        List<RecordedMethodCall> calls  = MethodCallUtils.listCalls(proxyCall);

        Assert.assertEquals(3, calls.size());
        RecordedMethodCall call;

        call = calls.get(0);
        Assert.assertEquals("nested", call.getName());

        call = calls.get(1);
        Assert.assertEquals("prop", call.getName());

        call = calls.get(2);
        Assert.assertEquals("text", call.getName());

        Assert.assertEquals("nested.prop.text",
                MethodCallUtils.path(calls));
        Assert.assertEquals("nested.prop.text",
                MethodCallUtils.path(proxyCall));
    }

    @Test
    public void findRoot() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        proxy.getText();

        Object root = MethodCallUtils.findRoot(
                RecordingSessions.get().getLastCall());

        Assert.assertEquals(proxy, root);
    }

    @Test
    public void findRoot_nested() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested();

        Object root = MethodCallUtils.findRoot(
                RecordingSessions.get().getLastCall());

        Assert.assertEquals(proxy, root);
    }

    @Test
    public void findRoot_nested_nested() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested().getProp();

        Object root = MethodCallUtils.findRoot(
                RecordingSessions.get().getLastCall());

        Assert.assertEquals(proxy, root);
    }
}
