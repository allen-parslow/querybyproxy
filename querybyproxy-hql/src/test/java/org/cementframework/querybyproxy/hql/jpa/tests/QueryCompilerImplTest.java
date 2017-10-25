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
package org.cementframework.querybyproxy.hql.jpa.tests;

import junit.framework.Assert;

import org.cementframework.querybyproxy.hql.api.IdentifactionVariable;
import org.cementframework.querybyproxy.hql.api.ParameterBinding;
import org.cementframework.querybyproxy.hql.impl.compiler.alias.IdentifactionVariableImpl;
import org.cementframework.querybyproxy.hql.impl.compiler.alias.LetteredIdentificationVarResolver;
import org.cementframework.querybyproxy.hql.impl.compiler.alias.NumberedIdentificationVarResolver;
import org.cementframework.querybyproxy.hql.impl.compiler.param.NamedParameterResolver;
import org.cementframework.querybyproxy.hql.jpa.api.JpaProxyQueryFactory;
import org.cementframework.querybyproxy.hql.jpa.impl.JpaProxyQueryFactoryImpl;
import org.cementframework.querybyproxy.hql.jpa.tests.model.SimpleEntity;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryParameter;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Test;

/**
 * @author allenparslow
 */
public class QueryCompilerImplTest {
    @Test
    public void test_IdentifactionVariableImpl() {
        IdentifactionVariableImpl id = new IdentifactionVariableImpl("x");
        Assert.assertEquals("x", id.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void test_LetteredIdentificationVarResolver_error() {
        LetteredIdentificationVarResolver idResolver = new LetteredIdentificationVarResolver();

        // parameter is not a proxy
        idResolver.getIdentifactionVariable(new IdentifactionVariableImpl("x"));
    }

    @Test
    public void test_LetteredIdentificationVarResolver() {
        LetteredIdentificationVarResolver idResolver = new LetteredIdentificationVarResolver();
        idResolver.setIndex(1);

        SimpleEntity a = MethodCallUtils.proxy(SimpleEntity.class);
        IdentifactionVariable id = idResolver.getIdentifactionVariable(a);
        Assert.assertEquals("b", id.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void test_NumberedIdentificationVarResolver_error() {
        NumberedIdentificationVarResolver idResolver = new NumberedIdentificationVarResolver();

        // parameter is not a proxy
        idResolver.getIdentifactionVariable(new IdentifactionVariableImpl("x"));
    }

    @Test
    public void test_NumberedIdentificationVarResolver_configured() {
        NumberedIdentificationVarResolver idResolver = new NumberedIdentificationVarResolver();
        idResolver.setPrefix("z");
        idResolver.setIndex(1);

        SimpleEntity a = MethodCallUtils.proxy(SimpleEntity.class);
        SimpleEntity b = MethodCallUtils.proxy(SimpleEntity.class);

        IdentifactionVariable id = idResolver.getIdentifactionVariable(a);
        Assert.assertEquals("z1", id.toString());

        // repeat
        id = idResolver.getIdentifactionVariable(a);
        Assert.assertEquals("z1", id.toString());

        // different
        id = idResolver.getIdentifactionVariable(b);
        Assert.assertEquals("z2", id.toString());

        idResolver = new NumberedIdentificationVarResolver("r", 5);
        id = idResolver.getIdentifactionVariable(a);
        Assert.assertEquals("r5", id.toString());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void test_NamedParameterResolver_configured() {
        JpaProxyQueryFactory queryFactory = new JpaProxyQueryFactoryImpl();
        ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

        NamedParameterResolver paramResolver = new NamedParameterResolver ();

        QueryParameter p1 = (QueryParameter) qb.param(1);
        QueryParameter p2 = (QueryParameter) qb.param(1);

        ParameterBinding binding = paramResolver.getBinding(p1, 1);
        Assert.assertEquals(":param0", binding.toString());

        binding = paramResolver.getBinding(p1, 1);
        Assert.assertEquals(":param0", binding.toString());

        binding = paramResolver.getBinding(p2, 1);
        Assert.assertEquals(":param1", binding.toString());

        paramResolver = new NamedParameterResolver ();
        paramResolver.setPrefix("f");
        paramResolver.setParamIndex(6);

        binding = paramResolver.getBinding(p1, 1);
        Assert.assertEquals(":f6", binding.toString());
    }


}
