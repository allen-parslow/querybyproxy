insert into SimpleEntity (id, Name) values (1, 'foo');
insert into SimpleEntity (id, Name, value, StartDate) values (2, 'bar', 42, '2009-01-02 01:01:01.00001');
insert into SimpleEntity (id, Name, value, StartDate) values (3, 'bar', 43, '2009-01-03 01:01:01.00001');

insert into EmbeddedIdEntity (idText, idValue, Name) values ('hello', 42, 'world');
insert into EmbeddedIdEntity (idText, idValue, Name) values ('another', 1, 'entry');

insert into NestedEntity (id, SimpleEntity_Id) values (1, 1);
insert into NestedEntity (id, SimpleEntity_Id) values (2, 2);
insert into NestedEntity (id, SimpleEntity_Id) values (3, 2);
insert into NestedEntity (id, SimpleEntity_Id) values (4, null);

insert into CollectionEntity (id) values (1);
insert into CollectionEntity (id) values (2);
insert into CollectionEntity (id) values (3);

insert into CollectionReferencedEntity (id, text, collectionEntity_id) values (1, 'a', 1);
insert into CollectionReferencedEntity (id, text, collectionEntity_id) values (2, 'b', 1);
insert into CollectionReferencedEntity (id, text, collectionEntity_id) values (3, 'c', 2);
insert into CollectionReferencedEntity (id, text, collectionEntity_id) values (4, 'd', null);

insert into NestedEmbeddedIdEntity (idText, idValue, Value, ENTITY_IDTEXT, IDVALUE) values ('one', 1, 1.1, 'hello', 42);
insert into NestedEmbeddedIdEntity (idText, idValue, Value, ENTITY_IDTEXT, IDVALUE) values ('two', 2, 2.2, 'hello', 42);
insert into NestedEmbeddedIdEntity (idText, idValue, Value, ENTITY_IDTEXT, IDVALUE) values ('three', 3, 3.3, 'another', 1);

insert into AliasedPropertyEntity (id, flagText) values (1, '1');
insert into AliasedPropertyEntity (id, flagText) values (2, '0');
insert into AliasedPropertyEntity (id, flagText) values (3, null);

insert into ManyToManyEntity (id, text) values (1, 'a');
insert into ManyToManyEntity (id, text) values (2, 'b');

insert into ManyToManyItem (id, label) values (1, 'x');
insert into ManyToManyItem (id, label) values (2, 'y');
insert into ManyToManyItem (id, label) values (3, 'z');

insert into ManyToMany_Entity_Item (entity_id, item_id) values (1, 1);
insert into ManyToMany_Entity_Item (entity_id, item_id) values (1, 2);
insert into ManyToMany_Entity_Item (entity_id, item_id) values (2, 2);
insert into ManyToMany_Entity_Item (entity_id, item_id) values (2, 3);

insert into Customer (id, firstName) values (1, 'John');
