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
package org.cementframework.querybyproxy.hql.impl.compiler;

import org.cementframework.querybyproxy.hql.api.IdentificationVariableResolver;
import org.cementframework.querybyproxy.hql.api.ParameterResolver;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;

/**
 * A basic query-compiler implementation consisting of any
 * alias-resolver/parameter-resolver.
 *
 * @author allenparslow
 */
public class BasicQueryCompilerImpl implements QueryCompiler {

    private final IdentificationVariableResolver aliasResolver;
    private final ParameterResolver parameterResolver;
    private final StringBuilder buffer = new StringBuilder();

    /**
     * Create a new <code>BasicQueryCompilerImpl</code> instance.
     *
     * @param aliasResolver
     *            the alias-resolver to use.
     * @param parameterResolver
     *            the parameter-resolver to use.
     */
    public BasicQueryCompilerImpl(
            IdentificationVariableResolver aliasResolver,
            ParameterResolver parameterResolver) {
        this.aliasResolver = aliasResolver;
        this.parameterResolver = parameterResolver;
    }

    /**
     * {@inheritDoc}
     */
    public IdentificationVariableResolver getAliasResolver() {
        return aliasResolver;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    /**
     * {@inheritDoc}
     */
    public void append(String text) {
        buffer.append(text);
    }

    /**
     * {@inheritDoc}
     */
    public String getQueryString() {
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void appendSpaceIfRequired() {
        String previousText = buffer.toString();
        if (!"".equals(previousText) && !previousText.endsWith(" ")) {
            buffer.append(" ");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return buffer.toString();
    }
}
