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

import java.io.File;
import java.io.IOException;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendException;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.lib.NodeExecutorConfig;
import com.github.eirslett.maven.plugins.frontend.lib.Platform;
import com.github.eirslett.maven.plugins.frontend.mojo.AbstractFrontendMojo;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td>
 * </table></pre>
 *
 * @author Rupert Smith
 */
@Mojo(name = "elm", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class BuildElmMojo extends AbstractFrontendMojo {
    /**
     * The name of the subdirectory to do all elm build work in. This is directly beneath under the working directory.
     */
    private static final String ELM_DIR = "elm";

    /**
    * The directory where front end files will be output by elm-make. If this is set then they will be refreshed so
    * they correctly show as modified.
    */
    @Parameter(property = "outputdir")
    private File outputdir;

    /** The directory containing Elm files and elm-package.json. */
    @Parameter(property = "srcdir", required = true)
    private File srcdir;

    /** Skips execution of this mojo. */
    @Parameter(property = "skip.elm", defaultValue = "false")
    private Boolean skip;

    @Component
    private BuildContext buildContext;

    /** {@inheritDoc} */
    public void execute(FrontendPluginFactory factory) throws FrontendException {
        if (shouldExecute()) {
            NodeExecutorConfig executorConfig = factory.getExecutorConfig();
            File workingDirectory = executorConfig.getWorkingDirectory();

            // Set up the working directory to be in an elm directory immediately beneath the working directory.
            String elmWorkingPath = workingDirectory.getPath() + "/" + ELM_DIR;
            File elmWorkingDir = null;

            try {
                elmWorkingDir = createDirIfNotExists(workingDirectory, elmWorkingPath);
            }catch (IOException e) {
                throw new FrontendException(e.getMessage(), e);
            }

            executorConfig = new WrappedExecutorConfig(executorConfig, elmWorkingDir);

            // Copy sources from the source dir to the working dir.
            try {
                FileUtils.copyDirectory(srcdir, elmWorkingDir);
            }catch (IOException e) {
                throw new FrontendException(e.getMessage(), e);
            }

            // Install dependencies and make it.
            ElmGithubInstallRunner elmGithubInstallRunner = new ElmGithubInstallRunner(executorConfig);
            ElmMakeRunner elmMakeRunner = new ElmMakeRunner(executorConfig);

            elmGithubInstallRunner.execute("", environmentVariables);
            elmMakeRunner.execute("--output main.js", environmentVariables);

            if (outputdir != null) {
                getLog().info("Refreshing files after elm-make: " + outputdir);
                buildContext.refresh(outputdir);
            }
        }
    }

    protected boolean skipExecution() {
        return this.skip;
    }

    private boolean shouldExecute() {
        return true;
    }

    private File createDirIfNotExists(File parentDir, String directoryName) throws IOException {
        File directory = new File(String.valueOf(directoryName));

        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    private class WrappedExecutorConfig implements NodeExecutorConfig {
        private final NodeExecutorConfig nodeExecutorConfig;
        private final File workingDirectory;

        public WrappedExecutorConfig(NodeExecutorConfig nodeExecutorConfig, File workingDirectory) {
            this.nodeExecutorConfig = nodeExecutorConfig;
            this.workingDirectory = workingDirectory;
        }

        public File getNodePath() {
            return nodeExecutorConfig.getNodePath();
        }

        public File getNpmPath() {
            return nodeExecutorConfig.getNpmPath();
        }

        public File getWorkingDirectory() {
            return workingDirectory;
        }

        public Platform getPlatform() {
            return nodeExecutorConfig.getPlatform();
        }
    }
}
