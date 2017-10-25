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

import org.cementframework.querybyproxy.shared.api.model.QueryFragment;

/**
 * A strategy of rendering HQL using query-fragment (grammar) visitors.
 *
 * @author allenparslow
 */
public interface QueryVisitorStrategy {

    /**
     * Visits the specified query fragment, rendering HQL.
     *
     * @param fragment
     *            the fragment to visit.
     * @param resolver
     *            the compiler to render HQL to.
     */
    void visit(QueryFragment fragment, QueryCompiler resolver);

}
