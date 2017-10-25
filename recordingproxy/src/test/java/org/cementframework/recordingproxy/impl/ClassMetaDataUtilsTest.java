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
import java.lang.reflect.Modifier;

import junit.framework.Assert;

import org.cementframework.recordingproxy.api.ClassMetaData;
import org.cementframework.recordingproxy.api.PropertyMetaData;
import org.cementframework.recordingproxy.impl.testmodel.AliasedReadMethod;
import org.cementframework.recordingproxy.impl.testmodel.CollectionProperty;
import org.cementframework.recordingproxy.impl.testmodel.NestedProperty;
import org.cementframework.recordingproxy.impl.testmodel.NoReadMethodClass;
import org.cementframework.recordingproxy.impl.testmodel.OneProperty;
import org.cementframework.recordingproxy.impl.testmodel.SuperProperty;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class ClassMetaDataUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void getMetaData_null() {
        ClassMetaDataUtils.getMetaData(null);
    }

    @Test
    public void getMetaData_none() {

        new ClassMetaDataUtils();

        ClassMetaData data = ClassMetaDataUtils.getMetaData(NoReadMethodClass.class);

        Assert.assertEquals(NoReadMethodClass.class, data.getTargetClass());

        Assert.assertEquals(0, data.getProperties().size());
        Assert.assertEquals(0, data.getSingularProperties().size());
        Assert.assertEquals(0, data.getNestedProperties().size());
        Assert.assertEquals(0, data.getCollectionProperties().size());
        Assert.assertEquals(0, data.getAliasedPropertyMap().size());

        data = ClassMetaDataUtils.getMetaData(NoReadMethodClass.class);

        Assert.assertEquals(0, data.getProperties().size());
        Assert.assertEquals(0, data.getSingularProperties().size());
        Assert.assertEquals(0, data.getNestedProperties().size());
        Assert.assertEquals(0, data.getCollectionProperties().size());
        Assert.assertEquals(0, data.getAliasedPropertyMap().size());
    }

    @Test
    public void getMetaData_simple_1() throws Exception {
        ClassMetaData data = ClassMetaDataUtils.getMetaData(OneProperty.class);

        Assert.assertEquals(1, data.getProperties().size());
        Assert.assertEquals(1, data.getSingularProperties().size());
        Assert.assertEquals(0, data.getNestedProperties().size());
        Assert.assertEquals(0, data.getCollectionProperties().size());
        Assert.assertEquals(0, data.getAliasedPropertyMap().size());

        Method method = OneProperty.class.getMethod("getText", new Class[0]);
        PropertyMetaData property = data.getProperty(method);
        Assert.assertNotNull(property);
        Assert.assertEquals("text", property.getName());
        Assert.assertEquals("text", property.toString());
        Assert.assertTrue(Modifier.isPublic(property.getModifiers()));
        Assert.assertEquals(method, property.getReadMethod());

        Assert.assertEquals(true, data.isSingular(method));
        Assert.assertEquals(false, data.isNested(method));
        Assert.assertEquals(false, data.isCollection(method));
    }

    @Test
    public void getMetaData_nested_1() throws Exception {
        ClassMetaData data = ClassMetaDataUtils.getMetaData(NestedProperty.class);

        Assert.assertEquals(1, data.getProperties().size());
        Assert.assertEquals(0, data.getSingularProperties().size());
        Assert.assertEquals(1, data.getNestedProperties().size());
        Assert.assertEquals(0, data.getCollectionProperties().size());
        Assert.assertEquals(0, data.getAliasedPropertyMap().size());

        Method method = NestedProperty.class.getMethod("getProp", new Class[0]);
        PropertyMetaData property = data.getProperty(method);
        Assert.assertNotNull(property);

        Assert.assertEquals(false, data.isSingular(method));
        Assert.assertEquals(true, data.isNested(method));
        Assert.assertEquals(false, data.isCollection(method));
    }

    @Test
    public void getMetaData_collection_1() throws Exception {
        ClassMetaData data = ClassMetaDataUtils.getMetaData(CollectionProperty.class);

        Assert.assertEquals(1, data.getProperties().size());
        Assert.assertEquals(0, data.getSingularProperties().size());
        Assert.assertEquals(0, data.getNestedProperties().size());
        Assert.assertEquals(1, data.getCollectionProperties().size());
        Assert.assertEquals(0, data.getAliasedPropertyMap().size());

        Method method = CollectionProperty.class.getMethod("getItems", new Class[0]);
        PropertyMetaData property = data.getProperty(method);
        Assert.assertNotNull(property);

        Assert.assertEquals(false, data.isSingular(method));
        Assert.assertEquals(false, data.isNested(method));
        Assert.assertEquals(true, data.isCollection(method));
    }

    @Test
    public void getMetaData_SuperProperty_1() {
        ClassMetaData data = ClassMetaDataUtils.getMetaData(SuperProperty.class);

        Assert.assertEquals(1, data.getProperties().size());
        Assert.assertEquals(1, data.getSingularProperties().size());
        Assert.assertEquals(0, data.getNestedProperties().size());
        Assert.assertEquals(0, data.getCollectionProperties().size());
        Assert.assertEquals(0, data.getAliasedPropertyMap().size());
    }

    @Test
    public void getMetaData_AliasedReadMethod_1() throws Exception {
        ClassMetaData data = ClassMetaDataUtils.getMetaData(AliasedReadMethod.class);

        Assert.assertEquals(1, data.getProperties().size());
        Assert.assertEquals(1, data.getSingularProperties().size());
        Assert.assertEquals(0, data.getNestedProperties().size());
        Assert.assertEquals(0, data.getCollectionProperties().size());
        Assert.assertEquals(1, data.getAliasedPropertyMap().size());

        Method method = AliasedReadMethod.class.getMethod("getText", new Class[0]);
        Assert.assertEquals("an-alias", data.getName(method));
        Assert.assertEquals("text", data.getProperty(method).getName());
    }
}
