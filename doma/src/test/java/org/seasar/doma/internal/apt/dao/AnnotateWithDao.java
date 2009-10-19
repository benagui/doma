/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.jdbc.ConfigAdapter;

/**
 * @author taedium
 * 
 */
@Dao(config = ConfigAdapter.class)
@AnnotateWith(annotations = {
        @Annotation(target = AnnotationTarget.CLASS, type = ClassAnnotation.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CLASS, type = ClassAnnotation2.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCOTR, type = ConstructorAnnotation.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCOTR, type = ConstructorAnnotation2.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCOTR_PARAMETER, type = ConstructorParameterAnnotation.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCOTR_PARAMETER, type = ConstructorParameterAnnotation2.class, elements = "aaa = 1, bbb = true") })
public interface AnnotateWithDao {

    @Insert
    int insert(Emp emp);
}