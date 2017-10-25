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

import org.cementframework.querybyproxy.hql.api.ParameterBinding;
import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryParameter;

/**
 * Gets a bindable query-parameter.
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlQueryParameterVisitor implements QueryFragmentVisitor<QueryParameter> {

    /**
     * {@inheritDoc}
     */
    public void visit(QueryParameter parameter,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        ParameterBinding binding;
        if (parameter.getName() == null) {
            binding = resolver.getParameterResolver().getBinding(parameter,
                    parameter.getTarget());
        } else {
            binding = resolver.getParameterResolver().getBinding(parameter,
                    parameter.getTarget(), parameter.getName());
        }
        resolver.append(binding.getQueryString());
    }

}
