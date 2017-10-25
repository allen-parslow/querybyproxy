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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cementframework.querybyproxy.hql.api.ParameterBinding;
import org.cementframework.querybyproxy.hql.api.ParameterResolver;
import org.cementframework.querybyproxy.shared.api.model.QueryBindable;

/**
 * Resolves parameter-binding jpql aliases (during the formation of jpql).
 *
 * <p>
 * Format: paramXXX and :paramXXX
 * </p>
 *
 * @author allenparslow
 */
public class NamedParameterResolver implements ParameterResolver {

    private Map<QueryBindable, ParameterBinding> expressionMap =
            new HashMap<QueryBindable, ParameterBinding>();

    private int paramIndex = 0;

    private String prefix;

    /**
     * Create a new <code>NamedParameterResolver</code> instance.
     */
    public NamedParameterResolver() {
        this("param", 0);
    }

    /**
     * Create a new <code>NamedParameterResolver</code> instance.
     *
     * @param prefix
     *            the prefix to use.
     * @param index
     *            the intital parameter index to use.
     */
    public NamedParameterResolver(String prefix, int index) {
        this.prefix = prefix;
        this.paramIndex = index;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterBinding getBinding(QueryBindable bindable, Object value) {

        ParameterBinding binding = expressionMap.get(bindable);

        if (binding == null) {
            String name = prefix + paramIndex;
            binding = new ParameterBindingImpl(
                    ":" + name,
                    name,
                    value);
            expressionMap.put(bindable, binding);
            this.paramIndex++;
        }

        return binding;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterBinding getBinding(QueryBindable bindable, Object value, String name) {
        ParameterBinding binding = expressionMap.get(bindable);

        if (binding == null) {
            name = name.replaceAll(":", "");
            binding = new ParameterBindingImpl(
                    ":" + name,
                    name,
                    value);
            expressionMap.put(bindable, binding);
        }
        return binding;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ParameterBinding> getBindings() {
        return expressionMap.values();
    }

    /**
     * Sets the parameter alias index.
     *
     * @param index
     *            the parameter index to use.
     */
    public void setParamIndex(int index) {
        this.paramIndex = index;
    }

    /**
     * The prefix for parameters.
     *
     * @param prefix
     *            the parameter prefix.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
