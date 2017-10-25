package org.cementframework.querybyproxy.shared.impl;

import java.util.ArrayList;

import junit.framework.Assert;

import org.cementframework.querybyproxy.shared.api.ProxyQuerySessions;
import org.cementframework.querybyproxy.shared.api.StrictQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.conditionals.Conditional;
import org.cementframework.querybyproxy.shared.api.model.conditionals.LogicGate;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoin;
import org.cementframework.querybyproxy.shared.api.model.sorts.QuerySortOperator;
import org.cementframework.querybyproxy.shared.api.model.values.StrictQueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.cementframework.querybyproxy.shared.api.model.values.TrimOption;
import org.cementframework.querybyproxy.shared.impl.model.FromClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.GroupByClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.HavingClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.OrderByClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.QueryStaticText;
import org.cementframework.querybyproxy.shared.impl.model.SelectClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.WhereClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BinaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.ComparisonOperator;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.GroupConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.SubqueryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.AggregateSelectionImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.QueryAggregate;
import org.cementframework.querybyproxy.shared.impl.model.sorts.DirectionalQuerySortImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticOperation;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryFunction;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryFunctionValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryDecoratorImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryOperator;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryValueImpl;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Test;

public class ProxyQueryBuilderImpl_fragments_strict_Test extends AbstractProxyQueryImplTest {
    StrictQueryBuilder qb = queryFactory.getQueryBuilder();

    @SuppressWarnings("unchecked")
    @Test
    public void qb_or() {
        Assert.assertEquals(LogicGate.AND, ProxyQuerySessions.get().getNextLogicGate());
        Assert.assertNull(qb.or());

        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);
        BinaryConditionalImpl<String> predicate = (BinaryConditionalImpl) qb.get(proxy.getText()).equalTo(qb.literal("x"));
        Assert.assertEquals(LogicGate.OR, predicate.getLogicGate());

        Assert.assertEquals(LogicGate.AND, ProxyQuerySessions.get().getNextLogicGate());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_group() {

        Conditional conditional = qb.group(
                qb.literal(1L).equalTo(qb.literal(2L)),
                qb.literal("a").equalTo(qb.literal("b"))
                );
        GroupConditionalImpl group = (GroupConditionalImpl) conditional;

        Assert.assertEquals(LogicGate.AND, group.getLogicGate());

        Assert.assertEquals(2, group.getConditionalExpressions().size());
        BinaryConditionalImpl predicate;
        QueryLiteral left;
        QueryLiteral right;

        predicate = (BinaryConditionalImpl)
                group.getConditionalExpressions().get(0);
        Assert.assertEquals(LogicGate.AND, predicate.getLogicGate());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals(1L, left.getTarget());
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        right = (QueryLiteral) predicate.getRightValue();
        Assert.assertEquals(2L, right.getTarget());

        predicate = (BinaryConditionalImpl)
                group.getConditionalExpressions().get(1);
        Assert.assertEquals(LogicGate.AND, predicate.getLogicGate());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        right = (QueryLiteral) predicate.getRightValue();
        Assert.assertEquals("b", right.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_group_or() {

        Conditional conditional = qb.group(
                qb.literal(1L).equalTo(qb.literal(2L)),
                qb.or(),
                qb.literal("a").equalTo(qb.literal("b"))
                );
        GroupConditionalImpl group = (GroupConditionalImpl) conditional;

        Assert.assertEquals(LogicGate.AND, group.getLogicGate());

        Assert.assertEquals(2, group.getConditionalExpressions().size());
        BinaryConditionalImpl predicate;
        QueryLiteral left;
        QueryLiteral right;

        predicate = (BinaryConditionalImpl)
                group.getConditionalExpressions().get(0);
        Assert.assertEquals(LogicGate.AND, predicate.getLogicGate());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals(1L, left.getTarget());
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        right = (QueryLiteral) predicate.getRightValue();
        Assert.assertEquals(2L, right.getTarget());

        predicate = (BinaryConditionalImpl)
                group.getConditionalExpressions().get(1);
        Assert.assertEquals(LogicGate.OR, predicate.getLogicGate());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        right = (QueryLiteral) predicate.getRightValue();
        Assert.assertEquals("b", right.getTarget());
    }

    @Test
    public void qb_desc() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        DirectionalQuerySortImpl sort = (DirectionalQuerySortImpl) qb.desc(qb.get(proxy.getText()));

        Assert.assertEquals(QuerySortOperator.DESC, sort.getSortOperator());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_count_1_strict() {
        AggregateSelectionImpl<Long> aggregate = (AggregateSelectionImpl<Long>) qb.count(qb.literal(1));
        Assert.assertEquals(QueryAggregate.COUNT, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_size_1_strict() {
        AggregateSelectionImpl<Long> aggregate = (AggregateSelectionImpl<Long>) qb.size(qb.literal(1));
        Assert.assertEquals(QueryAggregate.SIZE, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_min() {
        AggregateSelectionImpl<Integer> aggregate = (AggregateSelectionImpl<Integer>) qb.min(qb.literal(1));
        Assert.assertEquals(QueryAggregate.MIN, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_max() {
        AggregateSelectionImpl<Integer> aggregate = (AggregateSelectionImpl<Integer>) qb.max(qb.literal(1));
        Assert.assertEquals(QueryAggregate.MAX, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_sum() {
        AggregateSelectionImpl<Integer> aggregate = (AggregateSelectionImpl<Integer>) qb.sum(qb.literal(1));
        Assert.assertEquals(QueryAggregate.SUM, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_sum_type_change() {
        AggregateSelectionImpl<Long> aggregate = (AggregateSelectionImpl<Long>)
                    qb.sum(qb.literal(1), Long.class);
        Assert.assertEquals(QueryAggregate.SUM, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_avg() {
        AggregateSelectionImpl<Double> aggregate = (AggregateSelectionImpl<Double>) qb.avg(qb.literal(1));
        Assert.assertEquals(QueryAggregate.AVG, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_multiply() {
        StrictQueryValue func = qb.multiply(
                qb.literal(1), qb.literal(2));
        ArithmeticValueImpl function = (ArithmeticValueImpl) func;

        Assert.assertEquals(ArithmeticOperation.MULTIPLICATION, function.getOperation());

        QueryLiteral value1 = (QueryLiteral) function.getLeftOperand();
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getRightOperand();
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_divide() {
        StrictQueryValue func = qb.divide(
                qb.literal(1), qb.literal(2));
        ArithmeticValueImpl function = (ArithmeticValueImpl) func;

        Assert.assertEquals(ArithmeticOperation.DIVISION, function.getOperation());

        QueryLiteral value1 = (QueryLiteral) function.getLeftOperand();
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getRightOperand();
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_add() {
        StrictQueryValue func = qb.add(
                qb.literal(1), qb.literal(2));
        ArithmeticValueImpl function = (ArithmeticValueImpl) func;

        Assert.assertEquals(ArithmeticOperation.ADDITION, function.getOperation());

        QueryLiteral value1 = (QueryLiteral) function.getLeftOperand();
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getRightOperand();
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_subtract() {
        StrictQueryValue func = qb.subtract(
                qb.literal(1), qb.literal(2));
        ArithmeticValueImpl function = (ArithmeticValueImpl) func;

        Assert.assertEquals(ArithmeticOperation.SUBTRACTION, function.getOperation());

        QueryLiteral value1 = (QueryLiteral) function.getLeftOperand();
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getRightOperand();
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_concat() {
        StrictQueryValue<String> func = qb.concat(
                qb.literal("a"), qb.literal("b"));
        QueryFunctionValueImpl<String> function = (QueryFunctionValueImpl<String>) func;

        Assert.assertEquals(QueryFunction.CONCAT, function.getFunction());
        Assert.assertEquals(2, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("b", value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_currentDate() {
        StrictQueryValue func = qb.currentDate();
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.CURRENT_DATE, function.getFunction());
        Assert.assertEquals(0, function.getArguments().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_currentTime() {
        StrictQueryValue func = qb.currentTime();
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.CURRENT_TIME, function.getFunction());
        Assert.assertEquals(0, function.getArguments().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_currentTimestamp() {
        StrictQueryValue func = qb.currentTimestamp();
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.CURRENT_TIMESTAMP, function.getFunction());
        Assert.assertEquals(0, function.getArguments().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_length() {
        StrictQueryValue func = qb.length(qb.literal("a"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LENGTH, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_locate() {
        StrictQueryValue func = qb.locate(qb.literal("a"), qb.literal("b"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LOCATE, function.getFunction());
        Assert.assertEquals(2, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("b", value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_locate_2() {
        StrictQueryValue func = qb.locate(qb.literal("a"), qb.literal("b"), 1);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LOCATE, function.getFunction());
        Assert.assertEquals(3, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("b", value2.getTarget());

        QueryLiteral value3 = (QueryLiteral) function.getArguments().get(2);
        Assert.assertEquals(1, value3.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_lower() {
        StrictQueryValue func = qb.lower(qb.literal("a"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LOWER, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_abs() {
        StrictQueryValue func = qb.abs(qb.literal(1));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.ABS, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals(1, value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_mod() {
        StrictQueryValue func = qb.mod(
                qb.literal(1), qb.literal(2));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.MOD, function.getFunction());
        Assert.assertEquals(2, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_sqrt() {
        StrictQueryValue func = qb.sqrt(qb.literal(1));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.SQRT, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals(1, value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_substring() {
        StrictQueryValue func = qb.substring(qb.literal("a"), 1, 2);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.SUBSTRING, function.getFunction());
        Assert.assertEquals(3, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals(1, value2.getTarget());

        QueryLiteral value3 = (QueryLiteral) function.getArguments().get(2);
        Assert.assertEquals(2, value3.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_upper() {
        StrictQueryValue func = qb.upper(qb.literal("a"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.UPPER, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_trim_1() {
        StrictQueryValue func = qb.trim(qb.literal("a"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.TRIM, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_trim_2() {
        StrictQueryValue func = qb.trim(TrimOption.BOTH, qb.literal("a"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.TRIM, function.getFunction());
        Assert.assertEquals(2, function.getArguments().size());

        TrimOption value1 = (TrimOption) function.getArguments().get(0);
        Assert.assertEquals(TrimOption.BOTH, value1);

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("a", value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_trim_3() {
        StrictQueryValue func = qb.trim(
                TrimOption.BOTH,
                qb.literal('a'),
                qb.literal("b"));
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.TRIM, function.getFunction());
        Assert.assertEquals(4, function.getArguments().size());

        TrimOption value1 = (TrimOption) function.getArguments().get(0);
        Assert.assertEquals(TrimOption.BOTH, value1);

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals('a', value2.getTarget());

        QueryStaticText value3 = (QueryStaticText) function.getArguments().get(2);
        Assert.assertEquals(" FROM", value3.name());

        QueryLiteral value4 = (QueryLiteral) function.getArguments().get(3);
        Assert.assertEquals("b", value4.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_any() {

        Subquery subquery = createSubquery(Integer.class);
        Subquery result = qb.any(subquery);
        SubqueryDecoratorImpl decorator = (SubqueryDecoratorImpl) result;
        Assert.assertSame(SubqueryOperator.ANY, decorator.getSubqueryOperator());
        Assert.assertSame(subquery, decorator.getSubquery());

        Assert.assertSame(0, decorator.getSelect().getSelections().size());
        Assert.assertSame(0, decorator.getFrom().getJoins().size());
        Assert.assertSame(0, decorator.getWhere().getConditionalExpressions().size());
        Assert.assertSame(0, decorator.getGroupBy().getSelections().size());
        Assert.assertSame(0, decorator.getHaving().getConditionalExpressions().size());
        Assert.assertSame(0, decorator.getOrderBy().getSorts().size());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_all() {
        Subquery subquery = createSubquery(Integer.class);
        Subquery result = qb.all(subquery);
        SubqueryDecoratorImpl decorator = (SubqueryDecoratorImpl) result;
        Assert.assertSame(SubqueryOperator.ALL, decorator.getSubqueryOperator());
        Assert.assertSame(subquery, decorator.getSubquery());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_some() {
        Subquery subquery = createSubquery(Integer.class);
        Subquery result = qb.some(subquery);
        SubqueryDecoratorImpl decorator = (SubqueryDecoratorImpl) result;
        Assert.assertSame(SubqueryOperator.SOME, decorator.getSubqueryOperator());
        Assert.assertSame(subquery, decorator.getSubquery());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_exists() {
        Subquery subquery = createSubquery(Integer.class);
        Conditional result = qb.exists(subquery);
        SubqueryConditionalImpl conditional = (SubqueryConditionalImpl) result;

        Assert.assertSame(ComparisonOperator.EXISTS, conditional.getOperation());
        Assert.assertSame(subquery, conditional.getSubquery());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_notExists() {
        Subquery subquery = createSubquery(Integer.class);
        Conditional result = qb.notExists(subquery);
        SubqueryConditionalImpl conditional = (SubqueryConditionalImpl) result;

        Assert.assertSame(ComparisonOperator.NOT_EXISTS, conditional.getOperation());
        Assert.assertSame(subquery, conditional.getSubquery());
    }

    protected <T> Subquery<T> createSubquery(Class<T> clazz) {
        return new SubqueryValueImpl<T>(
                new SelectClauseImpl(false),
                new FromClauseImpl(new ArrayList<QueryJoin>()),
                new WhereClauseImpl(),
                new GroupByClauseImpl(),
                new HavingClauseImpl(),
                new OrderByClauseImpl());
    }

    @Test
    public void test() {
    }
}
