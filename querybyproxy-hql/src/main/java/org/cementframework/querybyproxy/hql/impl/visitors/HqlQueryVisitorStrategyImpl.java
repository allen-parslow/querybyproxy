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
package org.cementframework.querybyproxy.hql.impl.visitors;

import java.util.HashMap;
import java.util.Map;

import org.cementframework.querybyproxy.hql.api.QueryVisitorStrategy;
import org.cementframework.querybyproxy.hql.api.QueryCompiler;
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.HqlBetweenConditionalVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.HqlBinaryConditionalVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.HqlGroupConditionalVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.HqlLikeBinaryCondVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.HqlSubqueryCondVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.conditionals.HqlUnaryConditionalVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.joins.HqlPathJoinVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.joins.HqlRootJoinVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.joins.HqlThetaJoinVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.selects.HqlAggregateSelectionVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.selects.HqlConstructorValueVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.selects.HqlSelectProxyVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.sorts.HqlDirectionalQuerySortVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlArithmeticValueVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlProxyPropertyVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlProxyValueVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlQueryFunctionValueVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlQueryLiteralVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlQueryParameterVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlSubQueryModifierDecVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlSubQueryVisitor;
import org.cementframework.querybyproxy.hql.impl.visitors.values.HqlValuesListVisitor;
import org.cementframework.querybyproxy.shared.api.model.QueryFragment;
import org.cementframework.querybyproxy.shared.impl.model.FromClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.GroupByClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.HavingClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.OrderByClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.SelectClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.WhereClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BetweenConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BinaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.GroupConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.LikeBinaryConditional;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.SubqueryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.UnaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.joins.PathJoinImpl;
import org.cementframework.querybyproxy.shared.impl.model.joins.RootJoinImpl;
import org.cementframework.querybyproxy.shared.impl.model.joins.ThetaJoinImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.AggregateSelectionImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.ConstructorValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.SelectProxyImpl;
import org.cementframework.querybyproxy.shared.impl.model.sorts.DirectionalQuerySortImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxyPathExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxySelectExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryFunctionValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryParameter;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryDecoratorImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ValuesListImpl;

/**
 * A strategy of rendering HQL using query-fragment (grammar) visitors.
 *
 * @author allenparslow
 */
public class HqlQueryVisitorStrategyImpl implements QueryVisitorStrategy {

    /**
     * Create a new <code>HqlQueryVisitorStrategyImpl</code> instance.
     */
    public HqlQueryVisitorStrategyImpl() {
        addDefaults();
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, QueryFragmentVisitor> visitorMap =
            new HashMap<Class<?>, QueryFragmentVisitor>();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void visit(QueryFragment instance, QueryCompiler resolver) {
        QueryFragmentVisitor visitor = visitorMap.get(instance.getClass());

        if (visitor == null) {
            throw new UnsupportedOperationException("Unknown node type: " + instance.getClass());
        }

        visitor.visit(instance, this, resolver);
    }

    /**
     * Adds a visitor to the strategy.
     *
     * @param elementType
     *            the node type to visit.
     * @param visitor
     *            the visitor to add.
     */
    @SuppressWarnings("unchecked")
    public void add(Class<?> elementType, QueryFragmentVisitor visitor) {
        visitorMap.put(elementType, visitor);
    }

    /**
     * Adds a standard set of grammar visitors to the strategy.
     */
    @SuppressWarnings("unchecked")
    public final void addDefaults() {
        visitorMap.put(SelectClauseImpl.class, new HqlSelectClauseVisitor());
        visitorMap.put(FromClauseImpl.class, new HqlFromClauseVisitor());
        visitorMap.put(WhereClauseImpl.class, new HqlWhereClauseVisitor());
        visitorMap.put(GroupByClauseImpl.class, new HqlGroupByClauseVisitor());
        visitorMap.put(HavingClauseImpl.class, new HqlHavingClauseVisitor());
        visitorMap.put(OrderByClauseImpl.class, new HqlOrderByClauseVisitor());

        visitorMap.put(BinaryConditionalImpl.class, new HqlBinaryConditionalVisitor());
        visitorMap.put(GroupConditionalImpl.class, new HqlGroupConditionalVisitor());
        visitorMap.put(LikeBinaryConditional.class, new HqlLikeBinaryCondVisitor());
        visitorMap.put(UnaryConditionalImpl.class, new HqlUnaryConditionalVisitor());
        visitorMap.put(BetweenConditionalImpl.class, new HqlBetweenConditionalVisitor());
        visitorMap.put(SubqueryConditionalImpl.class, new HqlSubqueryCondVisitor());

        visitorMap.put(ProxyPathExpressionImpl.class, new HqlProxyPropertyVisitor());
        visitorMap.put(QueryLiteral.class, new HqlQueryLiteralVisitor());
        visitorMap.put(QueryParameter.class, new HqlQueryParameterVisitor());
        visitorMap.put(SubqueryValueImpl.class, new HqlSubQueryVisitor());
        visitorMap.put(ProxySelectExpressionImpl.class, new HqlProxyValueVisitor());
        visitorMap.put(SubqueryDecoratorImpl.class, new HqlSubQueryModifierDecVisitor());
        visitorMap.put(SelectProxyImpl.class, new HqlSelectProxyVisitor());
        visitorMap.put(AggregateSelectionImpl.class, new HqlAggregateSelectionVisitor());
        visitorMap.put(ValuesListImpl.class, new HqlValuesListVisitor());
        visitorMap.put(ConstructorValueImpl.class, new HqlConstructorValueVisitor());
        visitorMap.put(ArithmeticValueImpl.class, new HqlArithmeticValueVisitor());
        visitorMap.put(QueryFunctionValueImpl.class, new HqlQueryFunctionValueVisitor());

        visitorMap.put(PathJoinImpl.class, new HqlPathJoinVisitor());
        visitorMap.put(RootJoinImpl.class, new HqlRootJoinVisitor());
        visitorMap.put(ThetaJoinImpl.class, new HqlThetaJoinVisitor());

        visitorMap.put(DirectionalQuerySortImpl.class, new HqlDirectionalQuerySortVisitor());

    }
}
