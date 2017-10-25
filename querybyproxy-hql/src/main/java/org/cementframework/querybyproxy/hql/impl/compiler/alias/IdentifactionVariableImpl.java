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
package org.cementframework.querybyproxy.hql.impl.compiler.alias;

import org.cementframework.querybyproxy.hql.api.IdentifactionVariable;

/**
 * The immutable resolved entity-proxy jpql alias.
 *
 * @author allenparslow
 */
public class IdentifactionVariableImpl implements IdentifactionVariable {
    private final String name;

    /**
     * Create a new <code>QueryAliasImpl</code> instance.
     *
     * @param name
     *            the alias's identifier for the entity-proxy.
     */
    public IdentifactionVariableImpl(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
}
