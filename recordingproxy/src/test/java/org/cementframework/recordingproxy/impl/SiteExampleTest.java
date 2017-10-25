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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.beanutils.BeanComparator;
import org.cementframework.recordingproxy.impl.testmodel.Customer;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class SiteExampleTest {



    @SuppressWarnings("unchecked")
    @Test
    public void basicOperation() {

        Customer proxy = MethodCallUtils.proxy(Customer.class);

        List<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer("Zach"));
        customers.add(new Customer("Bob"));
        customers.add(new Customer("Mary"));

        org.apache.commons.beanutils.BeanComparator comparator
            = new BeanComparator(MethodCallUtils.methodName(proxy.getFirstName()));

        java.util.Collections.sort(customers, comparator);

        Assert.assertEquals("Bob", customers.get(0).getFirstName());
    }
}
