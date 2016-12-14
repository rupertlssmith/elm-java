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

import javax.script.ScriptException;

import com.thesett.elm.ElmRenderer;

/**
 * ElmModuleLoader abstracts different ways that the code implementing an Elm module can be loaded.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities
 * <tr><td> Load and provide access to an Elm renderer. </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public interface ElmModuleLoader
{
    /**
     * Provides an Elm renderer.
     *
     * @return An Elm renderer.
     *
     * @throws ScriptException       If the compiled Elm code fails to load because it contains an error.
     * @throws FileNotFoundException If the compiled Elm code fails to load because it cannot be found.
     */
    ElmRenderer loadRenderer() throws ScriptException, FileNotFoundException;
}
