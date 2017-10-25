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
package org.cementframework.querybyproxy.hql.impl.visitors.values;

import java.util.List;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxyPathExpressionImpl;
import org.cementframework.recordingproxy.api.RecordedMethodCall;
import org.cementframework.recordingproxy.impl.MethodCallUtils;

/**
 * Represent of property of an entity-proxy.
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlProxyPropertyVisitor implements QueryFragmentVisitor<ProxyPathExpressionImpl> {

    /**
     * {@inheritDoc}
     */
    public void visit(ProxyPathExpressionImpl property,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        Object root = property.getCall().getInvokingProxy();
        List<RecordedMethodCall> calls = MethodCallUtils.listParentCalls(property.getCall());
        if (calls.size() != 0) {
            root = calls.get(0).getInvokingProxy();
        }
        resolver.append(resolver.getAliasResolver().getIdentifactionVariable(root).getName());
        resolver.append(".");
        resolver.append(MethodCallUtils.nestedPath(property.getCall()));
        resolver.append(property.getCall().getName());
    }
}
