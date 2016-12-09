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
package com.thesett.elm.maven;

import com.github.eirslett.maven.plugins.frontend.lib.NodeExecutorConfig;
import com.github.eirslett.maven.plugins.frontend.lib.NodeTaskExecutor;

/**
 * Runs elm-make.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Run elm-make. </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ElmMakeRunner extends NodeTaskExecutor
{
    private static final String TASK_LOCATION = "node_modules/elm/binwrappers/elm-make";

    ElmMakeRunner(NodeExecutorConfig config)
    {
        super(config, TASK_LOCATION);
    }
}
