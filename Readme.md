### Query by Proxy uses a recording proxy to create strongly-typed fluent queries.

### Both JPA and Hibernate are supported.

#### Example:
```
    ProxyQuery<SimpleEntity> query = JpaProxyQueries.createQueryFactory(entityManager)
                .createQuery(SomeEntity.class);
    ProxyQueryBuilder qb = queryFactory.getQueryBuilder();
    SomeEntity a = query.getRootProxy();

    query.where(qb.get(a.getId()).equalTo(2));

    List<SomeEntity> results = query.select().find();
```