var global = this;
var window = this;
var process = {env: {}};

var console = {};
console.debug = print;
console.warn = print;
console.log = print;

var Platform = Java.type("javafx.application.Platform");
var Timer    = Java.type("java.util.Timer");

function setTimeout(func, milliseconds) {
	var timer = new Timer("setTimeout", true);
	timer.schedule(function() Platform.runLater(func), milliseconds);	
	return timer;
}

var app = Elm.Main.worker({});

app.ports.result.subscribe(function(msg) {
    console.log(msg);
});
