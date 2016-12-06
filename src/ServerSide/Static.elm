module ServerSide.Static exposing (..)

import Html exposing (Html)
import Json.Encode exposing (Value)
import HtmlToString exposing (htmlToString)
import Native.ServerSidePrograms


{-
   Defines the type of static programs (which have Never for the msg type).
   There are three static program types defines, one which produces Html Never,
   and one which produces Json Values, and one which produces Strings.
-}


type alias HtmlProgram =
    Program Value (Html Never) Never


type alias JsonProgram =
    Program Value Value Never


type alias StringProgram =
    Program Value String Never



{-
   Takes an init function which produces Html Never from some Json input, and turns
   it into a StringProgram by applying 'htmlToString' to the result of the init
   function.
-}


htmlToStringProgram : { init : Value -> Html Never } -> StringProgram
htmlToStringProgram program =
    let
        init_ input =
            Debug.log "static" (htmlToString <| program.init input)
    in
        Native.ServerSidePrograms.programWithFlags
            { init = init_
            , subscriptions = \_ -> Sub.none
            , update = \_ -> \model -> ( model, Cmd.none )
            }
