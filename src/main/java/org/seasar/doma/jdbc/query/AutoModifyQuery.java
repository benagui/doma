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
package org.seasar.doma.jdbc.query;

import java.util.List;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;
import org.seasar.doma.message.Message;

public abstract class AutoModifyQuery<ENTITY> extends AbstractQuery implements ModifyQuery {

    protected static final String[] EMPTY_STRINGS = new String[] {};

    protected String[] includedPropertyNames = EMPTY_STRINGS;

    protected String[] excludedPropertyNames = EMPTY_STRINGS;

    protected final EntityDesc<ENTITY> entityDesc;

    protected ENTITY entity;

    protected PreparedSql sql;

    protected List<EntityPropertyDesc<ENTITY, ?>> targetPropertyDescs;

    protected List<EntityPropertyDesc<ENTITY, ?>> idPropertyDescs;

    protected VersionPropertyDesc<ENTITY, ?, ?> versionPropertyDesc;

    protected boolean optimisticLockCheckRequired;

    protected boolean autoGeneratedKeysSupported;

    protected boolean executable;

    protected SqlExecutionSkipCause sqlExecutionSkipCause = SqlExecutionSkipCause.STATE_UNCHANGED;

    protected SqlLogType sqlLogType;

    protected AutoModifyQuery(EntityDesc<ENTITY> entityDesc) {
        AssertionUtil.assertNotNull(entityDesc);
        this.entityDesc = entityDesc;
    }

    protected void prepareIdAndVersionPropertyDescs() {
        idPropertyDescs = entityDesc.getIdPropertyDescs();
        versionPropertyDesc = entityDesc.getVersionPropertyDesc();
    }

    protected void validateIdExistent() {
        if (idPropertyDescs.isEmpty()) {
            throw new JdbcException(Message.DOMA2022, entityDesc.getName());
        }
    }

    protected void prepareOptions() {
        if (queryTimeout <= 0) {
            queryTimeout = config.getQueryTimeout();
        }
    }

    protected boolean isTargetPropertyName(String name) {
        if (includedPropertyNames.length > 0) {
            for (String includedName : includedPropertyNames) {
                if (includedName.equals(name)) {
                    for (String excludedName : excludedPropertyNames) {
                        if (excludedName.equals(name)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (excludedPropertyNames.length > 0) {
            for (String excludedName : excludedPropertyNames) {
                if (excludedName.equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public void setEntity(ENTITY entity) {
        this.entity = entity;
    }

    public ENTITY getEntity() {
        return entity;
    }

    public void setIncludedPropertyNames(String... includedPropertyNames) {
        this.includedPropertyNames = includedPropertyNames;
    }

    public void setExcludedPropertyNames(String... excludedPropertyNames) {
        this.excludedPropertyNames = excludedPropertyNames;
    }

    public void setSqlLogType(SqlLogType sqlLogType) {
        this.sqlLogType = sqlLogType;
    }

    @Override
    public PreparedSql getSql() {
        return sql;
    }

    @Override
    public boolean isOptimisticLockCheckRequired() {
        return optimisticLockCheckRequired;
    }

    @Override
    public boolean isExecutable() {
        return executable;
    }

    @Override
    public SqlExecutionSkipCause getSqlExecutionSkipCause() {
        return sqlExecutionSkipCause;
    }

    @Override
    public boolean isAutoGeneratedKeysSupported() {
        return autoGeneratedKeysSupported;
    }

    @Override
    public SqlLogType getSqlLogType() {
        return sqlLogType;
    }

    @Override
    public String toString() {
        return sql != null ? sql.toString() : null;
    }
}
