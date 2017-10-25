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
package org.cementframework.querybyproxy.hql.api;

import java.util.Collection;

import org.cementframework.querybyproxy.shared.api.model.QueryBindable;

/**
 * Resolves parameter-binding jpql aliases (during the formation of jpql).
 *
 * @author allenparslow
 */
public interface ParameterResolver {
    /**
     * Gets the parameter-binding for the specified query-bindable.
     *
     * <p>
     * Given the query-bindable instance, the alias returned MUST be repeatable.
     * </p>
     *
     * @param bindable
     *            the query-bindable to alias.
     * @param value
     *            the value for the binding.
     *
     * @return the parameter-binding for the query-bindable.
     */
    ParameterBinding getBinding(QueryBindable bindable, Object value);

    /**
     * Gets the parameter-binding for the specified query-bindable.
     *
     * <p>
     * Given the query-bindable instance, the alias returned MUST be repeatable.
     * </p>
     *
     * @param bindable
     *            the query-bindable to alias.
     * @param value
     *            the value for the binding.
     * @param name
     *            the name for the binding.
     *
     * @return the parameter-binding for the query-bindable.
     */
    ParameterBinding getBinding(QueryBindable bindable, Object value, String name);

    /**
     * Gets all of the parameter-bindings that have been resolved.
     *
     * @return A collection of parameter-bindings.
     */
    Collection<ParameterBinding> getBindings();
}
