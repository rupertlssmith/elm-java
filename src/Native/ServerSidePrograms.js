var _rupertlssmith$elm_server_side_renderer$Native_ServerSidePrograms = function() {
    function programWithFlags(impl) {
        return function(flagDecoder) {
            return function(object, moduleName) {
                object['program'] = function program(flags) {
                    if (typeof flagDecoder === 'undefined') {
                        throw new Error(
                            'Are you trying to sneak a Never value into Elm? Trickster!\n' +
                            'It looks like ' + moduleName + '.main is defined with `' +
                            'programWithFlags` but has type `Program Never`.'
                        );
                    }

                    var result = A2(_elm_lang$core$Native_Json.run, flagDecoder, flags);
                    if (result.ctor === 'Err') {
                        throw new Error(
                            moduleName + '.program(...) was called with an unexpected argument.\n' +
                            'I tried to convert it to an Elm value, but ran into this problem:\n\n' +
                            result._0
                        );
                    }

                    return impl.init(result._0);
                };
            };
        };
    }

    return {
        programWithFlags: programWithFlags,
    };

}();
