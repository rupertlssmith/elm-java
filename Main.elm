module Main exposing (example)

import HtmlToString exposing (htmlToString)
import Html exposing (Html)
import Json.Encode as Encode exposing (Value)


type alias Model =
    {}


type Msg
    = None


type alias StaticProgram =
    Platform.Program Value Model Msg


init : Value -> ( Model, Cmd Msg )
init model =
    ( {}, Cmd.none )


main : StaticProgram
main =
    Platform.programWithFlags
        { init = init
        , subscriptions = \_ -> Sub.none
        , update = update
        }


update : Msg -> Model -> ( Model, Cmd Msg )
update action model =
    ( model, Cmd.none )


example =
    htmlToString <| view {}


view : Model -> Html Msg
view model =
    Html.div [] [ Html.text "hello world" ]
