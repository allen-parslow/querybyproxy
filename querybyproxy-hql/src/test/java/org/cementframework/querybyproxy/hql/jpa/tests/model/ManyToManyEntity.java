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
package org.cementframework.querybyproxy.hql.jpa.tests.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

/**
 * @author allenparslow
 */
@Entity
public class ManyToManyEntity {
    private Integer id;
    private String text;
    private List<ManyToManyItem> entities = new ArrayList<ManyToManyItem>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToMany()
    @JoinTable(name="ManyToMany_Entity_Item",
       joinColumns = {@JoinColumn(name="entity_id")},
       inverseJoinColumns = {@JoinColumn(name="item_id")}
    )
    @OrderBy("label")
    public List<ManyToManyItem> getEntities() {
        return entities;
    }

    public void setEntities(List<ManyToManyItem> entities) {
        this.entities = entities;
    }
}
