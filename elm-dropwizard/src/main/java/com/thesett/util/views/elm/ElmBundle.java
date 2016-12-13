/*
 * Copyright The Sett Ltd.
 *
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
package com.thesett.util.views.elm;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public abstract class ElmBundle<T extends Configuration> implements ConfiguredBundle<T>
{
    /** {@inheritDoc} */
    public void initialize(Bootstrap<?> bootstrap)
    {
        /* empty */
    }

    /** {@inheritDoc} */
    public void run(T config, Environment environment) throws Exception
    {
    }

    /**
     * Override this to extract the elm bundle config from your application config.
     *
     * @param  config The application configuration.
     *
     * @return The Elm bundle configuration.
     */
    protected abstract ElmBundleConfig getElmBundleConfig(T config);
}
