port module Main exposing (example)

import HtmlToString exposing (htmlToString)
import Html exposing (Html)
import Json.Encode as Encode exposing (Value)
import Task


type alias Model =
    {}


type Msg
    = JustOneMore Msg


type alias StaticProgram =
    Platform.Program Value Model Msg


main : StaticProgram
main =
    Platform.programWithFlags
        { init = init
        , subscriptions = \_ -> Sub.none
        , update = \_ -> \model -> ( model, Cmd.none )
        }


init : Value -> ( Model, Cmd Msg )
init model =
    ( {}, Cmd.batch [ result example ] )


port result : String -> Cmd msg


example =
    htmlToString <| Html.div [] [ Html.text "hello world" ]
