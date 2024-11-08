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
package io.prestosql.exchange;

import io.hetu.core.filesystem.HetuLocalFileSystemClient;
import io.hetu.core.filesystem.LocalConfig;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.testng.Assert.assertNotNull;

public class FileSystemExchangeManagerFactoryTest
{
    private final FileSystemExchangeManagerFactory factory = new FileSystemExchangeManagerFactory();

    @Test
    public void testCreate()
    {
        Map<String, String> config = new HashMap<>();
        config.put("exchange.base-directories", "/opt/hetu-server-1.8.0-SNAPSHOT/exchange-base-dir");
        ExchangeManager exchangeManager = factory.create(config, new HetuLocalFileSystemClient(new LocalConfig(new Properties()), Paths.get("/")));
        assertNotNull(exchangeManager);
    }
}
