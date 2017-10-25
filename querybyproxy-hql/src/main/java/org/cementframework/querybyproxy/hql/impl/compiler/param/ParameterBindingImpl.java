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
package org.cementframework.querybyproxy.hql.impl.compiler.param;

import org.cementframework.querybyproxy.hql.api.ParameterBinding;

/**
 * The resolved parameter-binding jpql alias.
 *
 * @author allenparslow
 */
public class ParameterBindingImpl implements ParameterBinding {

    private final String queryString;
    private final String parameterName;
    private final Object value;

    /**
     * Create a new <code>ParameterBindingImpl</code> instance.
     *
     * @param queryString
     *            the jpql query-string fragment.
     * @param parameterName
     *            the name for binding.
     * @param value
     *            current parameter value.
     */
    public ParameterBindingImpl(String queryString, String parameterName, Object value) {
        this.queryString = queryString;
        this.parameterName = parameterName;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * {@inheritDoc}
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * {@inheritDoc}
     */
    public Object getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return queryString;
    }
}
