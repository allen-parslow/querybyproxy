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

import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author allenparslow
 */
public class PropertyTestNoneValid {
    private static String theStatic = "x";
    private String theTransientAnnotation = "x";
    private String noGetter = "x";

    public static String getTheStatic() {
        return theStatic;
    }

    public static void setTheStatic(String theStatic) {
        PropertyTestNoneValid.theStatic = theStatic;
    }

    @Id
    @Transient
    public String getTheTransientAnnotation() {
        return theTransientAnnotation;
    }

    public void setTheTransientAnnotation(String theTransientAnnotation) {
        this.theTransientAnnotation = theTransientAnnotation;
    }

    public void setNoGetter(String noGetter) {
        this.noGetter = noGetter;
    }

    @Override
    public String toString() {
        return noGetter;
    }
}
