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

package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * Defines the SQL log formats.
 */
public enum SqlLogType {

    /**
     * The raw SQL.
     * <p>
     * The bind variables are displayed as {@code ?}.
     */
    RAW,

    /**
     * The formatted SQL.
     * <p>
     * The bind variables are replaced with the string representations of the
     * parameters. The string representations is determined by the object that
     * is return from {@link Dialect#getSqlLogFormattingVisitor()}.
     */
    FORMATTED,

    /**
     * No output.
     */
    NONE
}
