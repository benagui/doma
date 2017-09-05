/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a database column.
 * <p>
 * The annotated field must be a member of an {@link Entity} annotated class.
 * <p>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     String employeeName;
 * 
 *     &#064;Column(name = &quot;SALARY&quot;)
 *     BigDecimal salary;
 *     
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * The name of the column.
     * <p>
     * If not specified, the name is resolved by {@link Entity#naming()}.
     */
    String name() default "";

    /**
     * Whether the column is included in SQL INSERT statements.
     */
    boolean insertable() default true;

    /**
     * Whether the column is included in SQL UPDATE statements.
     */
    boolean updatable() default true;

    /**
     * Whether the column name is enclosed by quotation marks in SQL statements.
     */
    boolean quote() default false;
}
