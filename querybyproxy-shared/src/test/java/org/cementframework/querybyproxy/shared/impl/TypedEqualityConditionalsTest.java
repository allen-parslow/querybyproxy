package org.cementframework.querybyproxy.shared.impl;

import java.util.ArrayList;
import java.util.List;

import org.cementframework.querybyproxy.shared.api.StrictQueryBuilder;
import org.cementframework.querybyproxy.shared.api.model.conditionals.LogicGate;
import org.cementframework.querybyproxy.shared.api.model.joins.QueryJoin;
import org.cementframework.querybyproxy.shared.api.model.values.StrictQueryValue;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.cementframework.querybyproxy.shared.impl.model.FromClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.GroupByClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.HavingClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.OrderByClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.SelectClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.WhereClauseImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BetweenConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BinaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.ComparisonOperator;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.LikeBinaryConditional;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.UnaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticOperation;
import org.cementframework.querybyproxy.shared.impl.model.values.ArithmeticValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ValuesListImpl;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionItem;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.OneProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Assert;
import org.junit.Test;

public class TypedEqualityConditionalsTest extends AbstractSessionTestBase {
    StrictQueryBuilder qb = queryFactory.getQueryBuilder();

    @SuppressWarnings("unchecked")
    @Test
    public void equalTo_path() {
        SimpleCase a = MethodCallUtils.proxy(SimpleCase.class);
        StrictQueryValue<String> left = qb.get(a.getText());
        BinaryConditionalImpl<String> actual = (BinaryConditionalImpl) left.equalTo(qb.literal("foo"));

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(left, actual.getLeftValue());
        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
        Assert.assertNotNull(actual.getRightValue());
        Assert.assertEquals(QueryLiteral.class, actual.getRightValue().getClass());

        QueryLiteral<String> right = (QueryLiteral<String>) actual.getRightValue();
        Assert.assertEquals("foo", right.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void equalTo() {
        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) qb.literal(1).equalTo(qb.literal(2));

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void equalTo_null() {
        BinaryConditionalImpl<Integer> actual = (BinaryConditionalImpl) qb.literal(1).equalTo(null);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notEqualTo() {
        StrictQueryValue<Integer> left = qb.literal(1);
        BinaryConditionalImpl actual = (BinaryConditionalImpl) left.notEqualTo(qb.literal(2));

        Assert.assertEquals(left, actual.getLeftValue());
        Assert.assertEquals(ComparisonOperator.NOT_EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void greaterThan() {
        BinaryConditionalImpl actual = (BinaryConditionalImpl) qb.literal(1).greaterThan(qb.literal(2));

        Assert.assertEquals(ComparisonOperator.GREATER_THAN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void greaterThanOrEqualTo() {
        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) qb.literal(1).greaterThanOrEqualTo(qb.literal(2));

        Assert.assertEquals(ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void lessThan() {
        BinaryConditionalImpl actual = (BinaryConditionalImpl) qb.literal(1).lessThan(qb.literal(2));

        Assert.assertEquals(ComparisonOperator.LESS_THAN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void lessThanOrEqualTo() {
        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) qb.literal(1).lessThanOrEqualTo(qb.literal(2));

        Assert.assertEquals(ComparisonOperator.LESS_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void like() {
        BinaryConditionalImpl<String> actual =
                (BinaryConditionalImpl) qb.literal("a").like(qb.literal("b"));

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void like_escape_null() {
        BinaryConditionalImpl<String> actual = (BinaryConditionalImpl) qb.literal("a").like(null, '\\');

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void like_escape() {
        LikeBinaryConditional<String> actual = (LikeBinaryConditional)
                qb.literal("a").like(qb.literal("b"), '\\');

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
        Assert.assertEquals(Character.valueOf('\\'), actual.getEscape());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notLike() {
        BinaryConditionalImpl<String> actual =
                (BinaryConditionalImpl) qb.literal("a").notLike(qb.literal("b"));

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notLike_escape() {
        BinaryConditionalImpl<String> actual =
                (BinaryConditionalImpl) qb.literal("a").notLike(qb.literal("b"), '\\');

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void between() {
        BetweenConditionalImpl actual = (BetweenConditionalImpl) qb.literal(1).between(qb.literal(2), qb.literal(3));

        Assert.assertEquals(ComparisonOperator.BETWEEN, actual.getOperation());
        QueryLiteral lower = (QueryLiteral) actual.getLowerBound();
        QueryLiteral upper = (QueryLiteral) actual.getUpperBound();

        Assert.assertEquals(2, lower.getTarget());
        Assert.assertEquals(3, upper.getTarget());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_null_1() {
        qb.literal(1).between(null, qb.literal(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_null_2() {
        qb.literal(1).between(qb.literal(2), null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notBetween() {
        BetweenConditionalImpl actual = (BetweenConditionalImpl) qb.literal(1).notBetween(qb.literal(2), qb.literal(3));

        Assert.assertEquals(ComparisonOperator.NOT_BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void in() {
        List<StrictQueryValue<Integer>> list = new ArrayList<StrictQueryValue<Integer>>();
        list.add(qb.literal(2));
        list.add(qb.literal(3));

        BinaryConditionalImpl actual = (BinaryConditionalImpl) qb.literal(1).in(list);

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
        List<StrictQueryValue<Integer>> list = new ArrayList<StrictQueryValue<Integer>>();
        list.add(qb.literal(2));
        list.add(qb.literal(3));

        BinaryConditionalImpl actual = (BinaryConditionalImpl) qb.literal(1).notIn(list);

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
    public void isEmpty() {
        UnaryConditionalImpl actual = (UnaryConditionalImpl) qb.literal(1).isEmpty();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_EMPTY, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isNotEmpty() {

        UnaryConditionalImpl actual = (UnaryConditionalImpl) qb.literal(1).isNotEmpty();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_EMPTY, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isNull() {
        UnaryConditionalImpl actual = (UnaryConditionalImpl) qb.literal(1).isNull();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NULL, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isNotNull() {
        UnaryConditionalImpl actual = (UnaryConditionalImpl) qb.literal(1).isNotNull();

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_NULL, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void times() {
        StrictQueryValue value = qb.literal(1).times(qb.literal(2));

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
        StrictQueryValue value = qb.literal(1).dividedBy(qb.literal(2));

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
        StrictQueryValue value = qb.literal(1).add(qb.literal(2));

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
        StrictQueryValue value = qb.literal(1).subtract(qb.literal(2));

        ArithmeticValueImpl actual = (ArithmeticValueImpl) value;
        Assert.assertEquals(ArithmeticOperation.SUBTRACTION, actual.getOperation());

        QueryLiteral left = (QueryLiteral) actual.getLeftOperand();
        QueryLiteral right = (QueryLiteral) actual.getRightOperand();

        Assert.assertEquals(1, left.getTarget());
        Assert.assertEquals(2, right.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void memberOf() {
        CollectionProperty a = MethodCallUtils.proxy(CollectionProperty.class);

        CollectionItem member = new CollectionItem();

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) qb.param(member).memberOf(qb.get(a.getItems()));

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.MEMBER_OF, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notMemberOf() {
        CollectionProperty a = MethodCallUtils.proxy(CollectionProperty.class);

        CollectionItem member = new CollectionItem();

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) qb.param(member).notMemberOf(qb.get(a.getItems()));

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.NOT_MEMBER_OF, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void in_subquery() {
        OneProperty a = MethodCallUtils.proxy(OneProperty.class);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) qb.get(a.getText()).in(createSubquery(String.class));

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void not_in_subquery() {
        OneProperty a = MethodCallUtils.proxy(OneProperty.class);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) qb.get(a.getText()).notIn(createSubquery(String.class));

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.NOT_IN, actual.getOperation());
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
