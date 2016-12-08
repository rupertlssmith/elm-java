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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * InstallElmMojo builds on the frontend-maven-plugin to add support for installing elm build tools.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Install Elm build tools. </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */

@Mojo(name = "install-elm", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class InstallElmMojo extends AbstractMojo
{
    /** The version of Elm to install. */
    @Parameter(property = "elmVersion", required = true)
    private String elmVersion;

    /** Skips execution of this mojo. */
    @Parameter(property = "skip.installnodenpm", defaultValue = "false")
    private Boolean skip;

    /** {@inheritDoc} */
    public void execute() throws MojoFailureException
    {
        System.out.println("execute");
        
        if (!skipExecution())
        {
        }
    }

    /** {@inheritDoc} */
    protected boolean skipExecution()
    {
        return this.skip;
    }
}
