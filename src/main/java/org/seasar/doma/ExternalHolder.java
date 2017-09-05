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

import org.seasar.doma.jdbc.holder.HolderConverter;

/**
 * Indicates to handle an arbitrary class as if it was a {@link Holder}
 * annotated class.
 * <p>
 * The annotated class must implement {@link HolderConverter}.
 * <p>
 * In the bellow code, the SalaryConverter class handles the Salary class as a
 * {@link Holder} annotated class:
 * 
 * <pre>
 * &#064;ExternalHolder
 * public class SalaryConverter implements HolderConverter&lt;Salary, BigDecimal&gt; {
 * 
 *     public BigDecimal fromHolderToValue(Salary holder) {
 *         return holder.getValue();
 *     }
 * 
 *     public Salary fromValueToHolder(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 * <p>
 * The annotated instance is required to be thread safe.
 * 
 * @see Holder
 * @see HolderConverter
 * @see HolderConverters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalHolder {
}
