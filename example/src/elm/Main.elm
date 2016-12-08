port module Main exposing (..)

import HtmlToString exposing (htmlToString)
import Html exposing (Html)
import Json.Encode as Encode exposing (Value)
import Task
import ServerSide.Static exposing (..)


main : StringProgram
main =
    htmlToStringProgram { init = init }


init : Value -> Html Never
init model =
    view


view : Html Never
view =
    Html.div [] [ Html.text "hello world" ]
