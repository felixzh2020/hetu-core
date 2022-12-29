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
package io.prestosql.parquet.reader;

import io.prestosql.parquet.RichColumnDescriptor;
import io.prestosql.spi.block.BlockBuilder;
import io.prestosql.spi.type.Type;
import org.apache.parquet.schema.PrimitiveType;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ShortDecimalColumnReaderTest
{
    @Mock
    private RichColumnDescriptor mockDescriptor;

    private ShortDecimalColumnReader shortDecimalColumnReaderUnderTest;

    @BeforeMethod
    public void setUp() throws Exception
    {
        initMocks(this);
        shortDecimalColumnReaderUnderTest = new ShortDecimalColumnReader(mockDescriptor);
    }

    @Test
    public void testReadValue() throws Exception
    {
        // Setup
        final BlockBuilder blockBuilder = null;
        final Type type = null;
        when(mockDescriptor.getMaxDefinitionLevel()).thenReturn(0);
        when(mockDescriptor.getType()).thenReturn(PrimitiveType.PrimitiveTypeName.INT64);
        when(mockDescriptor.isRequired()).thenReturn(false);

        // Run the test
        shortDecimalColumnReaderUnderTest.readValue(blockBuilder, type);

        // Verify the results
    }

    @Test
    public void testSkipValue()
    {
        // Setup
        when(mockDescriptor.getMaxDefinitionLevel()).thenReturn(0);
        when(mockDescriptor.getType()).thenReturn(PrimitiveType.PrimitiveTypeName.INT64);

        // Run the test
        shortDecimalColumnReaderUnderTest.skipValue();

        // Verify the results
    }
}