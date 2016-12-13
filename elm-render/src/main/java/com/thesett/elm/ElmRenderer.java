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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

    /** Holds a reference to the Nashorn lifecycle manager. */
    private ScriptEngineManager nashornLifecycle;

    /** Holds a reference to the Nashorn execution engine. */
    private ScriptEngine engine;

    public ElmRenderer()
    {
        nashornLifecycle = new ScriptEngineManager();
    }

    /**
     * Initializes the Nashorn javascript engine, ready to run the Elm static program, by loading the bootstrap code and
     * the compiled Elm code.
     *
     * @throws ScriptException If the compiled Elm code fails to evaluate.
     */
    public void init() throws ScriptException
    {
        // Load the Nashorn bootstrap code and the compiled Elm .js code.
        engine = nashornLifecycle.getEngineByName("nashorn");
        engine.eval("load (\"bootnashorn.js\");");
        engine.eval("load (\"main.js\");");
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
}
