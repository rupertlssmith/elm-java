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
    console.log("inputModel:");
    console.log(inputModel);
    return Elm[moduleName].program(inputModel);
}


