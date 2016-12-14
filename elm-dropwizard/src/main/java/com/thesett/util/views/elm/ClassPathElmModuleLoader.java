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
import com.thesett.util.resource.ResourceUtils;

/**
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Load and provide access to an Elm renderer. </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ClassPathElmModuleLoader implements ElmModuleLoader
{
    private final String classPathLocation;

    public ClassPathElmModuleLoader(String classPathLocation)
    {
        this.classPathLocation = classPathLocation;
    }

    /** {@inheritDoc} */
    public ElmRenderer loadRenderer() throws ScriptException, FileNotFoundException
    {
        String resourcePath = ResourceUtils.resourceFilePath(classPathLocation);

        return new ElmRenderer(resourcePath);
    }
}
