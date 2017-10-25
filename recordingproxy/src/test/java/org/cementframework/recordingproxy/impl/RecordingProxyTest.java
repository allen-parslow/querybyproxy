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

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;

import org.cementframework.recordingproxy.api.RecordingSessions;
import org.cementframework.recordingproxy.impl.testmodel.EqualityTestingMethodInterceptor;
import org.cementframework.recordingproxy.impl.testmodel.FringeCases;
import org.cementframework.recordingproxy.impl.testmodel.SimpleCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class RecordingProxyTest {

    @Test
    public void test_toString() {
        RecordingProxy proxy = new RecordingProxy(SimpleCase.class);
        Assert.assertEquals("SimpleCase", proxy.toString());
    }

    @Test
    public void intercept() throws Throwable {
        RecordingProxy recordingProxy = new RecordingProxy(SimpleCase.class);
        Object proxy = recordingProxy.getProxy();

        Method method;
        Object actual;

        method = SimpleCase.class.getMethod("hashCode", new Class[0]);
        actual = recordingProxy.intercept(recordingProxy, method, new Object[] {}, null);
        Assert.assertEquals(recordingProxy.hashCode(), actual);
        Assert.assertNull(RecordingSessions.get().getFirstCall());

        method = SimpleCase.class.getMethod("toString", new Class[0]);
        actual = recordingProxy.intercept(recordingProxy, method, new Object[] {}, null);
        Assert.assertEquals(proxy.toString(), actual);
        Assert.assertNull(RecordingSessions.get().getFirstCall());

        method = SimpleCase.class.getMethod("equals", new Class[] { Object.class });
        actual = recordingProxy.intercept(recordingProxy, method, new Object[] { proxy }, null);
        Assert.assertEquals(true, actual);
        Assert.assertNull(RecordingSessions.get().getFirstCall());

        method = SimpleCase.class.getMethod("setText", new Class[] { String.class });
        actual = recordingProxy.intercept(recordingProxy, method, new Object[] { null }, null);
        Assert.assertNull(actual);
        Assert.assertNull(RecordingSessions.get().getFirstCall());

        method = SimpleCase.class.getMethod("getText", new Class[] {});
        actual = recordingProxy.intercept(recordingProxy, method, new Object[] {}, null);
        Assert.assertNull(actual);
        Assert.assertEquals("text", RecordingSessions.get().getFirstCall().getName());
    }

    @Test
    public void intercept_FringeCases() throws Throwable {
        RecordingProxy recordingProxy = new RecordingProxy(FringeCases.class);

        Method method;
        Object actual;

        method = FringeCases.class.getMethod("equals", new Class[] {});
        actual = recordingProxy.intercept(recordingProxy, method, new Object[] {}, null);
        Assert.assertNull(actual);
        Assert.assertNull(RecordingSessions.get().getFirstCall());

        method = FringeCases.class.getMethod("equals", new Class[] { Object.class, Object.class });
        actual = recordingProxy.intercept(
                recordingProxy,
                method,
                new Object[] { null, null },
                null);
        Assert.assertNull(actual);
        Assert.assertNull(RecordingSessions.get().getFirstCall());
    }

    @Test
    public void test_equal_hashcode_tostring___with_TestInterceptor() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Object.class);
        enhancer.setCallback(new EqualityTestingMethodInterceptor());
        Object proxyInstance1 = enhancer.create();

        Enhancer enhancer2 = new Enhancer();
        enhancer2.setSuperclass(Object.class);
        enhancer2.setCallback(new EqualityTestingMethodInterceptor());
        Object proxyInstance2 = enhancer2.create();

        equality_test(proxyInstance1, proxyInstance2);
    }

    private void equality_test(Object proxyInstance1, Object proxyInstance2) {
        String toString = proxyInstance1.toString();
        Assert.assertNotNull(toString);
        // repeatable
        Assert.assertEquals(toString, proxyInstance1.toString());

        // different from another instance
        Assert.assertFalse(toString.equals(proxyInstance2.toString()));

        // equality to self
        Assert.assertTrue(proxyInstance1.equals(proxyInstance1));

        // different from another instance
        Assert.assertFalse(proxyInstance1.equals(proxyInstance2));
        Assert.assertFalse(proxyInstance2.equals(proxyInstance1));

        // a non-proxied instances
        Assert.assertFalse(proxyInstance1.equals(new RecordingProxyTest()));

        // repeatable hashcode
        Assert.assertTrue(proxyInstance1.hashCode() == proxyInstance1.hashCode());
    }

}
