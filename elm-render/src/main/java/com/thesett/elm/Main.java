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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sun.javafx.application.PlatformImpl;

public class Main
{
    public static void main(String[] args) throws ScriptException
    {
        // JavaFX must be started, as Elm uses setTimeout, which has been polyfilled to use Platform.runLater.
        // Therefore the JavaFX thread must be started to process this.
        PlatformImpl.startup(new Runnable()
            {
                public void run()
                {
                }
            });

        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("nashorn");
        engine.eval("load (\"main.js\");");
        engine.eval("load (\"bootnashorn.js\");");

        PlatformImpl.exit();
    }
}
