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
package com.thesett.elm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.thesett.util.collections.CollectionUtil;
import com.thesett.util.resource.ResourceUtils;

/**
 * ElmRenderer sets up a Nashorn javascript environment with compiled Elm code. Static Elm programs within this code can
 * then be run against input data models.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Set up Nashorn to run an Elm program. </td></tr>
 * <tr><td> Run a static Html Elm program to render a view. </td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ElmRenderer
{
    /** The name of the helper function to call the static program with. */
    public static final String PROGRAM_FUNCTION = "staticElmProgram";

    /** The name of the javascript file to load the boot wrapper from. */
    public static final String BOOT_JS_FILENAME = "bootnashorn.js";

    /** The package path of the boot wrapper. */
    public static final String BOOT_JS_PACKAGE = "";

    /** Holds a reference to the Nashorn lifecycle manager. */
    private ScriptEngineManager nashornLifecycle;

    /** Holds a reference to the Nashorn execution engine. */
    private ScriptEngine engine;

    /** Holds a path to the boot wrapper javascript. */
    private String bootResourcePath;

    /** Holds a path to the compiled Elm javascript. */
    private final String elmJsResourcePath;

    public ElmRenderer(String elmJsResourcePath) throws ScriptException, FileNotFoundException
    {
        nashornLifecycle = new ScriptEngineManager();
        this.elmJsResourcePath = elmJsResourcePath;

        init();
    }

    /**
     * Runs the static Elm program found in the named module.
     *
     * @param  moduleName The name of the module to get the static program from.
     * @param  inputModel The input model to pass to the program.
     *
     * @return The output model from the program.
     *
     * @throws ScriptException If the javascript code fails to evaluate.
     */
    public Object runModule(String moduleName, Object inputModel) throws ScriptException
    {
        // Render the view.
        Invocable invocable = (Invocable) engine;

        Object result = null;

        try
        {
            result = invocable.invokeFunction(PROGRAM_FUNCTION, moduleName, inputModel);
        }
        catch (NoSuchMethodException e)
        {
            // This should not happen as the name of the function to run the program is known.
            // Promoting this to a bug.
            throw new IllegalStateException(e);
        }

        return result;
    }

    /**
     * Initializes the Nashorn javascript engine, ready to run the Elm static program, by loading the bootstrap code and
     * the compiled Elm code.
     *
     * @throws FileNotFoundException If the compiled Elm code cannot be loaded from the resource path specified in the
     *                               constructor.
     * @throws ScriptException       If the compiled Elm code fails to evaluate.
     */
    private void init() throws ScriptException, FileNotFoundException
    {
        // Load the Nashorn bootstrap code and the compiled Elm .js code.
        engine = nashornLifecycle.getEngineByName("nashorn");

        List<String> resources = ResourceUtils.getResources(BOOT_JS_FILENAME, BOOT_JS_PACKAGE);

        if ((resources == null) || resources.isEmpty())
        {
            throw new IllegalStateException("Failed to find " + BOOT_JS_PACKAGE + " " + BOOT_JS_FILENAME +
                " on the classpath.");
        }

        InputStream bootResource = ElmRenderer.class.getClassLoader().getResourceAsStream(CollectionUtil.first(resources));
        engine.eval(new InputStreamReader(bootResource));

        engine.eval(new FileReader(elmJsResourcePath));
    }
}
