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
 * Creates <code>QueryCompiler</code> instances that are used to transform the
 * query-by-proxy criteria model into jpql.
 *
 * @author allenparslow
 */
public interface QueryCompilerFactory {

    /**
     * Creates a new <code>QueryCompiler</code> instance that can be used to
     * transform a query-by-proxy criteria model into jpql.
     *
     * @return the new <code>QueryCompiler</code>.
     */
    QueryCompiler createQueryComplier();
}
