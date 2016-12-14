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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptException;

import com.google.common.base.Charsets;
import com.thesett.elm.ElmRenderer;

import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;

/**
 * Renders Html views written in Elm.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Run a static Html Elm program to render a view. </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ElmViewRenderer implements ViewRenderer
{
    /** Holds a mapping from module names to loaders for their renderers. */
    public static Map<String, ElmModuleLoader> moduleLoaders = new HashMap<>();

    /** Indicates whether or not the renderer cache should be used. */
    public static boolean useCache;

    /** Holds a cache of renderers for Elm modules. */
    private final Map<String, ElmRenderer> moduleRenderers = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     *
     * <p/>A view is renderable iff it is an {@link ElmView}.
     */
    public boolean isRenderable(View view)
    {
        return (view instanceof ElmView);
    }

    /** {@inheritDoc} */
    public void render(View view, Locale locale, OutputStream outputStream) throws IOException
    {
        ElmView elmView = (ElmView) view;

        // Get the name of the Module.
        String moduleName = elmView.getModuleName();

        ElmRenderer renderer = null;

        // If caching is enabled, try and fetch the renderer from the cache.
        if (useCache)
        {
            renderer = moduleRenderers.get(moduleName);
        }

        // If no cached renderer was found, try and obtain it through its loader.
        if (renderer == null)
        {
            ElmModuleLoader loader = moduleLoaders.get(moduleName);

            if (loader == null)
            {
                throw new IllegalStateException("No module loader mapping was found for Elm module: " + moduleName);
            }

            try
            {
                renderer = loader.loadRenderer();
            }
            catch (ScriptException | FileNotFoundException e)
            {
                throw new IllegalStateException("Failed to load compiled Elm module: " + moduleName, e);
            }
            catch (Throwable t) {
                t.printStackTrace();
                throw t;
            }
        }

        // If caching is enabled, retain the renderer in the cache.
        if (useCache)
        {
            moduleRenderers.put(moduleName, renderer);
        }

        // Render the view.
        try(Writer writer = new OutputStreamWriter(outputStream, view.getCharset().or(Charsets.UTF_8)))
        {
            String result = (String) renderer.runModule(moduleName, elmView.getContent());
            writer.write(result);
            writer.flush();
        }
        catch (ScriptException e)
        {
            throw new IllegalStateException("Elm script failed to run for module: " + moduleName, e);
        }
    }

    /** {@inheritDoc} */
    public void configure(Map<String, String> map)
    {
    }

    /** {@inheritDoc} */
    public String getSuffix()
    {
        return "";
    }
}
