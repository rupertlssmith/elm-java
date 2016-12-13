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

import java.util.List;
import java.util.Map;

/**
 * ElmBundleConfig provides all of the configuration needed to set up the Elm bundle.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Provide the Elm bundle configuration.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ElmBundleConfig
{
    /** Indicates whether the compiled Elm static program should be cached, or re-loaded each time. */
    private boolean cacheTemplates = true;

    /** Optionally provides overrides from Elm module names to files containing the compiled Elm code fro the module. */
    private List<Map<String, String>> overrides;

    public boolean isCacheTemplates()
    {
        return cacheTemplates;
    }

    public void setCacheTemplates(boolean cacheTemplates)
    {
        this.cacheTemplates = cacheTemplates;
    }

    public List<Map<String, String>> getOverrides()
    {
        return overrides;
    }

    public void setOverrides(List<Map<String, String>> overrides)
    {
        this.overrides = overrides;
    }
}
