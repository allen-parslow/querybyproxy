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

import java.util.HashMap;
import java.util.Map;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.QueryFragmentVisitor;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticOperation;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticValueImpl;

/**
 * A visitor the rends arithmetic expressions.
 *
 * @author allenparslow
 */
@SuppressWarnings("unchecked")
public class HqlArithmeticValueVisitor implements QueryFragmentVisitor<ArithmeticValueImpl> {

    private Map<ArithmeticOperation, String> operationString;

    /**
     * Create a new instance.
     */
    public HqlArithmeticValueVisitor() {
        operationString = new HashMap<ArithmeticOperation, String>();
        operationString.put(ArithmeticOperation.MULTIPLICATION, " * ");
        operationString.put(ArithmeticOperation.DIVISION, " / ");
        operationString.put(ArithmeticOperation.ADDITION, " + ");
        operationString.put(ArithmeticOperation.SUBTRACTION, " - ");
    }

    /**
     * {@inheritDoc}
     */
    public void visit(ArithmeticValueImpl value,
            QueryVisitorStrategy strategy,
            QueryCompiler resolver) {

        strategy.visit(value.getLeftOperand(), resolver);
        resolver.append(operationString.get(value.getOperation()));
        strategy.visit(value.getRightOperand(), resolver);
    }
}
