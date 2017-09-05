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
 * Indicates a value holder class.
 * <p>
 * The holder class is the user defined type that wraps a basic value. It can be
 * mapped to a database column.
 * <p>
 * Instantiation by constructor:
 * 
 * <pre>
 * &#064;Holder(valueType = String.class)
 * public class PhoneNumber {
 * 
 *     private final String value;
 * 
 *     public PhoneNumber(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * }
 * </pre>
 * 
 * Instantiation by factory method:
 * 
 * <pre>
 * &#064;Holder(valueType = String.class, factoryMethod = &quot;of&quot;)
 * public class PhoneNumber {
 * 
 *     private final String value;
 * 
 *     private PhoneNumber(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * 
 *     public static PhoneNumber of(String value) {
 *         return new PhoneNumber(value);
 *     }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Holder {

    /**
     * The value type that is wrapped by the holder class.
     */
    Class<?> valueType();

    /**
     * The factory method name.
     * <p>
     * The factory method that accepts the wrapped value as an argument.
     * <p>
     * The default value {@code "new"} means constructor usage.
     */
    String factoryMethod() default "new";

    /**
     * The accessor method name.
     * <p>
     * The accessor method returns the wrapped value.
     */
    String accessorMethod() default "getValue";

    /**
     * Whether the constructor or the factory method accepts {@code null} as an
     * argument.
     */
    boolean acceptNull() default false;

}
