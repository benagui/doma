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
package org.seasar.doma.jdbc.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.query.SqlBatchUpdateQuery;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.message.Message;

/**
 * @author bakenezumi
 *
 */
public class BatchUpdateExecutorTest extends TestCase {

    private SqlBatchUpdateQuery mockQuery() {
        final SqlBatchUpdateQuery query = new SqlBatchUpdateQuery();
        query.setConfig(new MockConfig());
        query.setSqlLogType(SqlLogType.FORMATTED);
        return query;
    }

    public void testBuilder() throws Exception {
        SqlBatchUpdateQuery query = mockQuery();
        BatchBuilder<SqlBatchUpdateQuery> builder = BatchBuilder.newInstance(query);
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "SMITH").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder = builder.fixSql();
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "ALLEN").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("2000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 20);
        builder = builder.fixSql();

        builder.execute((q) -> new BatchUpdateCommand(q));
    }

    public void testGetSql() throws Exception {
        SqlBatchUpdateQuery query = mockQuery();
        BatchBuilder<SqlBatchUpdateQuery> builder = BatchBuilder.newInstance(query);
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "SMITH").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder = builder.fixSql();
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "ALLEN").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("2000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 20);
        builder = builder.fixSql();

        String sql = String.format("update Emp%n" + "set%n" + "name = ?,%n"
                + "salary = ?%n" + "where%n" + "ID = ?");

        List<? extends Sql<?>> sqls = builder.getSqls();
        assertEquals(2, sqls.size());
        Sql<?> sql0 = sqls.get(0);
        assertEquals(sql, sql0.getRawSql());
        List<? extends SqlParameter> parameters0 = sql0.getParameters();
        assertEquals(3, parameters0.size());
        assertEquals("SMITH", parameters0.get(0).getValue());
        assertEquals(new BigDecimal("1000"), parameters0.get(1).getValue());
        assertEquals(10, parameters0.get(2).getValue());

        Sql<?> sql1 = sqls.get(1);
        assertEquals(sql, sql1.getRawSql());
        List<? extends SqlParameter> parameters1 = sql1.getParameters();
        assertEquals(3, parameters1.size());
        assertEquals("ALLEN", parameters1.get(0).getValue());
        assertEquals(new BigDecimal("2000"), parameters1.get(1).getValue());
        assertEquals(20, parameters1.get(2).getValue());
    }

    public void testLiteral() throws Exception {
        SqlBatchUpdateQuery query = mockQuery();
        BatchBuilder<SqlBatchUpdateQuery> builder = BatchBuilder.newInstance(query);
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").literal(String.class, "SMITH").sql(",");
        builder.sql("salary = ").literal(BigDecimal.class,
                new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder = builder.fixSql();
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").literal(String.class, "ALLEN").sql(",");
        builder.sql("salary = ").literal(BigDecimal.class,
                new BigDecimal("2000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 20);
        builder = builder.fixSql();

        List<? extends Sql<?>> sqls = builder.getSqls();
        assertEquals(2, sqls.size());
        Sql<?> sql0 = sqls.get(0);
        assertEquals(String.format("update Emp%n" + "set%n"
                + "name = 'SMITH',%n" + "salary = 1000%n" + "where%n"
                + "ID = ?"), sql0.getRawSql());
        List<? extends SqlParameter> parameters0 = sql0.getParameters();
        assertEquals(1, parameters0.size());
        assertEquals(10, parameters0.get(0).getValue());

        Sql<?> sql1 = sqls.get(1);
        assertEquals(String.format("update Emp%n" + "set%n"
                + "name = 'ALLEN',%n" + "salary = 2000%n" + "where%n"
                + "ID = ?"), sql1.getRawSql());
        List<? extends SqlParameter> parameters1 = sql1.getParameters();
        assertEquals(1, parameters1.size());
        assertEquals(20, parameters1.get(0).getValue());
    }

    public void testNotEqualParamCall() throws Exception {
        SqlBatchUpdateQuery query = mockQuery();
        BatchBuilder<SqlBatchUpdateQuery> builder = BatchBuilder.newInstance(query);
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "SMITH").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder = builder.fixSql();
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "ALLEN").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("2000"));
        builder = builder.fixSql();

        try {
            builder.execute((q) -> new BatchUpdateCommand(q));
        } catch (AssertionError e) {
            return;
        }

        fail();
    }

    public void testChangeType() throws Exception {
        SqlBatchUpdateQuery query = mockQuery();
        BatchBuilder<SqlBatchUpdateQuery> builder = BatchBuilder.newInstance(query);
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "SMITH").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder = builder.fixSql();
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "ALLEN").sql(",");
        builder = builder.sql("salary = ");
                
        try {
            builder.param(int.class, 2000);
        } catch (JdbcException e) {
            assertEquals(Message.DOMA2229, e.getMessageResource());
            return;
        }

        fail();
    }

    public void testParamToLiteral() throws Exception {
        SqlBatchUpdateQuery query = mockQuery();
        BatchBuilder<SqlBatchUpdateQuery> builder = BatchBuilder.newInstance(query);
        builder.sql("update Emp");
        builder.sql("set");
        builder.sql("name = ").param(String.class, "SMITH").sql(",");
        builder.sql("salary = ")
                .param(BigDecimal.class, new BigDecimal("1000"));
        builder.sql("where");
        builder.sql("ID = ").param(int.class, 10);
        builder = builder.fixSql();
        builder.sql("update Emp");
        builder.sql("set");
        builder = builder.sql("name = ");

        try {
            builder.literal(String.class, "ALLEN");
        } catch (JdbcException e) {
            assertEquals(Message.DOMA2230, e.getMessageResource());
            return;
        }

        fail();
    }

    private static class Employee {
        final int id;
        final String name;
        final BigDecimal salary;
        Employee(int id, String name, BigDecimal salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }
    }

    public void testExecutor() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee[] {
                                       new Employee(10, "SMITH", new BigDecimal("1001")),
                                       new Employee(20, "ALLEN", new BigDecimal("2001"))
                                   });
        BatchUpdateExecutor executor = BatchUpdateExecutor.newInstance(new MockConfig());
        executor.execute(employees, (emp, builder) -> {
            builder.sql("update Emp");
            builder.sql("set");
            builder.sql("name = ").param(String.class, emp.name).sql(",");
            builder.sql("salary = ")
                    .param(BigDecimal.class, emp.salary);
            builder.sql("where");
            builder.sql("ID = ").param(int.class, emp.id);
        });
    }

}
