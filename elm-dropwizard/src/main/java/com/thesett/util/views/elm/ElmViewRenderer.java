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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

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
    public static Map<String, ElmModuleLoader> moduleLoaders;
    public static boolean useCache;

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
        // Get the name of the Module.

        // Initialize javascript environment.
        // Load the Nashorn bootstrap code.
        // Load the compiled Elm .js code.

        // Create the javascript command to run the static program.

        // Render the view.
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
