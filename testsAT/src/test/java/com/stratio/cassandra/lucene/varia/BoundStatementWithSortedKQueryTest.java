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
import static com.stratio.cassandra.lucene.builder.Builder.field;
import static com.stratio.cassandra.lucene.builder.Builder.geoDistance;

import com.stratio.cassandra.lucene.builder.Builder;
import com.stratio.cassandra.lucene.search.AbstractSearchTest;
import com.stratio.cassandra.lucene.util.CassandraUtilsSelect;
import org.junit.jupiter.api.Test;

/**
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */

public class BoundStatementWithSortedKQueryTest extends AbstractSearchTest {

    @Test
    public void testSortIntegerAsc() {
        utils.sort(field("integer_1").reverse(false)).checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }

    @Test
    public void testSortIntegerDesc() {
        utils.sort(field("integer_1").reverse(true)).checkOrderedColumns("integer_1", -1, -2, -3, -4, -5);
    }

    @Test
    public void testSortIntegerDefault() {
        utils.sort(field("integer_1")).checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }

    @Test
    public void testSortDoubleAsc() {
        utils.sort(field("double_1").reverse(false)).checkOrderedColumns("double_1", 1D, 2D, 3D, 3D, 3D);
    }

    @Test
    public void testSortDoubleDesc() {
        utils.sort(field("double_1").reverse(true)).checkOrderedColumns("double_1", 3D, 3D, 3D, 2D, 1D);
    }

    @Test
    public void testSortDoubleDefault() {
        utils.sort(field("double_1")).checkOrderedColumns("double_1", 1D, 2D, 3D, 3D, 3D);
    }

    @Test
    public void testSortCombined() {
        CassandraUtilsSelect select = utils.sort(field("double_1"), field("integer_1"));
        select.checkOrderedColumns("double_1", 1D, 2D, 3D, 3D, 3D);
        select.checkOrderedColumns("integer_1", -1, -2, -5, -4, -3);
    }

    @Test
    public void testSortWithFilter() {
        utils.filter(all())
            .sort(field("integer_1").reverse(false))
            .checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }

    @Test
    public void testSortWithQuery() {
        utils.query(all())
            .sort(field("integer_1").reverse(false))
            .checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }

    @Test
    public void testSortWithFilterAndQuery() {
        utils.filter(all())
            .query(all())
            .sort(field("integer_1").reverse(false))
            .checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }

    @Test
    public void testSortWithGeoDistanceFilterNotReversed() {
        utils.filter(geoDistance("geo_point", 40.442163, -3.784519, "10000km"))
            .sort(Builder.geoDistance("geo_point", 40.442163, -3.784519).reverse(false))
            .checkOrderedColumns("integer_1", -1, -2, -3, -4, -5);
    }

    @Test
    public void testSortWithGeoDistanceQueryNotReversed() {
        utils.query(geoDistance("geo_point", 40.442163, -3.784519, "10000km"))
            .sort(Builder.geoDistance("geo_point", 40.442163, -3.784519).reverse(false))
            .checkOrderedColumns("integer_1", -1, -2, -3, -4, -5);
    }

    @Test
    public void testSortWithGeoDistanceFilterReversed() {
        utils.filter(geoDistance("geo_point", 40.442163, -3.784519, "10000km"))
            .sort(Builder.geoDistance("geo_point", 40.442163, -3.784519).reverse(true))
            .checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }

    @Test
    public void testSortWithGeoDistanceQueryReversed() {
        utils.query(geoDistance("geo_point", 40.442163, -3.784519, "10000km"))
            .sort(Builder.geoDistance("geo_point", 40.442163, -3.784519).reverse(true))
            .checkOrderedColumns("integer_1", -5, -4, -3, -2, -1);
    }
}