# elm-java

Rendering of static Html, Json or String data using Elm and running on a JVM. This enables the use of Elm as a templating language on the JVM.

### But Why...?

Elm makes a very nice templating language because it is a full and increasingly mature functional programming language which is statically typed. The 'functional' part means that arbitrary functions that manipulate data for rendering a template can be written and passed around. The 'statically typed' part means that types can be constructed to constrain templates such that only valid inputs can be accepted and only valid outputs will be produced; the elm compiler can verify that templates are correctly constructed and provide helpful error messages when they are not.

Most templating languages are neither functional or statically typed.

For example, the statically typed aspect can be used to define a structure representing Html, and then the template is constrained to only generate valid Html. Compare this to a string based tempalte language, and the difference is that the typed version actively prevents errors in creating the Html.

The functional language aspect allows much more complex transformations to be applied when generating output. This is useful for incorporating more sophisticated logic into a template. This is not always needed or desirable, but it does prove to be extremely useful when writing more complex templating systems such as code generators or pretty printers.

### Runtime Performance

Elm code is compiled to javascript, which is run on the Nashorn engine within Java. The Nashorn javascript engine can run reasonably quickly, but only once the JVM has 'warmed up' after JIT compiling bytecode. Under the default JVM settings, this only happens once code has been run many times, and tends to mean that Nashorn takes a while to warm up. The compile threshold can be altered to make this happen sooner by passing a command line argument to java:

    java -XX:CompileThreshold=10000 ...

Alternatively, some warm up code can be written to run the template engine in a loop at program start up to try and ensure code is fully compiled before it needs to be run.