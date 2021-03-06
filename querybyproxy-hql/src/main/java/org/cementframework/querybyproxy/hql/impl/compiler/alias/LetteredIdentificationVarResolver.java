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

import java.util.HashMap;
import java.util.Map;

import org.cementframework.querybyproxy.hql.api.IdentifactionVariable;
import org.cementframework.querybyproxy.hql.api.IdentificationVariableResolver;

import net.sf.cglib.proxy.Enhancer;

/**
 * An identification variable resolver where alias are produced with single
 * letters.
 *
 * @author allenparslow
 */
public class LetteredIdentificationVarResolver implements IdentificationVariableResolver {

    private int index = 0;

    private final Map<Object, IdentifactionVariableImpl> aliasMap =
            new HashMap<Object, IdentifactionVariableImpl>();

    /**
     * {@inheritDoc}
     */
    public IdentifactionVariable getIdentifactionVariable(Object proxy) {
        if (!Enhancer.isEnhanced(proxy.getClass())) {
            throw new IllegalStateException("Object is not a proxy instance: " + proxy);
        }
        IdentifactionVariableImpl alias = aliasMap.get(proxy);

        if (alias == null) {
            String aliasText = getAliasText();
            alias = new IdentifactionVariableImpl(aliasText);

            aliasMap.put(proxy, alias);
            index++;
        }

        return alias;
    }

    protected String getAliasText() {
        return String.valueOf((char) ('a' + index));
    }

    /**
     * Sets the alias index.
     *
     * @param index
     *            the alias index to use.
     */
    public void setIndex(int index) {
        this.index = index;
    }

}
