/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.operator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.airlift.json.JsonCodec;
import io.airlift.units.DataSize;
import io.airlift.units.Duration;
import io.prestosql.execution.Lifespan;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static io.airlift.units.DataSize.Unit.BYTE;
import static io.prestosql.operator.TestOperatorStats.assertExpectedOperatorStats;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.joda.time.DateTimeZone.UTC;
import static org.testng.Assert.assertEquals;

public class TestDriverStats
{
    public static final DriverStats EXPECTED = new DriverStats(
            Lifespan.driverGroup(21),

            new DateTime(1),
            new DateTime(2),
            new DateTime(3),

            new Duration(4, NANOSECONDS),
            new Duration(5, NANOSECONDS),

            new DataSize(6, BYTE),
            new DataSize(7, BYTE),
            new DataSize(8, BYTE),

            new Duration(9, NANOSECONDS),
            new Duration(10, NANOSECONDS),
            new Duration(12, NANOSECONDS),
            false,
            ImmutableSet.of(),

            new DataSize(131, BYTE),
            141,
            new Duration(151, NANOSECONDS),

            new DataSize(132, BYTE),
            142,
            new Duration(152, NANOSECONDS),

            new DataSize(13, BYTE),
            14,
            new Duration(15, NANOSECONDS),

            new DataSize(16, BYTE),
            17,

            new DataSize(18, BYTE),
            19,

            new DataSize(20, BYTE),

            ImmutableList.of(TestOperatorStats.EXPECTED),

            new Duration(155, NANOSECONDS),
            new Duration(155, NANOSECONDS));

    @Test
    public void testJson()
    {
        JsonCodec<DriverStats> codec = JsonCodec.jsonCodec(DriverStats.class);

        String json = codec.toJson(EXPECTED);
        DriverStats actual = codec.fromJson(json);

        assertExpectedDriverStats(actual);
    }

    public static void assertExpectedDriverStats(DriverStats actual)
    {
        assertEquals(actual.getLifespan(), Lifespan.driverGroup(21));

        assertEquals(actual.getCreateTime(), new DateTime(1, UTC));
        assertEquals(actual.getStartTime(), new DateTime(2, UTC));
        assertEquals(actual.getEndTime(), new DateTime(3, UTC));
        assertEquals(actual.getQueuedTime(), new Duration(4, NANOSECONDS));
        assertEquals(actual.getElapsedTime(), new Duration(5, NANOSECONDS));

        assertEquals(actual.getUserMemoryReservation(), new DataSize(6, BYTE));
        assertEquals(actual.getRevocableMemoryReservation(), new DataSize(7, BYTE));
        assertEquals(actual.getSystemMemoryReservation(), new DataSize(8, BYTE));

        assertEquals(actual.getTotalScheduledTime(), new Duration(9, NANOSECONDS));
        assertEquals(actual.getTotalCpuTime(), new Duration(10, NANOSECONDS));
        assertEquals(actual.getTotalBlockedTime(), new Duration(12, NANOSECONDS));

        assertEquals(actual.getPhysicalInputDataSize(), new DataSize(131, BYTE));
        assertEquals(actual.getPhysicalInputPositions(), 141);
        assertEquals(actual.getPhysicalInputReadTime(), new Duration(151, NANOSECONDS));

        assertEquals(actual.getInternalNetworkInputDataSize(), new DataSize(132, BYTE));
        assertEquals(actual.getInternalNetworkInputPositions(), 142);
        assertEquals(actual.getInternalNetworkInputReadTime(), new Duration(152, NANOSECONDS));

        assertEquals(actual.getRawInputDataSize(), new DataSize(13, BYTE));
        assertEquals(actual.getRawInputPositions(), 14);
        assertEquals(actual.getRawInputReadTime(), new Duration(15, NANOSECONDS));

        assertEquals(actual.getProcessedInputDataSize(), new DataSize(16, BYTE));
        assertEquals(actual.getProcessedInputPositions(), 17);

        assertEquals(actual.getOutputDataSize(), new DataSize(18, BYTE));
        assertEquals(actual.getOutputPositions(), 19);

        assertEquals(actual.getPhysicalWrittenDataSize(), new DataSize(20, BYTE));

        assertEquals(actual.getOperatorStats().size(), 1);
        assertExpectedOperatorStats(actual.getOperatorStats().get(0));
    }
}
