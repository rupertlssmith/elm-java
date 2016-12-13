var global = this;
var window = this;
var process = {
    env: {}
};

var console = {};
console.debug = print;
console.warn = print;
console.log = print;

function staticElmProgram(moduleName, inputModel) {
    var result = Elm[moduleName].program(inputModel);

    console.log("result = " + result);

    return result;
}


