/*
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.lucene.varia;

import static com.stratio.cassandra.lucene.builder.Builder.all;

import java.util.LinkedHashMap;
import java.util.Map;

import com.stratio.cassandra.lucene.BaseTest;
import com.stratio.cassandra.lucene.util.CassandraUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Andres de la Pena <adelapena@stratio.com>
 */

public class SearchWithLongSkinnyRowsTest extends BaseTest {

    private static CassandraUtils utils;

    @BeforeAll
    public static void before() {
        utils = CassandraUtils.builder("search_with_long_skinny_rows")
            .withPartitionKey("id")
            .withColumn("id", "int")
            .withColumn("ascii_1", "ascii")
            .withColumn("bigint_1", "bigint")
            .withColumn("blob_1", "blob")
            .withColumn("boolean_1", "boolean")
            .withColumn("decimal_1", "decimal")
            .withColumn("date_1", "timestamp")
            .withColumn("double_1", "double")
            .withColumn("float_1", "float")
            .withColumn("integer_1", "int")
            .withColumn("inet_1", "inet")
            .withColumn("text_1", "text")
            .withColumn("varchar_1", "varchar")
            .withColumn("uuid_1", "uuid")
            .withColumn("timeuuid_1", "timeuuid")
            .withColumn("list_1", "list<text>")
            .withColumn("set_1", "set<text>")
            .withColumn("map_1", "map<text,text>")
            .build()
            .createKeyspace()
            .createTable()
            .createIndex();
        for (Integer p = 0; p < 2; p++) {
            for (Integer i = 1; i <= 100; i++) {
                Map<String, String> data = new LinkedHashMap<>();
                data.put("id", i.toString());
                data.put("ascii_1", "'ascii_bis'");
                data.put("bigint_1", "3000000000000000");
                data.put("blob_1", "0x3E0A15");
                data.put("boolean_1", "true");
                data.put("decimal_1", "3000000000.0");
                data.put("date_1", String.valueOf(System.currentTimeMillis()));
                data.put("double_1", "2.0");
                data.put("float_1", "3.0");
                data.put("integer_1", "3");
                data.put("inet_1", "'127.1.1.1'");
                data.put("text_1", "'text'");
                data.put("varchar_1", "'varchar'");
                data.put("uuid_1", "60297440-b4fa-11e3-8b5a-0002a5d5c51d");
                data.put("timeuuid_1", "a4a70900-24e1-11df-8924-001ff3591713");
                data.put("list_1", "['l2','l3']");
                data.put("set_1", "{'s2','s3'}");
                data.put("map_1", "{'k2':'v2','k3':'v3'}");
                utils.insert(data);
            }
        }
        utils.refresh();
    }

    @AfterAll
    public static void after() {
        CassandraUtils.dropKeyspaceIfNotNull(utils);
    }

    @Test
    public void testQuery1() {
        utils.query(all()).fetchSize(10).limit(1).check(1);
    }

    @Test
    public void testQuery99() {
        utils.query(all()).fetchSize(10).limit(99).check(99);
    }

    @Test
    public void testQuery100() {
        utils.query(all()).fetchSize(10).limit(100).check(100);
    }

    @Test
    public void testQuery101() {
        utils.query(all()).fetchSize(10).limit(101).check(100);
    }

    @Test
    public void testQuery1000() {
        utils.query(all()).limit(1000).check(100);
    }

    @Test
    public void testFilter1() {
        utils.filter(all()).fetchSize(10).limit(1).check(1);
    }

    @Test
    public void testFilter99() {
        utils.filter(all()).fetchSize(10).limit(99).check(99);
    }

    @Test
    public void testFilter100() {
        utils.filter(all()).fetchSize(10).limit(100).check(100);
    }

    @Test
    public void testFilter101() {
        utils.filter(all()).fetchSize(10).limit(101).check(100);
    }

    @Test
    public void testFilter1000() {
        utils.filter(all()).limit(1000).check(100);
    }
}
