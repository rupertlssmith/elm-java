var global = this;
var window = this;
var process = {
    env: {}
};

// This needs mocked out if Navigation.program is in the codebase, as it runs getLocation().
// Navigation.program cannot be used server side but it will cause a failure if this is not
// mocked out to provide it with some dummy values.
var document = {
    location: {
	    href: "",
	    host: "",
	    hostname: "",
	    protocol: "",
	    origin: "",
	    port_: "",
	    pathname: "",
	    search: "",
	    hash: "",
	    username: "",
	    password: ""
    }
};

var console = {};
console.debug = print;
console.warn = print;
console.log = print;

function staticElmProgram(moduleName, inputModel) {
    console.log("inputModel:");
    console.log(inputModel);
    return Elm[moduleName].program(inputModel);
}


