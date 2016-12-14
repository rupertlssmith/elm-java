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

import io.dropwizard.views.View;

/**
 * ElmView represents a view that is rendered by a static Elm program.
 *
 * <p/>The Elm program type takes an input model that is a Json object (Json.Encode.Value), and produces an output type
 * of String.
 *
 * <p/>On the java side, the input model is represented by the content type &lt;C&gt;. This content object will
 * converted to json and provided as input to the static Elm program.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Encapsulate a content model, and a static Elm program to render it.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ElmView<C> extends View {
    /** Holds the content model to render the view from. */
    private final C content;

    /** Holds the name of the Elm module to render with. */
    private final String moduleName;

    /**
     * Creates a view that will render the supplied content model using the static Elm program defined within the named
     * module.
     *
     * @param content    The content model.
     * @param moduleName The Elm module providing the static rendering program.
     */
    public ElmView(C content, String moduleName) {
        super(moduleName);

        this.content = content;
        this.moduleName = moduleName;
    }

    /**
     * Provides the name of the Elm module to render with.
     *
     * @return The name of the Elm module to render with.
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Provides the content model.
     *
     * @return The content model.
     */
    public C getContent() {
        return content;
    }
}
