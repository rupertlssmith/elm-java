var main = require('./main.js');

var app = main.Main.worker({});

app.ports.result.subscribe(function(msg) {
    console.log(msg);
});
