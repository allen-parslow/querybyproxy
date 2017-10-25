package org.cementframework.querybyproxy.shared.impl;

import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.conditionals.LogicGate;
import org.cementframework.querybyproxy.shared.api.model.values.StrictQueryValue;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BetweenConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BinaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.ComparisonOperator;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.UnaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticOperation;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.ValuesListImpl;
import org.junit.Assert;
import org.junit.Test;

public class TerseEqualityConditionalsTest extends AbstractSessionTestBase {
    ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

    @SuppressWarnings("unchecked")
    @Test
    public void isEmpty() {
        UnaryConditionalImpl actual = (UnaryConditionalImpl) qb.literal(1).isEmpty();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_EMPTY, actual.getOperation());
    }

    @Test
    public void isNotEmpty() {
        UnaryConditionalImpl<Integer> actual = (UnaryConditionalImpl<Integer>) qb.literal(1).isNotEmpty();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_EMPTY, actual.getOperation());
    }

    @Test
    public void isNull() {
        UnaryConditionalImpl<Integer> actual = (UnaryConditionalImpl<Integer>) qb.literal(1).isNull();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NULL, actual.getOperation());
    }

    @Test
    public void isNotNull() {
        UnaryConditionalImpl<Integer> actual = (UnaryConditionalImpl<Integer>) qb.literal(1).isNotNull();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_NULL, actual.getOperation());
    }
    @Test
    public void equalTo() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).equalTo(2);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @Test
    public void notEqualTo() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).notEqualTo(2);

        Assert.assertEquals(ComparisonOperator.NOT_EQUALS, actual.getOperation());
    }

    @Test
    public void greaterThan() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).greaterThan(2);

        Assert.assertEquals(ComparisonOperator.GREATER_THAN, actual.getOperation());
    }

    @Test
    public void greaterThanOrEqualTo() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).greaterThanOrEqualTo(2);

        Assert.assertEquals(ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @Test
    public void lessThan() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).lessThan(2);

        Assert.assertEquals(ComparisonOperator.LESS_THAN, actual.getOperation());
    }

    @Test
    public void lessThanOrEqualTo() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).lessThanOrEqualTo(2);

        Assert.assertEquals(ComparisonOperator.LESS_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @Test
    public void like() {
        BinaryConditionalImpl<String> actual = (BinaryConditionalImpl<String>) qb.literal("a").like("b");

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @Test
    public void like_escape() {
        BinaryConditionalImpl<String> actual = (BinaryConditionalImpl<String>) qb.literal("a").like("b", '\\');

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @Test
    public void notLike() {
        BinaryConditionalImpl<String> actual = (BinaryConditionalImpl<String>) qb.literal("a").notLike("b");

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @Test
    public void notLike_escape() {
        BinaryConditionalImpl<String> actual = (BinaryConditionalImpl<String>) qb.literal("a").notLike("b", '\\');

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void between() {
        BetweenConditionalImpl actual = (BetweenConditionalImpl) qb.literal(1).between(2, 3);

        Assert.assertEquals(ComparisonOperator.BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notBetween() {
        BetweenConditionalImpl actual = (BetweenConditionalImpl) qb.literal(1).notBetween(2, 3);

        Assert.assertEquals(ComparisonOperator.NOT_BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void in() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).in(2, 3);

        Assert.assertEquals(ComparisonOperator.IN, actual.getOperation());
        ValuesListImpl values = (ValuesListImpl) actual.getRightValue();
        Assert.assertEquals(2, values.getValues().size());

        QueryLiteral value = (QueryLiteral) values.getValues().get(0);
        Assert.assertEquals(2, value.getTarget());

        value = (QueryLiteral) values.getValues().get(1);
        Assert.assertEquals(3, value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notIn() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl<Integer>) qb.literal(1).notIn(2, 3);

        Assert.assertEquals(ComparisonOperator.NOT_IN, actual.getOperation());
        ValuesListImpl values = (ValuesListImpl) actual.getRightValue();
        Assert.assertEquals(2, values.getValues().size());

        QueryLiteral value = (QueryLiteral) values.getValues().get(0);
        Assert.assertEquals(2, value.getTarget());

        value = (QueryLiteral) values.getValues().get(1);
        Assert.assertEquals(3, value.getTarget());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void times() {
        StrictQueryValue value = qb.literal(1).times(2);

        ArithmeticValueImpl actual = (ArithmeticValueImpl) value;
        Assert.assertEquals(ArithmeticOperation.MULTIPLICATION, actual.getOperation());

        QueryLiteral left = (QueryLiteral) actual.getLeftOperand();
        QueryLiteral right = (QueryLiteral) actual.getRightOperand();

        Assert.assertEquals(1, left.getTarget());
        Assert.assertEquals(2, right.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void dividedBy() {
        StrictQueryValue value = qb.literal(1).dividedBy(2);

        ArithmeticValueImpl actual = (ArithmeticValueImpl) value;
        Assert.assertEquals(ArithmeticOperation.DIVISION, actual.getOperation());

        QueryLiteral left = (QueryLiteral) actual.getLeftOperand();
        QueryLiteral right = (QueryLiteral) actual.getRightOperand();

        Assert.assertEquals(1, left.getTarget());
        Assert.assertEquals(2, right.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void add() {
        StrictQueryValue value = qb.literal(1).add(2);

        ArithmeticValueImpl actual = (ArithmeticValueImpl) value;
        Assert.assertEquals(ArithmeticOperation.ADDITION, actual.getOperation());

        QueryLiteral left = (QueryLiteral) actual.getLeftOperand();
        QueryLiteral right = (QueryLiteral) actual.getRightOperand();

        Assert.assertEquals(1, left.getTarget());
        Assert.assertEquals(2, right.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void subtract() {
        StrictQueryValue value = qb.literal(1).subtract(2);

        ArithmeticValueImpl actual = (ArithmeticValueImpl) value;
        Assert.assertEquals(ArithmeticOperation.SUBTRACTION, actual.getOperation());

        QueryLiteral left = (QueryLiteral) actual.getLeftOperand();
        QueryLiteral right = (QueryLiteral) actual.getRightOperand();

        Assert.assertEquals(1, left.getTarget());
        Assert.assertEquals(2, right.getTarget());
    }

    @Test
    public void test() {
    }
}
