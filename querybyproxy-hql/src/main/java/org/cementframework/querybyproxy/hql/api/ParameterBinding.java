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

/**
 * The resolved parameter-binding jpql alias.
 *
 * @author allenparslow
 */
public interface ParameterBinding {

    /**
     * Gets the query-string to incorporate into a jpql string.
     *
     * @return the jpql query-string fragment.
     */
    String getQueryString();

    /**
     * Gets the name of the parameter to be used during <code>Query</code>
     * .setParameter.
     *
     * @return the name for binding.
     */
    String getParameterName();

    /**
     * Gets the current value of the parameter.
     *
     * @return current parameter value.
     */
    Object getValue();
}
