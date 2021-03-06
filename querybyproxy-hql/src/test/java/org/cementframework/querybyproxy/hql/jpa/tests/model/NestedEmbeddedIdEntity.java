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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @author allenparslow
 */
@Entity
public class NestedEmbeddedIdEntity {

    private EmbeddedIdEntityId id;

    private EmbeddedIdEntity entity;

    private Double value;

    public NestedEmbeddedIdEntity() {
    }

    public NestedEmbeddedIdEntity(EmbeddedIdEntityId id) {
        this.id = id;
    }

    public NestedEmbeddedIdEntity(String text, Integer value) {
        this.id = new EmbeddedIdEntityId(text, value);
    }

    @EmbeddedId()
    public EmbeddedIdEntityId getId() {
        return id;
    }

    public void setId(EmbeddedIdEntityId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public EmbeddedIdEntity getEntity() {
        return entity;
    }

    public void setEntity(EmbeddedIdEntity name) {
        this.entity = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
