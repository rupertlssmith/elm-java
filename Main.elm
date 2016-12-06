port module Main exposing (example)

import HtmlToString exposing (htmlToString)
import Html exposing (Html)
import Json.Encode as Encode exposing (Value)
import Task


type alias Model =
    {}


type Msg
    = None


type alias StaticProgram =
    Platform.Program Value Model Msg


message : msg -> Cmd msg
message x =
    Task.perform identity (Task.succeed x)


main : StaticProgram
main =
    Platform.programWithFlags
        { init = init
        , subscriptions = \_ -> Sub.none
        , update = update
        }


init : Value -> ( Model, Cmd Msg )
init model =
    let
        d =
            Debug.log "main" "test"
    in
        ( {}, Cmd.batch [ message None, result example ] )


update : Msg -> Model -> ( Model, Cmd Msg )
update action model =
    let
        d =
            Debug.log "main" action
    in
        ( model, Cmd.none )


port result : String -> Cmd msg


example =
    htmlToString <| view {}


view : Model -> Html Msg
view model =
    Html.div [] [ Html.text "hello world" ]
