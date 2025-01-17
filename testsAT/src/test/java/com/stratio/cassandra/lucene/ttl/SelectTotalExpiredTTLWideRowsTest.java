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
package com.stratio.cassandra.lucene.ttl;

import static com.stratio.cassandra.lucene.builder.Builder.match;
import static com.stratio.cassandra.lucene.builder.Builder.stringMapper;

import java.util.concurrent.TimeUnit;

import com.stratio.cassandra.lucene.BaseTest;
import com.stratio.cassandra.lucene.util.CassandraUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */

public class SelectTotalExpiredTTLWideRowsTest extends BaseTest {

    private static CassandraUtils utils;

    @BeforeAll
    public static void before() {
        utils = CassandraUtils.builder("test_ttl_skinny")
            .withPartitionKey("a")
            .withClusteringKey("a2")
            .withColumn("a", "int")
            .withColumn("a2", "int")
            .withColumn("b", "text", stringMapper())
            .withColumn("c", "text", stringMapper())
            .build()
            .createKeyspace()
            .createTable()
            .createIndex();
    }

    @Test
    public void testSkinnyRowsTotalExpiredRows() throws InterruptedException {
        utils.insert(new String[]{"a", "a2", "b", "c"}, new Object[]{1, 1, "a", "b"}, 5)
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{2, 2, "a", "b"}, 10)
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{3, 3, "a", "c"}, 11)
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{4, 4, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{5, 5, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{6, 6, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{7, 7, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{8, 8, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{9, 9, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{10, 10, "a", "c"})
            .flush()
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{11, 11, "a", "b"}, 5)
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{12, 12, "a", "b"}, 10)
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{13, 13, "a", "c"}, 11)
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{14, 14, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{15, 15, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{16, 16, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{17, 17, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{18, 18, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{19, 19, "a", "c"})
            .insert(new String[]{"a", "a2", "b", "c"}, new Object[]{20, 20, "a", "c"})
            .flush();

        TimeUnit.SECONDS.sleep(13);

        utils.compact(false)
            .refresh()
            .filter(match("c", "b")).check(0)
            .filter(match("c", "c")).checkUnorderedColumns("a", 4, 5, 6, 7, 8, 9, 10, 14, 15, 16, 17, 18, 19, 20)
            .filter(match("b", "a")).checkUnorderedColumns("a", 4, 5, 6, 7, 8, 9, 10, 14, 15, 16, 17, 18, 19, 20)
            .checkNumDocsInIndex(14);
    }

    @AfterAll
    public static void after() {
        CassandraUtils.dropKeyspaceIfNotNull(utils);
    }
}