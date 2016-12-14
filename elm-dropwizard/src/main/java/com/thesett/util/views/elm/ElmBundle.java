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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * ElmBundle defines a DropWizard bundle for rendering views with Elm static programs. It allows a mapping between Elm
 * modules and the locations on the classpath where the compiled code for them can be loaded.
 *
 * <p/>It also allows Elm modules to be mapped to scripts loaded from files. This can be very useful when caching of the
 * scripts is disabled, so that they are loaded from the file every time they are used, in order to immediately reflect
 * changes during development.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Allow mapping from Elm modules to compiled script locations to be established. </td>
 *     <td> {@link ElmBundleConfig} </td></tr>
 * <tr><td> Set up the {@link ElmViewRenderer}. </td><td> {@link ElmViewRenderer} </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public abstract class ElmBundle<T extends Configuration> implements ConfiguredBundle<T>
{
    /** Provides a mapping from Elm module names to locations on the classpath where their compiled code is. */
    private final Map<String, String> modulesToClasspath = new HashMap<>();

    /** Holds the config for this bundle. */
    private ElmBundleConfig elmBundleConfig;

    /** {@inheritDoc} */
    public void initialize(Bootstrap<?> bootstrap)
    {
        /* empty */
    }

    /** {@inheritDoc} */
    public void run(T config, Environment environment) throws Exception
    {
        this.elmBundleConfig = getElmBundleConfig(config);

        // Flatten any configured overrides into a single map.
        List<Map<String, String>> configOverrides = elmBundleConfig.getOverrides();
        Map<String, String> overrides = new HashMap<>();

        if (configOverrides != null)
        {
            for (Map<String, String> override : configOverrides)
            {
                overrides.putAll(override);
            }
        }

        // Set up templates to be loaded from the classpath or filesystem for overrides.
        if (!modulesToClasspath.isEmpty())
        {
            Map<String, ElmModuleLoader> loaders = new HashMap<>();

            for (String moduleName : modulesToClasspath.keySet())
            {
                // Check if there is a filesystem override.
                if (overrides.containsKey(moduleName))
                {
                    loaders.put(moduleName, new FileElmModuleLoader(overrides.get(moduleName)));
                }
                else
                {
                    loaders.put(moduleName, new ClassPathElmModuleLoader(modulesToClasspath.get(moduleName)));
                }
            }

            ElmViewRenderer.moduleLoaders = loaders;
        }

        ElmViewRenderer.useCache = elmBundleConfig.isCacheTemplates();
    }

    /**
     * Establishes a mapping from an Elm module name to a location on the classpath where the script for that module can
     * be found.
     *
     * @param moduleName              The name of the Elm module.
     * @param scriptClasspathLocation The location on the classpath on the compiled script for the module.
     */
    public void addModuleMapping(String moduleName, String scriptClasspathLocation)
    {
        modulesToClasspath.put(moduleName, scriptClasspathLocation);
    }

    /**
     * Override this to extract the elm bundle config from your application config.
     *
     * @param  config The application configuration.
     *
     * @return The Elm bundle configuration.
     */
    protected abstract ElmBundleConfig getElmBundleConfig(T config);

    /**
     * Override this to map Elm modules to compiled scripts on the classpath where the module implementation can be
     * fnound.
     */
    protected abstract void configureModuleMapping();
}
