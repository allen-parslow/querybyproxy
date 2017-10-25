package org.cementframework.querybyproxy.shared.impl;

import java.util.ArrayList;
import java.util.List;

import org.cementframework.querybyproxy.shared.api.ProxyQuery;
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
import org.cementframework.querybyproxy.shared.impl.model.conditionals.UnaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.SubqueryValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ValuesListImpl;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionItem;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.OneProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AndWhereConditionalsTest extends AbstractSessionTestBase {
    ProxyQuery<SimpleCase> query;
    StrictQueryBuilder qb2 = queryFactory.getQueryBuilder();

    @Before
    public void createQuery() {
        query = queryFactory.createQuery(SimpleCase.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isEmpty() {
        query.andWhere(1).isEmpty();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_EMPTY, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isNotEmpty() {
        query.andWhere(1).isNotEmpty();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_EMPTY, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isNull() {
        query.andWhere(1).isNull();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NULL, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isNotNull() {
        query.andWhere(1).isNotNull();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_NULL, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void equalTo() {
        query.andWhere(1).equalTo(2);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notEqualTo() {
        query.andWhere(1).notEqualTo(2);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_notEqualTo() {
        query.andWhere(1).notEqualTo(qb2.literal(2));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void greaterThan() {
        query.andWhere(1).greaterThan(2);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.GREATER_THAN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void greaterThanOrEqualTo() {
        query.andWhere(1).greaterThanOrEqualTo(2);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void lessThan() {
        query.andWhere(1).lessThan(2);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LESS_THAN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void lessThanOrEqualTo() {
        query.andWhere(1).lessThanOrEqualTo(2);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LESS_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void like() {
        query.andWhere("a").like("b");

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void memberOf() {
        CollectionProperty a = MethodCallUtils.proxy(CollectionProperty.class);

        CollectionItem member = new CollectionItem();

        query.andWhere(member).memberOf(qb2.get(a.getItems()));

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.MEMBER_OF, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notMemberOf() {
        CollectionProperty a = MethodCallUtils.proxy(CollectionProperty.class);

        CollectionItem member = new CollectionItem();

        query.andWhere(member).notMemberOf(qb2.get(a.getItems()));

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.NOT_MEMBER_OF, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void like_escape() {
        query.andWhere("a").like("b", '\\');

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notLike() {
        query.andWhere("a").notLike("b");

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notLike_escape() {
        query.andWhere("a").notLike("b", '\\');

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void between() {
        query.andWhere(1).between(2, 3);

        BetweenConditionalImpl actual =
                (BetweenConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void notBetween() {
        query.andWhere(1).notBetween(2, 3);

        BetweenConditionalImpl actual =
                (BetweenConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void in() {
        query.andWhere(1).in(2, 3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

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
        query.andWhere(1).notIn(2, 3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

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
        query.andWhere(1).times(2).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void times_times() {
        query.andWhere(1).times(2).times(2).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void dividedBy() {
        query.andWhere(1).dividedBy(2).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void add() {
        query.andWhere(1).add(2).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void subtract() {
        query.andWhere(1).subtract(2).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_equalTo() {
        query.andWhere(1).equalTo(qb2.literal(2));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_greaterThan() {
        query.andWhere(1).greaterThan(qb2.literal(2));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.GREATER_THAN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_greaterThanOrEqualTo() {
        query.andWhere(1).greaterThanOrEqualTo(qb2.literal(2));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_lessThan() {
        query.andWhere(1).lessThan(qb2.literal(2));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LESS_THAN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_lessThanOrEqualTo() {
        query.andWhere(1).lessThanOrEqualTo(qb2.literal(2));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LESS_THAN_OR_EQUAL_TO, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_like() {
        query.andWhere("a").like(qb2.literal("b"));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_like_escape() {
        query.andWhere("a").like(qb2.literal("b"), '\\');

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void typed_like_escape_null() {
        StrictQueryValue<String> value = null;
        query.andWhere("a").like(value, '\\');

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_notLike() {
        query.andWhere("a").notLike(qb2.literal("b"));

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_notLike_escape() {
        query.andWhere("a").notLike(qb2.literal("b"), '\\');

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_LIKE, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_between() {
        query.andWhere(1).between(qb2.literal(2), qb2.literal(3));

        BetweenConditionalImpl actual =
                (BetweenConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_notBetween() {
        query.andWhere(1).notBetween(qb2.literal(2), qb2.literal(3));

        BetweenConditionalImpl<Integer> actual =
                (BetweenConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.NOT_BETWEEN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_in() {
        List<StrictQueryValue<Integer>> list = new ArrayList<StrictQueryValue<Integer>>();
        list.add(qb2.literal(2));
        list.add(qb2.literal(3));

        query.andWhere(1).in(list);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

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
    public void typed_notIn() {
        List<StrictQueryValue<Integer>> list = new ArrayList<StrictQueryValue<Integer>>();
        list.add(qb2.literal(2));
        list.add(qb2.literal(3));

        query.andWhere(1).notIn(list);

        BinaryConditionalImpl<Integer> actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

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
    public void typed_isEmpty() {
        query.andWhere(1).isEmpty();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_EMPTY, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_isNotEmpty() {
        query.andWhere(1).isNotEmpty();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_EMPTY, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_isNull() {
        query.andWhere(1).isNull();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NULL, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_isNotNull() {
        query.andWhere(1).isNotNull();

        UnaryConditionalImpl<Integer> actual =
                (UnaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_NULL, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_times() {
        query.andWhere(1).times(qb2.literal(2)).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_dividedBy() {
        query.andWhere(1).dividedBy(qb2.literal(2)).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_add() {
        query.andWhere(1).add(qb2.literal(2)).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_subtract() {
        query.andWhere(1).subtract(qb2.literal(2)).equalTo(3);

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(ComparisonOperator.EQUALS, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_memberOf() {
        CollectionProperty a = MethodCallUtils.proxy(CollectionProperty.class);

        CollectionItem member = new CollectionItem();

        query.andWhere(member).memberOf(qb2.get(a.getItems()));

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.MEMBER_OF, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_notMemberOf() {
        CollectionProperty a = MethodCallUtils.proxy(CollectionProperty.class);

        CollectionItem member = new CollectionItem();

        query.andWhere(member).notMemberOf(qb2.get(a.getItems()));

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.NOT_MEMBER_OF, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_in_subquery() {
        OneProperty a = MethodCallUtils.proxy(OneProperty.class);

        query.andWhere(a.getText()).in(createSubquery(String.class));

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

        Assert.assertEquals(LogicGate.AND, actual.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IN, actual.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void typed_not_in_subquery() {
        OneProperty a = MethodCallUtils.proxy(OneProperty.class);

        query.andWhere(a.getText()).notIn(createSubquery(String.class));

        BinaryConditionalImpl actual =
                (BinaryConditionalImpl) query.getWhere().getConditionalExpressions().get(0);

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

}
