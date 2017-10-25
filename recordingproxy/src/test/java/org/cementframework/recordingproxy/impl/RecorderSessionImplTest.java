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

import org.cementframework.recordingproxy.api.RecordedMethodCall;
import org.cementframework.recordingproxy.api.RecordingSessions;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.cementframework.recordingproxy.impl.testmodel.CollectionProperty;
import org.cementframework.recordingproxy.impl.testmodel.NestedNested;
import org.cementframework.recordingproxy.impl.testmodel.NestedProperty;
import org.cementframework.recordingproxy.impl.testmodel.OneProperty;
import org.cementframework.recordingproxy.impl.testmodel.SimpleCase;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class RecorderSessionImplTest extends AbstractSessionTestBase {

    @Test
    public void createProxy() {
        new RecordingSessions();

        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        Assert.assertNotNull(proxy);
        Assert.assertTrue(Enhancer.isEnhanced(proxy.getClass()));
    }

    @Test
    public void getFirstCall_0() {
        Assert.assertNull(RecordingSessions.get().getFirstCall());
        MethodCallUtils.proxy(SimpleCase.class);
        Assert.assertNull(RecordingSessions.get().getFirstCall());
        Assert.assertTrue(RecordingSessions.get().isEmpty());
    }

    @Test
    public void getFirstCall_1() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        String result = proxy.getText();
        RecordedMethodCall call = RecordingSessions.get().getFirstCall();
        Assert.assertNotNull(call);
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertEquals(
                "public java.lang.String org.cementframework.recordingproxy.impl.testmodel"
                + ".SimpleCase.getText()",
                call.toString());
        Assert.assertEquals("text", call.getName());
        Assert.assertSame(proxy, call.getInvokingProxy());
        Assert.assertNull(call.getParent());
        Assert.assertSame(String.class, call.getElementType());
        Assert.assertNull(result);
        Assert.assertNull(call.getResultingProxy());

        proxy.getValue();
        call = RecordingSessions.get().getFirstCall();
        Assert.assertNotNull(call);
        Assert.assertEquals("value", call.getName());

        // no more calls
        Assert.assertNull(RecordingSessions.get().getFirstCall());
    }

    @Test
    public void getFirstCall_nested() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);
        proxy.getNested();
        Assert.assertFalse(RecordingSessions.get().isEmpty());
        RecordingSessions.get().clear();
    }

    @Test
    public void getFirstCall_clear() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        proxy.getText();

        Assert.assertFalse(RecordingSessions.get().isEmpty());
        Assert.assertFalse(RecordingSessions.get().isEmpty());

        RecordingSessions.get().clear();

        Assert.assertTrue(RecordingSessions.get().isEmpty());
        Assert.assertTrue(RecordingSessions.get().isEmpty());
    }

    @Test
    public void getFirstCall_2() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        proxy.getText();
        proxy.getValue();
        RecordedMethodCall call = RecordingSessions.get().getFirstCall();
        Assert.assertFalse(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertEquals("text", call.getName());
        Assert.assertFalse(RecordingSessions.get().isEmpty());

        call = RecordingSessions.get().getFirstCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());
        Assert.assertNotNull(call);
        Assert.assertEquals("value", call.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void getFirstCall_0_safe() {
        MethodCallUtils.proxy(SimpleCase.class);
        Assert.assertTrue(RecordingSessions.get().isEmpty());
        RecordingSessions.get().getSafeFirstCall();
    }

    @Test
    public void getFirstCall_1_safe() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        proxy.getText();

        Assert.assertFalse(RecordingSessions.get().isEmpty());
        Assert.assertNotNull(RecordingSessions.get().getSafeFirstCall());
        Assert.assertTrue(RecordingSessions.get().isEmpty());
    }

    @Test
    public void getLastCall_0() {
        Assert.assertNull(RecordingSessions.get().getLastCall());
        MethodCallUtils.proxy(SimpleCase.class);
        Assert.assertNull(RecordingSessions.get().getLastCall());
    }

    @Test
    public void getLastCall_1() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        proxy.getText();
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertNotNull(call);
        Assert.assertEquals("text", call.getName());
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        proxy.getValue();
        call = RecordingSessions.get().getLastCall();
        Assert.assertNotNull(call);
        Assert.assertEquals("value", call.getName());

        // no more calls
        Assert.assertNull(RecordingSessions.get().getFirstCall());
    }

    @Test
    public void getLastCall_2() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        proxy.getText();
        proxy.getValue();
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertNotNull(call);
        Assert.assertEquals("value", call.getName());

        call = RecordingSessions.get().getLastCall();
        Assert.assertNotNull(call);
        Assert.assertEquals("text", call.getName());
    }

    @Test
    public void getLastCall_nested_1() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);

        OneProperty result = proxy.getProp();
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertSame(proxy, call.getInvokingProxy());
        Assert.assertEquals("prop", call.getName());
        Assert.assertEquals("getProp", call.getMethod().getName());

        Assert.assertNull(call.getParent());
        Assert.assertEquals(OneProperty.class, call.getElementType());
        Assert.assertEquals("", MethodCallUtils.nestedPath(call));
        Assert.assertEquals(result.toString(), call.getResultingProxy().toString());
    }

    @Test
    public void getLastCall_nested_property_1() {
        NestedProperty proxy = MethodCallUtils.proxy(NestedProperty.class);

        proxy.getProp().getText();
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertEquals("text", call.getName());
        Assert.assertEquals("getText", call.getMethod().getName());
        Assert.assertEquals(proxy.getProp().toString(), call.getInvokingProxy().toString());
        Assert.assertEquals("prop", call.getParent().getName());
        Assert.assertNull(call.getParent().getParent());
        Assert.assertEquals("prop.", MethodCallUtils.nestedPath(call));
        Assert.assertEquals(String.class, call.getElementType());
        Assert.assertNull(call.getResultingProxy());

        // engage the proxy cache
        proxy.getProp().getText();
        call = RecordingSessions.get().getLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertEquals("text", call.getName());
    }

    @Test
    public void getLastCall_nested_nested_1() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);

        OneProperty result = proxy.getNested().getProp();
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertEquals(OneProperty.class, call.getElementType());
        Assert.assertEquals("prop", call.getName());
        Assert.assertEquals("nested", call.getParent().getName());
        Assert.assertNull(call.getParent().getParent());
        Assert.assertEquals("nested.", MethodCallUtils.nestedPath(call));
        Assert.assertEquals(result.toString(), call.getResultingProxy().toString());
    }


    @Test
    public void getLastCall_nested_nested_property_1() {
        NestedNested proxy = MethodCallUtils.proxy(NestedNested.class);

        proxy.getNested().getProp().getText();
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertEquals(String.class, call.getElementType());
        Assert.assertEquals("text", call.getName());
        Assert.assertEquals("prop", call.getParent().getName());
        Assert.assertEquals("nested", call.getParent().getParent().getName());
        Assert.assertNull(call.getParent().getParent().getParent());
        Assert.assertEquals("nested.prop.", MethodCallUtils.nestedPath(call));
        Assert.assertNull(call.getResultingProxy());
    }

    @Test
    public void getLastCall_collection_1() {
        CollectionProperty proxy = MethodCallUtils.proxy(CollectionProperty.class);
        List<String> items;
        try {
            items = proxy.getItems();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        RecordedMethodCall call = RecordingSessions.get().getLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(call);
        Assert.assertEquals("items", call.getName());
        Assert.assertEquals("getItems", call.getMethod().getName());
        Assert.assertSame(proxy, call.getInvokingProxy());
        Assert.assertNull(call.getParent());
        Assert.assertEquals(String.class, call.getElementType());
        Assert.assertNull(items);
        Assert.assertNull(call.getResultingProxy());
    }


    @Test(expected = IllegalStateException.class)
    public void getLastCall_0_safe() {
        MethodCallUtils.proxy(SimpleCase.class);
        RecordedMethodCall call = RecordingSessions.get().getSafeLastCall();
        Assert.fail("Unexpected: " + call.getName());
    }

    @Test
    public void getLastCall_1_safe() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        proxy.getText();

        Assert.assertNotNull(RecordingSessions.get().getSafeLastCall());
        Assert.assertTrue(RecordingSessions.get().isEmpty());
    }
}
