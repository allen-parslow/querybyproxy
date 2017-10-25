package org.cementframework.querybyproxy.shared.impl;

import junit.framework.Assert;

import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.sorts.QuerySortOperator;
import org.cementframework.querybyproxy.shared.api.model.values.StrictQueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.TrimOption;
import org.cementframework.querybyproxy.shared.impl.model.QueryStaticText;
import org.cementframework.querybyproxy.shared.impl.model.selections.AggregateSelectionImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.QueryAggregate;
import org.cementframework.querybyproxy.shared.impl.model.sorts.DirectionalQuerySortImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticOperation;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryFunction;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryFunctionValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryParameter;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Test;

public class ProxyQueryBuilderImpl_fragments_Test extends AbstractProxyQueryImplTest {
    ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

    @Test
    public void qb_literal() {
        QueryLiteral<Integer> literal = (QueryLiteral<Integer>) qb.literal(1);
        Assert.assertEquals(1, literal.getTarget());
    }

    @Test
    public void qb_param() {
        QueryParameter<Integer> literal = (QueryParameter<Integer>) qb.param(1);
        Assert.assertEquals(1, literal.getTarget());
    }

    @Test
    public void qb_param_name() {
        QueryParameter<Integer> literal = (QueryParameter<Integer>)
                    qb.param("xyz", Integer.class);
        Assert.assertEquals(null, literal.getTarget());
        Assert.assertEquals("xyz", literal.getName());
    }

    @Test
    public void qb_desc() {
        SimpleCase proxy = MethodCallUtils.proxy(SimpleCase.class);

        DirectionalQuerySortImpl sort = (DirectionalQuerySortImpl) qb.desc(proxy.getText());

        Assert.assertEquals(QuerySortOperator.DESC, sort.getSortOperator());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_count_1() {
        AggregateSelectionImpl<Long> aggregate = (AggregateSelectionImpl<Long>) qb.count(1);
        Assert.assertEquals(QueryAggregate.COUNT, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_min() {
        AggregateSelectionImpl<Integer> aggregate = (AggregateSelectionImpl<Integer>) qb.min(1);
        Assert.assertEquals(QueryAggregate.MIN, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_max() {
        AggregateSelectionImpl<Integer> aggregate = (AggregateSelectionImpl<Integer>) qb.max(1);
        Assert.assertEquals(QueryAggregate.MAX, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_sum() {
        AggregateSelectionImpl<Long> aggregate = (AggregateSelectionImpl<Long>) qb.sum(1);
        Assert.assertEquals(QueryAggregate.SUM, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_sum_Double() {
        AggregateSelectionImpl<Double> aggregate = (AggregateSelectionImpl<Double>) qb.sum(1D);
        Assert.assertEquals(QueryAggregate.SUM, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1D, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void qb_avg() {
        AggregateSelectionImpl<Double> aggregate = (AggregateSelectionImpl<Double>) qb.avg(1);
        Assert.assertEquals(QueryAggregate.AVG, aggregate.getAggregate());

        QueryLiteral value = (QueryLiteral) aggregate.getTarget();
        Assert.assertEquals(1, value.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_multiply() {
        StrictQueryValue func = qb.multiply(1, 2);
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
        StrictQueryValue func = qb.divide(1, 2);
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
        StrictQueryValue func = qb.add(1, 2);
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
        StrictQueryValue func = qb.subtract(1, 2);
        ArithmeticValueImpl function = (ArithmeticValueImpl) func;

        Assert.assertEquals(ArithmeticOperation.SUBTRACTION, function.getOperation());

        QueryLiteral value1 = (QueryLiteral) function.getLeftOperand();
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getRightOperand();
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_abs() {
        StrictQueryValue func = qb.abs(1);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.ABS, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals(1, value1.getTarget());
    }


    @Test
    @SuppressWarnings("unchecked")
    public void qb_concat() {
        StrictQueryValue<String> func = qb.concat("a", "b");
        QueryFunctionValueImpl<String> function = (QueryFunctionValueImpl<String>) func;

        Assert.assertEquals(QueryFunction.CONCAT, function.getFunction());
        Assert.assertEquals(true, function.isCommaSeparated());
        Assert.assertEquals(2, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("b", value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_length() {
        StrictQueryValue func = qb.length("a");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LENGTH, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_locate() {
        StrictQueryValue func = qb.locate("a", "b");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LOCATE, function.getFunction());
        Assert.assertEquals(true, function.isCommaSeparated());
        Assert.assertEquals(2, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("b", value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_locate_2() {
        StrictQueryValue func = qb.locate("a", "b", 1);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LOCATE, function.getFunction());
        Assert.assertEquals(true, function.isCommaSeparated());
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
        StrictQueryValue func = qb.lower("a");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.LOWER, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_mod() {
        StrictQueryValue func = qb.mod(1, 2);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.MOD, function.getFunction());
        Assert.assertEquals(true, function.isCommaSeparated());
        Assert.assertEquals(2, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals(1, value1.getTarget());

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals(2, value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_sqrt() {
        StrictQueryValue func = qb.sqrt(1);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.SQRT, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals(1, value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_substring() {
        StrictQueryValue func = qb.substring("a", 1, 2);
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.SUBSTRING, function.getFunction());
        Assert.assertEquals(true, function.isCommaSeparated());
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
        StrictQueryValue func = qb.upper("a");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.UPPER, function.getFunction());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_trim_1() {
        StrictQueryValue func = qb.trim("a");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.TRIM, function.getFunction());
        Assert.assertEquals(false, function.isCommaSeparated());
        Assert.assertEquals(1, function.getArguments().size());

        QueryLiteral value1 = (QueryLiteral) function.getArguments().get(0);
        Assert.assertEquals("a", value1.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_trim_2() {
        StrictQueryValue func = qb.trim(TrimOption.BOTH, "a");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.TRIM, function.getFunction());
        Assert.assertEquals(false, function.isCommaSeparated());
        Assert.assertEquals(2, function.getArguments().size());

        TrimOption value1 = (TrimOption) function.getArguments().get(0);
        Assert.assertEquals(TrimOption.BOTH, value1);

        QueryLiteral value2 = (QueryLiteral) function.getArguments().get(1);
        Assert.assertEquals("a", value2.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void qb_trim_3() {
        StrictQueryValue func = qb.trim(TrimOption.BOTH, 'a', "b");
        QueryFunctionValueImpl function = (QueryFunctionValueImpl) func;

        Assert.assertEquals(QueryFunction.TRIM, function.getFunction());
        Assert.assertEquals(false, function.isCommaSeparated());
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
}

