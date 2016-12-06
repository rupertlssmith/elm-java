port module Main exposing (example)

import HtmlToString exposing (htmlToString)
import Html exposing (Html)
import Json.Encode as Encode exposing (Value)
import Task
import ServerSide.Static exposing (HtmlProgram)


main : HtmlProgram
main =
    Platform.programWithFlags
        { init = init
        , subscriptions = \_ -> Sub.none
        , update = \_ -> \model -> ( model, Cmd.none )
        }


init : Value -> ( Html Never, Cmd Never )
init model =
    ( html, Cmd.none )


port result : String -> Cmd msg


html : Html Never
html =
    Html.div [] [ Html.text "hello world" ]


example =
    htmlToString <| html
