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
 * Transforms the query-by-proxy criteria model into jpql.
 *
 * @author allenparslow
 */
public interface QueryCompiler {

    /**
     * Gets the identification-variable resolver that will resolve aliases for
     * entity proxies.
     *
     * @return the identification-variable resolver.
     */
    IdentificationVariableResolver getAliasResolver();

    /**
     * Gets the resolver that will resolve parameter-binding jpql aliases.
     *
     * @return the parameter resolver.
     */
    ParameterResolver getParameterResolver();

    /**
     * Appends text the current jpql buffer.
     *
     * @param text
     *            the text to append.
     */
    void append(String text);

    /**
     * Appends a space (" ") character to the current jpql buffer if the buffer
     * is not empty and the buffer doesn't already end in a space character.
     */
    void appendSpaceIfRequired();

    /**
     * Gets the jpql query-string current string from the buffer.
     *
     * @return The jpql query-string .
     */
    String getQueryString();
}
