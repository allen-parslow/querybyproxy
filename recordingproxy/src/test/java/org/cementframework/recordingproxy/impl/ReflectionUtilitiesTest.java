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
/**
 *
 */
package org.cementframework.recordingproxy.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.cementframework.recordingproxy.impl.testmodel.FieldAnnotationAndReadMethod;
import org.cementframework.recordingproxy.impl.testmodel.FinalField;
import org.cementframework.recordingproxy.impl.testmodel.ImmutableCase;
import org.cementframework.recordingproxy.impl.testmodel.ReadMethodOnly;
import org.cementframework.recordingproxy.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.testmodel.SuperEmptyParentField;
import org.cementframework.recordingproxy.impl.testmodel.SuperField;
import org.cementframework.recordingproxy.impl.testmodel.SuperFinalField;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class ReflectionUtilitiesTest {

    private static final Class<?>[] ARGS = new Class[0];

    @Test
    public void test() throws Exception {
        new ReflectionUtilities();
    }

    @Test
    public void isObjectMethod() throws Exception {

        Assert.assertFalse(ReflectionUtilities.isObjectMethod(null));
        Assert.assertTrue(ReflectionUtilities.isObjectMethod(
                Object.class.getMethod("getClass", ARGS)));
        Assert.assertTrue(ReflectionUtilities.isObjectMethod(
                String.class.getMethod("toString", ARGS)));
        Assert.assertFalse(ReflectionUtilities.isObjectMethod(
                String.class.getMethod("intern", ARGS)));
    }

    @Test
    public void getFieldMap() throws Exception {
        Map<String, Field> fieldMap;

        fieldMap = ReflectionUtilities.getFieldMap(null);
        Assert.assertEquals(0, fieldMap.size());

        fieldMap = ReflectionUtilities.getFieldMap(ReadMethodOnly.class);
        Assert.assertEquals(0, fieldMap.size());

        fieldMap = ReflectionUtilities.getFieldMap(FieldAnnotationAndReadMethod.class);
        Assert.assertEquals(1, fieldMap.size());
        Assert.assertNotNull(fieldMap.get("text"));

        fieldMap = ReflectionUtilities.getFieldMap(SuperField.class);
        Assert.assertEquals(2, fieldMap.size());
        Assert.assertNotNull(fieldMap.get("text"));
        Assert.assertNotNull(fieldMap.get("anotherField"));

        fieldMap = new HashMap<String, Field>();
        ReflectionUtilities.populateFieldMap(int.class, fieldMap);
        Assert.assertEquals(0, fieldMap.size());

        fieldMap = new HashMap<String, Field>();
        ReflectionUtilities.populateFieldMap(Object.class, fieldMap);
        Assert.assertEquals(0, fieldMap.size());
    }

    @Test
    public void isImmutableCheck() throws Exception {
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(boolean.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(byte.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(short.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(int.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(long.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(long.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(double.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(float.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(char.class));

        Assert.assertFalse(ReflectionUtilities.isImmutableCheck(SimpleCase.class));
        Assert.assertFalse(ReflectionUtilities.isImmutableCheck(SuperField.class));
        Assert.assertFalse(ReflectionUtilities.isImmutableCheck(SuperEmptyParentField.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(ImmutableCase.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(SuperFinalField.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(ReadMethodOnly.class));
        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(FinalField.class));

        Assert.assertTrue(ReflectionUtilities.isImmutableCheck(Object.class));
    }

    @Test
    public void isImmutable() throws Exception {
        Assert.assertTrue(ReflectionUtilities.isImmutable(boolean.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(byte.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(short.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(int.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(long.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(long.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(double.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(float.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(char.class));

        Assert.assertTrue(ReflectionUtilities.isImmutable(Boolean.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Byte.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Short.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Integer.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Long.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Double.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Character.class));

        Assert.assertTrue(ReflectionUtilities.isImmutable(String.class));

        Assert.assertTrue(ReflectionUtilities.isImmutable(BigDecimal.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(BigInteger.class));

        Assert.assertTrue(ReflectionUtilities.isImmutable(java.util.Date.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Calendar.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(java.sql.Date.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(java.sql.Timestamp.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(java.sql.Time.class));

        Assert.assertTrue(ReflectionUtilities.isImmutable(Object.class));
        Assert.assertTrue(ReflectionUtilities.isImmutable(Class.class));
    }
}
