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
package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.ModifyQuery;

/**
 * An abstract class for commands that modify database data.
 * 
 * @param <QUERY>
 *            the query type
 */
public abstract class ModifyCommand<QUERY extends ModifyQuery> implements Command<Integer> {

    protected final QUERY query;

    protected final PreparedSql sql;

    protected ModifyCommand(QUERY query) {
        assertNotNull(query);
        this.query = query;
        this.sql = query.getSql();
    }

    @Override
    public Integer execute() {
        if (!query.isExecutable()) {
            JdbcLogger logger = query.getConfig().getJdbcLogger();
            logger.logSqlExecutionSkipping(query.getClassName(), query.getMethodName(),
                    query.getSqlExecutionSkipCause());
            return Integer.valueOf(0);
        }
        Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
        try {
            PreparedStatement preparedStatement = prepareStatement(connection);
            try {
                log();
                setupOptions(preparedStatement);
                bindParameters(preparedStatement);
                return executeInternal(preparedStatement);
            } catch (SQLException e) {
                Dialect dialect = query.getConfig().getDialect();
                throw new SqlExecutionException(query.getConfig().getExceptionSqlLogType(), sql, e,
                        dialect.getRootCause(e));
            } finally {
                JdbcUtil.close(preparedStatement, query.getConfig().getJdbcLogger());
            }
        } finally {
            JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
        }
    }

    protected PreparedStatement prepareStatement(Connection connection) {
        if (query.isAutoGeneratedKeysSupported()) {
            Config config = query.getConfig();
            Dialect dialect = config.getDialect();
            switch (dialect.getAutoGeneratedKeysType()) {
            case FIRST_COLUMN:
                return JdbcUtil.prepareStatementForAutoGeneratedKeysOfFirstColumn(connection, sql);
            case DEFAULT:
                return JdbcUtil.prepareStatementForAutoGeneratedKeys(connection, sql);
            }
        }
        return JdbcUtil.prepareStatement(connection, sql);
    }

    protected abstract int executeInternal(PreparedStatement preparedStatement) throws SQLException;

    protected void log() {
        JdbcLogger logger = query.getConfig().getJdbcLogger();
        logger.logSql(query.getClassName(), query.getMethodName(), sql);
    }

    protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
        if (query.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(query.getQueryTimeout());
        }
    }

    protected void bindParameters(PreparedStatement preparedStatement) throws SQLException {
        PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(query);
        binder.bind(preparedStatement, sql.getParameters());
    }

    protected int executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        try {
            int updatedRows = preparedStatement.executeUpdate();
            validateRows(updatedRows);
            return updatedRows;
        } catch (SQLException e) {
            Dialect dialect = query.getConfig().getDialect();
            if (dialect.isUniqueConstraintViolated(e)) {
                throw new UniqueConstraintException(query.getConfig().getExceptionSqlLogType(), sql,
                        e);
            }
            throw e;
        }
    }

    protected void validateRows(int rows) {
        if (query.isOptimisticLockCheckRequired() && rows == 0) {
            throw new OptimisticLockException(query.getConfig().getExceptionSqlLogType(), sql);
        }
    }
}
