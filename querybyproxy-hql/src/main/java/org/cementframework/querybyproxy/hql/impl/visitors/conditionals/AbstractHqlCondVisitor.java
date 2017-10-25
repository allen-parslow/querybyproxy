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
package org.cementframework.querybyproxy.hql.impl.visitors.conditionals;

import java.util.HashMap;
import java.util.Map;

import org.cementframework.querybyproxy.shared.impl.model.conditionals.ComparisonOperator;

/**
 * The base class for conditional visitors.
 *
 * @author allenparslow
 */
public abstract class AbstractHqlCondVisitor {
    private Map<ComparisonOperator, String> comparisionStrings;

    /**
     * Create a new instance.
     */
    public AbstractHqlCondVisitor() {
        comparisionStrings = new HashMap<ComparisonOperator, String>();
        comparisionStrings.put(ComparisonOperator.EXISTS, "EXISTS");
        comparisionStrings.put(ComparisonOperator.NOT_EXISTS, "NOT EXISTS");
        comparisionStrings.put(ComparisonOperator.IS_EMPTY, " IS EMPTY");
        comparisionStrings.put(ComparisonOperator.IS_NOT_EMPTY, " IS NOT EMPTY");
        comparisionStrings.put(ComparisonOperator.MEMBER_OF, " MEMBER OF ");
        comparisionStrings.put(ComparisonOperator.NOT_MEMBER_OF, " NOT MEMBER OF ");
        comparisionStrings.put(ComparisonOperator.IS_NULL, " IS NULL");
        comparisionStrings.put(ComparisonOperator.IS_NOT_NULL, " IS NOT NULL");
        comparisionStrings.put(ComparisonOperator.NOT_IN, " NOT IN ");
        comparisionStrings.put(ComparisonOperator.IN, " IN ");
        comparisionStrings.put(ComparisonOperator.NOT_LIKE, " NOT LIKE ");
        comparisionStrings.put(ComparisonOperator.LIKE, " LIKE ");
        comparisionStrings.put(ComparisonOperator.BETWEEN, " BETWEEN ");
        comparisionStrings.put(ComparisonOperator.NOT_BETWEEN, " NOT BETWEEN ");
        comparisionStrings.put(ComparisonOperator.EQUALS, " = ");
        comparisionStrings.put(ComparisonOperator.NOT_EQUALS, " != ");
        comparisionStrings.put(ComparisonOperator.GREATER_THAN, " > ");
        comparisionStrings.put(ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, " >= ");
        comparisionStrings.put(ComparisonOperator.LESS_THAN, " < ");
        comparisionStrings.put(ComparisonOperator.LESS_THAN_OR_EQUAL_TO, " <= ");
    }

    protected String getComparisonString(ComparisonOperator operator) {
        return comparisionStrings.get(operator);
    }
}
