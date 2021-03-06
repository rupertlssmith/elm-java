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

import java.util.Collections;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.lib.InstallationException;
import com.github.eirslett.maven.plugins.frontend.lib.NPMInstaller;
import com.github.eirslett.maven.plugins.frontend.lib.NodeInstaller;
import com.github.eirslett.maven.plugins.frontend.lib.ProxyConfig;
import com.github.eirslett.maven.plugins.frontend.lib.TaskRunnerException;
import com.github.eirslett.maven.plugins.frontend.mojo.AbstractFrontendMojo;

import com.github.eirslett.maven.plugins.frontend.mojo.MojoUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.sonatype.plexus.build.incremental.BuildContext;

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
public class InstallElmMojo extends AbstractFrontendMojo
{
    private static final String NPM_REGISTRY_URL = "npmRegistryURL";

    /** The version of Elm to install. */
    @Parameter(property = "elmVersion", required = true)
    private String elmVersion;

    /** The version of Elm to install. */
    @Parameter(property = "elmGithubInstallVersion", required = true)
    private String elmGithubInstallVersion;

    /** Where to download Node.js binary from. Defaults to http://nodejs.org/dist/ */
    @Parameter(
        property = "nodeDownloadRoot", required = false, defaultValue = NodeInstaller.DEFAULT_NODEJS_DOWNLOAD_ROOT
    )
    private String nodeDownloadRoot;

    /** Where to download NPM binary from. Defaults to http://registry.npmjs.org/npm/-/ */
    @Parameter(property = "npmDownloadRoot", required = false, defaultValue = NPMInstaller.DEFAULT_NPM_DOWNLOAD_ROOT)
    private String npmDownloadRoot;

    /**
     * Where to download Node.js and NPM binaries from.
     *
     * @deprecated use {@link #nodeDownloadRoot} and {@link #npmDownloadRoot} instead, this configuration will be used
     *             only when no {@link #nodeDownloadRoot} or {@link #npmDownloadRoot} is specified.
     */
    @Parameter(property = "downloadRoot", required = false, defaultValue = "")
    @Deprecated
    private String downloadRoot;

    /**
     * The version of Node.js to install. IMPORTANT! Most Node.js version names start with 'v', for example 'v0.10.18'
     */
    @Parameter(property = "nodeVersion", required = true)
    private String nodeVersion;

    /** The version of NPM to install. */
    @Parameter(property = "npmVersion", required = false, defaultValue = "provided")
    private String npmVersion;

    /** Server Id for download username and password */
    @Parameter(property = "serverId", defaultValue = "")
    private String serverId;

    @Parameter(property = "session", defaultValue = "${session}", readonly = true)
    private MavenSession session;

    /** Skips execution of this mojo. */
    @Parameter(property = "skip.installnodenpm", defaultValue = "false")
    private Boolean skip;

    @Component
    private BuildContext buildContext;

    @Component(role = SettingsDecrypter.class)
    private SettingsDecrypter decrypter;

    @Parameter(property = "frontend.npm.npmInheritsProxyConfigFromMaven", required = false, defaultValue = "true")
    private boolean npmInheritsProxyConfigFromMaven;

    /** Registry override, passed as the registry option during npm install if set. */
    @Parameter(property = NPM_REGISTRY_URL, required = false, defaultValue = "")
    private String npmRegistryURL;

    @Override
    public void execute(FrontendPluginFactory factory) throws InstallationException, TaskRunnerException
    {
        ProxyConfig proxyConfig = MojoUtils.getProxyConfig(session, decrypter);
        String nodeDownloadRoot = getNodeDownloadRoot();
        String npmDownloadRoot = getNpmDownloadRoot();
        Server server = MojoUtils.decryptServer(serverId, session, decrypter);

        // Download and install node and npm.
        if (null != server)
        {
            factory.getNodeInstaller(proxyConfig)
                .setNodeVersion(nodeVersion)
                .setNodeDownloadRoot(nodeDownloadRoot)
                .setNpmVersion(npmVersion)
                .setUserName(server.getUsername())
                .setPassword(server.getPassword())
                .install();
            factory.getNPMInstaller(proxyConfig)
                .setNodeVersion(nodeVersion)
                .setNpmVersion(npmVersion)
                .setNpmDownloadRoot(npmDownloadRoot)
                .setUserName(server.getUsername())
                .setPassword(server.getPassword())
                .install();
        }
        else
        {
            factory.getNodeInstaller(proxyConfig)
                .setNodeVersion(nodeVersion)
                .setNodeDownloadRoot(nodeDownloadRoot)
                .setNpmVersion(npmVersion)
                .install();
            factory.getNPMInstaller(proxyConfig)
                .setNodeVersion(this.nodeVersion)
                .setNpmVersion(this.npmVersion)
                .setNpmDownloadRoot(npmDownloadRoot)
                .install();
        }

        // Use npm to install elm and elm-github-install
        //File packageJson = new File(workingDirectory, "package.json");

        if ((buildContext == null) /*|| buildContext.hasDelta(packageJson)*/ || !buildContext.isIncremental())
        {
            proxyConfig = getNPMProxyConfig();
            factory.getNpmRunner(proxyConfig, getRegistryUrl())
                .execute("install elm@" + elmVersion, environmentVariables);
            factory.getNpmRunner(proxyConfig, getRegistryUrl())
                .execute("install elm-github-install@" + elmGithubInstallVersion, environmentVariables);
        }
        else
        {
            getLog().info("Skipping npm install as package.json unchanged");
        }
    }

    @Override
    protected boolean skipExecution()
    {
        return this.skip;
    }

    private String getNodeDownloadRoot()
    {
        if ((downloadRoot != null) && !"".equals(downloadRoot) &&
                NodeInstaller.DEFAULT_NODEJS_DOWNLOAD_ROOT.equals(nodeDownloadRoot))
        {
            return downloadRoot;
        }

        return nodeDownloadRoot;
    }

    private String getNpmDownloadRoot()
    {
        if ((downloadRoot != null) && !"".equals(downloadRoot) &&
                NPMInstaller.DEFAULT_NPM_DOWNLOAD_ROOT.equals(npmDownloadRoot))
        {
            return downloadRoot;
        }

        return npmDownloadRoot;
    }

    private ProxyConfig getNPMProxyConfig()
    {
        if (npmInheritsProxyConfigFromMaven)
        {
            return MojoUtils.getProxyConfig(session, decrypter);
        }
        else
        {
            getLog().info("npm not inheriting proxy config from Maven");

            return new ProxyConfig(Collections.<ProxyConfig.Proxy>emptyList());
        }
    }

    private String getRegistryUrl()
    {
        // check to see if overridden via `-D`, otherwise fallback to pom value
        return System.getProperty(NPM_REGISTRY_URL, npmRegistryURL);
    }
}
