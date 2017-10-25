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

import junit.framework.Assert;

import org.cementframework.recordingproxy.api.RecordedMethodCall;
import org.cementframework.recordingproxy.api.RecordingSessions;
import org.cementframework.recordingproxy.impl.testmodel.NestedNested;
import org.cementframework.recordingproxy.impl.testmodel.NestedProperty;
import org.cementframework.recordingproxy.impl.testmodel.OneProperty;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class PathingTest extends AbstractSessionTestBase {
    @Test
    public void test_paths_OneProperty() {
        OneProperty a = MethodCallUtils.proxy(OneProperty.class);

        Assert.assertNull(a.getText());
        RecordedMethodCall call1 = RecordingSessions.get().getSafeFirstCall();

        Assert.assertNull(a.getText());
        RecordedMethodCall call2 = RecordingSessions.get().getSafeFirstCall();

        Assert.assertEquals(call1.getName(), call2.getName());

        Assert.assertNotNull(call1.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), call2.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), a);

        Assert.assertNull(call1.getResultingProxy());
        Assert.assertNull(call2.getResultingProxy());
    }

    @Test
    public void test_paths_NestedNested_level_1() {
        NestedNested a = MethodCallUtils.proxy(NestedNested.class);

        NestedProperty prop1 = a.getNested();
        RecordedMethodCall call1 = RecordingSessions.get().getSafeFirstCall();

        NestedProperty prop2 = a.getNested();
        RecordedMethodCall call2 = RecordingSessions.get().getSafeFirstCall();

        Assert.assertNotNull(prop1);
        Assert.assertEquals(prop1, prop2);

        Assert.assertEquals(call1.getName(), call2.getName());

        Assert.assertNotNull(call1.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), call2.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), a);

        Assert.assertNotNull(call1.getResultingProxy());
        Assert.assertEquals(call1.getResultingProxy(), call2.getResultingProxy());
        Assert.assertEquals(call1.getResultingProxy(), prop1);
    }

    @Test
    public void test_paths_NestedNested_level_2_test_1() {
        NestedNested a = MethodCallUtils.proxy(NestedNested.class);

        NestedProperty b = a.getNested();
        OneProperty prop1 = b.getProp();
        RecordedMethodCall call1 = RecordingSessions.get().getSafeLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        OneProperty prop2 = a.getNested().getProp();
        RecordedMethodCall call2 = RecordingSessions.get().getSafeLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(prop1);
        Assert.assertEquals(prop1, prop2);

        Assert.assertEquals(call1.getName(), call2.getName());

        Assert.assertNotNull(call1.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), call2.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), b);

        Assert.assertNotNull(call1.getResultingProxy());
        Assert.assertEquals(call1.getResultingProxy(), call2.getResultingProxy());
        Assert.assertNotNull(call1.getParent());
        Assert.assertEquals(call1.getResultingProxy().toString(), prop1.toString());
    }

    @Test
    public void test_paths_NestedNested_level_2_test_2() {
        NestedNested a = MethodCallUtils.proxy(NestedNested.class);

        OneProperty prop1 = a.getNested().getProp();
        RecordedMethodCall call1 = RecordingSessions.get().getSafeLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        OneProperty prop2 = a.getNested().getProp();
        RecordedMethodCall call2 = RecordingSessions.get().getSafeLastCall();
        Assert.assertTrue(RecordingSessions.get().isEmpty());

        Assert.assertNotNull(prop1);
        Assert.assertEquals(prop1, prop2);

        Assert.assertEquals(call1.getName(), call2.getName());

        Assert.assertNotNull(call1.getInvokingProxy());
        Assert.assertEquals(call1.getInvokingProxy(), call2.getInvokingProxy());
        Assert.assertFalse(call1.getInvokingProxy().equals(a));
        Assert.assertTrue(call1.getInvokingProxy() instanceof NestedProperty);

        Assert.assertNotNull(call1.getResultingProxy());
        Assert.assertEquals(call1.getResultingProxy(), call2.getResultingProxy());
        Assert.assertNotNull(call1.getParent());
        Assert.assertEquals(call1.getResultingProxy().toString(), prop1.toString());
    }
}
