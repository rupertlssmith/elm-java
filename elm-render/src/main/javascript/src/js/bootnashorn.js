var global = this;
var window = this;
var process = {
    env: {}
};

var console = {};
console.debug = print;
console.warn = print;
console.log = print;

var result = Elm.Main.program({});
console.log("result = " + result);
