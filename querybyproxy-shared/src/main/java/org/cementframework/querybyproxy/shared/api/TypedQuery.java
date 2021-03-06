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
package org.cementframework.querybyproxy.shared.api;

import java.util.List;

/**
 * A typed qbp (query-by-proxy) query.
 *
 *
 * <p>
 * NOTE: This query is an execution manager and already has had parameters bound
 * and already has formed it's jpql.
 * </p>
 *
 * <p>
 * Not thread safe: not advisable to store as member variable.
 * </p>
 *
 * @param <T>
 *            the object-type for rows returned by the query.
 * @author allenparslow
 */
public interface TypedQuery<T> {

    /**
     * Executes the current query producing a single object.
     *
     * @return the resulting object.
     */
    T findSingleResult();

    /**
     * Executes the current query producing a list of objects.
     *
     * @return the resulting list of objects.
     */
    List<T> find();

    /**
     * Set limit on the number of rows to retrieve.
     *
     * <p>
     * Note: limit() is optional, the default is all rows</>
     *
     * @param rowLimit
     *            the maximum number of rows
     * @return self
     */
    TypedQuery<T> limit(int rowLimit);

    /**
     * Set the position of the first result.
     *
     * @param startPosition
     *            position of the first result, zero-indexed.
     * @return self
     */
    TypedQuery<T> first(int startPosition);
}
