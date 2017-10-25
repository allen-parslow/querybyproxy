package org.cementframework.querybyproxy.shared.impl;

import java.util.List;

import org.cementframework.querybyproxy.shared.api.ProxyQuery;
import org.cementframework.querybyproxy.shared.api.ProxyQueryBuilder;
import org.cementframework.querybyproxy.shared.api.ProxyQuerySessions;
import org.cementframework.querybyproxy.shared.api.TypedQuery;
import org.cementframework.querybyproxy.shared.api.model.conditionals.LogicGate;
import org.cementframework.querybyproxy.shared.api.model.selections.Selection;
import org.cementframework.querybyproxy.shared.api.model.sorts.QuerySort;
import org.cementframework.querybyproxy.shared.api.model.sorts.QuerySortOperator;
import org.cementframework.querybyproxy.shared.api.model.values.Subquery;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.BinaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.ComparisonOperator;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.SubqueryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.conditionals.UnaryConditionalImpl;
import org.cementframework.querybyproxy.shared.impl.model.joins.RootJoinImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.AggregateSelectionImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.ConstructorValueImpl;
import org.cementframework.querybyproxy.shared.impl.model.selections.QueryAggregate;
import org.cementframework.querybyproxy.shared.impl.model.selections.SelectProxyImpl;
import org.cementframework.querybyproxy.shared.impl.model.sorts.DirectionalQuerySortImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxyPathExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.ProxySelectExpressionImpl;
import org.cementframework.querybyproxy.shared.impl.model.values.QueryLiteral;
import org.cementframework.querybyproxy.shared.impl.model.values.RedirectingQueryValue;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionItem;
import org.cementframework.querybyproxy.shared.impl.testmodel.CollectionProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.OneProperty;
import org.cementframework.querybyproxy.shared.impl.testmodel.SimpleCase;
import org.cementframework.recordingproxy.impl.MethodCallUtils;
import org.junit.Assert;
import org.junit.Test;

public class AbstractProxyQueryImplTest extends AbstractSessionTestBase {
    ProxyQueryBuilder qb = queryFactory.getQueryBuilder();

    @Test
    public void no_action() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        Assert.assertFalse(query.getSelect().isDistinct());
        Assert.assertEquals(0, query.getSelect().getSelections().size());

        Assert.assertEquals(1, query.getFrom().getJoins().size());
        RootJoinImpl join = (RootJoinImpl) query.getFrom().getJoins().get(0);
        Assert.assertEquals(query.getRootProxy(), join.getProxy());

        Assert.assertEquals(0, query.getWhere().getConditionalExpressions().size());
        Assert.assertEquals(0, query.getGroupBy().getSelections().size());
        Assert.assertEquals(0, query.getHaving().getConditionalExpressions().size());
        Assert.assertEquals(0, query.getOrderBy().getSorts().size());
    }

    @Test
    public void select_root() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        List<SimpleCase> items = query.select().find();
        Assert.assertNull(items);

        Assert.assertFalse(query.getSelect().isDistinct());
        Assert.assertEquals(1, query.getSelect().getSelections().size());
        Selection select;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(SelectProxyImpl.class, select.getClass());

        SelectProxyImpl actual = (SelectProxyImpl) select;
        Assert.assertEquals(a, actual.getProxy());
    }

    @Test
    public void select_root_distinct() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        TypedQuery<SimpleCase> typedQuery = query.distinct().select();
        Assert.assertNotNull(typedQuery);

        Assert.assertTrue(query.getSelect().isDistinct());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void select_proxy() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        OneProperty b = MethodCallUtils.proxy(OneProperty.class);

        List<OneProperty> items = query.select(b).find();
        Assert.assertNull(items);

        Assert.assertFalse(query.getSelect().isDistinct());
        Assert.assertEquals(1, query.getSelect().getSelections().size());
        Selection select;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(ProxySelectExpressionImpl.class, select.getClass());

        ProxySelectExpressionImpl actual = (ProxySelectExpressionImpl) select;
        Assert.assertEquals(b, actual.getProxy());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void select_proxy_value() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        OneProperty b = MethodCallUtils.proxy(OneProperty.class);

        List<OneProperty> items = query.select(qb.get(b)).find();
        Assert.assertNull(items);

        Assert.assertFalse(query.getSelect().isDistinct());
        Assert.assertEquals(1, query.getSelect().getSelections().size());
        Selection select;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(ProxySelectExpressionImpl.class, select.getClass());

        ProxySelectExpressionImpl actual = (ProxySelectExpressionImpl) select;
        Assert.assertEquals(b, actual.getProxy());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void select_selections() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.select(1, "b");

        List<Object[]> items = query.build().find();
        Assert.assertNull(items);

        Assert.assertFalse(query.getSelect().isDistinct());
        Assert.assertEquals(2, query.getSelect().getSelections().size());
        Selection select;
        QueryLiteral actual;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(QueryLiteral.class, select.getClass());
        actual = (QueryLiteral) select;
        Assert.assertEquals(1, actual.getTarget());

        select = query.getSelect().getSelections().get(1);
        Assert.assertEquals(QueryLiteral.class, select.getClass());
        actual = (QueryLiteral) select;
        Assert.assertEquals("b", actual.getTarget());
    }

    @Test(expected = IllegalStateException.class)
    public void select_selections_no_select() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.build();
    }

    @Test
    public void find() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        List<SimpleCase> results = query.find();
        Assert.assertNull(results);
    }

    @Test
    public void findSingleResult() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        SimpleCase results = query.findSingleResult();
        Assert.assertNull(results);
    }


    @Test(expected = IllegalArgumentException.class)
    public void select_constructor_null() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.select(null, 1, "b");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void select_constructor() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        List<OneProperty> items = query.select(OneProperty.class, 1, "b").find();
        Assert.assertNull(items);

        Assert.assertFalse(query.getSelect().isDistinct());
        Assert.assertEquals(1, query.getSelect().getSelections().size());
        Selection select;
        QueryLiteral arg;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(ConstructorValueImpl.class, select.getClass());

        ConstructorValueImpl actual = (ConstructorValueImpl) select;
        Assert.assertEquals(OneProperty.class, actual.getConstructorClass());

        Assert.assertEquals(2, actual.getArguments().size());

        arg = (QueryLiteral) actual.getArguments().get(0);
        Assert.assertEquals(1, arg.getTarget());

        arg = (QueryLiteral) actual.getArguments().get(1);
        Assert.assertEquals("b", arg.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void where() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.where(
                qb.literal("a").equalTo("b"),
                qb.literal(1D).greaterThan(2D)
                );

        Assert.assertEquals(2, query.getWhere().getConditionalExpressions().size());
        BinaryConditionalImpl predicate;
        QueryLiteral left;

        predicate = (BinaryConditionalImpl)
                query.getWhere().getConditionalExpressions().get(0);
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());

        predicate = (BinaryConditionalImpl)
                query.getWhere().getConditionalExpressions().get(1);
        Assert.assertEquals(ComparisonOperator.GREATER_THAN, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals(1D, left.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void andWhere_equalTo() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.andWhere("a").equalTo("b");

        Assert.assertEquals(1, query.getWhere().getConditionalExpressions().size());
        BinaryConditionalImpl predicate;
        QueryLiteral left;

        predicate = (BinaryConditionalImpl)
                query.getWhere().getConditionalExpressions().get(0);
        Assert.assertEquals(LogicGate.AND, predicate.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void andWhere_() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.andWhere("a").isNotEmpty();

        Assert.assertEquals(1, query.getWhere().getConditionalExpressions().size());
        UnaryConditionalImpl predicate;
        QueryLiteral left;

        predicate = (UnaryConditionalImpl)
                query.getWhere().getConditionalExpressions().get(0);
        Assert.assertEquals(LogicGate.AND, predicate.getLogicGate());
        Assert.assertEquals(ComparisonOperator.IS_NOT_EMPTY, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void orWhere() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.orWhere("a").equalTo("b");

        Assert.assertEquals(1, query.getWhere().getConditionalExpressions().size());
        BinaryConditionalImpl predicate;
        QueryLiteral left;

        predicate = (BinaryConditionalImpl)
                query.getWhere().getConditionalExpressions().get(0);
        Assert.assertEquals(LogicGate.OR, predicate.getLogicGate());
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void subquery() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        Subquery<Integer> subquery = query.subquery(qb.literal(1));

        Assert.assertFalse(subquery.getSelect().isDistinct());
        Assert.assertEquals(1, subquery.getSelect().getSelections().size());
        Selection select;
        QueryLiteral actual;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(QueryLiteral.class, select.getClass());
        actual = (QueryLiteral) select;
        Assert.assertEquals(1, actual.getTarget());

        Assert.assertEquals(1, subquery.getFrom().getJoins().size());
        RootJoinImpl join = (RootJoinImpl) subquery.getFrom().getJoins().get(0);
        Assert.assertEquals(query.getRootProxy(), join.getProxy());

        Assert.assertEquals(0, subquery.getWhere().getConditionalExpressions().size());
        Assert.assertEquals(0, subquery.getGroupBy().getSelections().size());
        Assert.assertEquals(0, subquery.getHaving().getConditionalExpressions().size());
        Assert.assertEquals(0, subquery.getOrderBy().getSorts().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void subquery_terse() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        Subquery<Integer> subquery = query.subquery(1);

        Assert.assertFalse(subquery.getSelect().isDistinct());
        Assert.assertEquals(1, subquery.getSelect().getSelections().size());
        Selection select;
        QueryLiteral actual;

        select = query.getSelect().getSelections().get(0);
        Assert.assertEquals(QueryLiteral.class, select.getClass());
        actual = (QueryLiteral) select;
        Assert.assertEquals(1, actual.getTarget());

        Assert.assertEquals(1, subquery.getFrom().getJoins().size());
        RootJoinImpl join = (RootJoinImpl) subquery.getFrom().getJoins().get(0);
        Assert.assertEquals(query.getRootProxy(), join.getProxy());

        Assert.assertEquals(0, subquery.getWhere().getConditionalExpressions().size());
        Assert.assertEquals(0, subquery.getGroupBy().getSelections().size());
        Assert.assertEquals(0, subquery.getHaving().getConditionalExpressions().size());
        Assert.assertEquals(0, subquery.getOrderBy().getSorts().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void groupBy() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.groupBy(
                qb.literal(1),
                qb.literal("b")
                );

        Assert.assertEquals(2, query.getGroupBy().getSelections().size());
        QueryLiteral selection;

        selection = (QueryLiteral) query.getGroupBy().getSelections().get(0);
        Assert.assertEquals(1, selection.getTarget());

        selection = (QueryLiteral) query.getGroupBy().getSelections().get(1);
        Assert.assertEquals("b", selection.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void having() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.having(
                qb.literal("a").equalTo("b"),
                qb.literal(1D).greaterThan(2D)
                );

        Assert.assertEquals(2, query.getHaving().getConditionalExpressions().size());
        BinaryConditionalImpl predicate;
        QueryLiteral left;

        predicate = (BinaryConditionalImpl)
                query.getHaving().getConditionalExpressions().get(0);
        Assert.assertEquals(ComparisonOperator.EQUALS, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals("a", left.getTarget());

        predicate = (BinaryConditionalImpl)
                query.getHaving().getConditionalExpressions().get(1);
        Assert.assertEquals(ComparisonOperator.GREATER_THAN, predicate.getOperation());
        left = (QueryLiteral) predicate.getLeftValue();
        Assert.assertEquals(1D, left.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void orderBy() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.orderBy(qb.literal(1), qb.literal("b"));

        Assert.assertEquals(2, query.getOrderBy().getSorts().size());
        QuerySort sort;
        DirectionalQuerySortImpl actual;
        QueryLiteral value;

        sort = query.getOrderBy().getSorts().get(0);
        Assert.assertEquals(DirectionalQuerySortImpl.class, sort.getClass());
        actual = (DirectionalQuerySortImpl) sort;
        Assert.assertEquals(QuerySortOperator.ASC, actual.getSortOperator());
        value = (QueryLiteral) actual.getSelection();
        Assert.assertEquals(1, value.getTarget());

        sort = query.getOrderBy().getSorts().get(1);
        actual = (DirectionalQuerySortImpl) sort;
        Assert.assertEquals(QuerySortOperator.ASC, actual.getSortOperator());
        value = (QueryLiteral) actual.getSelection();
        Assert.assertEquals("b", value.getTarget());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void orderBy_path() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        query.orderBy(qb.get(a.getText()));

        Assert.assertEquals(1, query.getOrderBy().getSorts().size());
        QuerySort sort;
        DirectionalQuerySortImpl actual;
        ProxyPathExpressionImpl value;

        sort = query.getOrderBy().getSorts().get(0);
        Assert.assertEquals(DirectionalQuerySortImpl.class, sort.getClass());
        actual = (DirectionalQuerySortImpl) sort;
        Assert.assertEquals(QuerySortOperator.ASC, actual.getSortOperator());
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getSelection().getClass());
        value = (ProxyPathExpressionImpl) actual.getSelection();
        Assert.assertEquals("text", value.getCall().getName());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void orderBy_path_2() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        query.orderBy(a.getText());

        Assert.assertEquals(1, query.getOrderBy().getSorts().size());
        QuerySort sort;
        DirectionalQuerySortImpl actual;
        ProxyPathExpressionImpl value;

        sort = query.getOrderBy().getSorts().get(0);
        Assert.assertEquals(DirectionalQuerySortImpl.class, sort.getClass());
        actual = (DirectionalQuerySortImpl) sort;
        Assert.assertEquals(QuerySortOperator.ASC, actual.getSortOperator());
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getSelection().getClass());
        value = (ProxyPathExpressionImpl) actual.getSelection();
        Assert.assertEquals("text", value.getCall().getName());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void orderBy_desc() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        query.orderBy(
                qb.desc(qb.get(a.getText()))
                );

        Assert.assertEquals(1, query.getOrderBy().getSorts().size());
        QuerySort sort;
        DirectionalQuerySortImpl actual;
        ProxyPathExpressionImpl value;

        sort = query.getOrderBy().getSorts().get(0);
        Assert.assertEquals(DirectionalQuerySortImpl.class, sort.getClass());
        actual = (DirectionalQuerySortImpl) sort;
        Assert.assertEquals(QuerySortOperator.DESC, actual.getSortOperator());
        Assert.assertEquals(ProxyPathExpressionImpl.class, actual.getSelection().getClass());
        value = (ProxyPathExpressionImpl) actual.getSelection();
        Assert.assertEquals("text", value.getCall().getName());
        Assert.assertEquals(String.class, value.getType());
        value.toString();
    }

    @Test
    public void orderBy_wrong_wrong() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);

        query.orderBy("a");

        Assert.assertEquals(0, query.getOrderBy().getSorts().size());
    }

    @Test
    public void test_ProxyQuerySessions() {
        new ProxyQuerySessions();
        new RedirectingQueryValue<Integer>(qb.literal(1), null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void select_2() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        query.select(a.getValue(), a.getText());

        Assert.assertEquals(2, query.getSelect().getSelections().size());
        ProxyPathExpressionImpl path;

        path = (ProxyPathExpressionImpl) query.getSelect().getSelections().get(0);
        Assert.assertEquals("value", path.getCall().getName());

        path = (ProxyPathExpressionImpl) query.getSelect().getSelections().get(1);
        Assert.assertEquals("text", path.getCall().getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void terse_groupBy_2() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        query.groupBy(a.getValue(), a.getText());

        Assert.assertEquals(2, query.getGroupBy().getSelections().size());
        ProxyPathExpressionImpl path;

        path = (ProxyPathExpressionImpl) query.getGroupBy().getSelections().get(0);
        Assert.assertEquals("value", path.getCall().getName());

        path = (ProxyPathExpressionImpl) query.getGroupBy().getSelections().get(1);
        Assert.assertEquals("text", path.getCall().getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void select_2_aggregate() {
        ProxyQuery<SimpleCase> query = queryFactory.createQuery(SimpleCase.class);
        SimpleCase a = query.getRootProxy();

        query.select(
                a.getText(), qb.max(a.getValue())
                );

        Assert.assertEquals(2, query.getSelect().getSelections().size());
        ProxyPathExpressionImpl path;
        AggregateSelectionImpl agg;

        path = (ProxyPathExpressionImpl) query.getSelect().getSelections().get(0);
        Assert.assertEquals("text", path.getCall().getName());

        agg = (AggregateSelectionImpl) query.getSelect().getSelections().get(1);
        Assert.assertEquals(QueryAggregate.MAX, agg.getAggregate());
        path = (ProxyPathExpressionImpl) agg.getTarget();
        Assert.assertEquals("value", path.getCall().getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void correlated_subquery() {
        ProxyQuery<CollectionProperty> query = queryFactory.createQuery(CollectionProperty.class);
        CollectionProperty a = query.getRootProxy();

        ProxyQuery<CollectionItem> subquery  = queryFactory.correlatedSubquery(a.getItems());
        CollectionItem b = subquery.getRootProxy();

        subquery.andWhere(b.getText()).equalTo(a.getLabelText());

        query.where(qb.exists(subquery.subquery()));

        Assert.assertEquals(0, query.getSelect().getSelections().size());

        Assert.assertEquals(1, query.getWhere().getConditionalExpressions().size());
        SubqueryConditionalImpl conditional = (SubqueryConditionalImpl)
                query.getWhere().getConditionalExpressions().get(0);
        Assert.assertEquals(ComparisonOperator.EXISTS, conditional.getOperation());

        Subquery actual = conditional.getSubquery();
        Assert.assertEquals(1, actual.getWhere().getConditionalExpressions().size());

        BinaryConditionalImpl correlate = (BinaryConditionalImpl)
                actual.getWhere().getConditionalExpressions().get(0);

        ProxyPathExpressionImpl corrLeft = (ProxyPathExpressionImpl) correlate.getLeftValue();
        ProxyPathExpressionImpl corrRight = (ProxyPathExpressionImpl) correlate.getRightValue();

        // finally test that expression will generate the correct correlations
        Assert.assertSame(b, MethodCallUtils.findRoot(corrLeft.getCall()));
        Assert.assertEquals("text", MethodCallUtils.path(corrLeft.getCall()));

        Assert.assertEquals(ComparisonOperator.EQUALS, correlate.getOperation());

        Assert.assertEquals(a, MethodCallUtils.findRoot(corrRight.getCall()));
        Assert.assertEquals("labelText", MethodCallUtils.path(corrRight.getCall()));
    }
}
