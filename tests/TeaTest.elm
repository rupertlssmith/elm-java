module TeaTest exposing (..)

import Html
import Html.Attributes
import Html.Events
import HtmlQuery exposing (..)
import HtmlToString exposing (..)
import String
import Test exposing (..)
import Expect exposing (Expectation)


darthVader : Html.Html Msg
darthVader =
    Html.div []
        [ Html.p [] [ Html.text "Luke I'm your father." ]
        , Html.map SubComp lukeSkywalker
        ]


lukeSkywalker : Html.Html SubMsg
lukeSkywalker =
    Html.div []
        [ Html.button [ Html.Events.onClick LightSide ] [ Html.text "nooo" ]
        , Html.map SubSubComp r2d2
        , Html.map SubSubComp (Html.div [ Html.Attributes.class "force" ] [])
        , Html.map SubSubComp (Html.text "Han shot first")
        ]


r2d2 : Html.Html SubSubMsg
r2d2 =
    Html.span [ Html.Events.onClick Beep ] []


type Msg
    = SubComp SubMsg


type SubMsg
    = LightSide
    | SubSubComp SubSubMsg


type SubSubMsg
    = Beep


all : Test
all =
    describe "TeaTest"
        [ test "should render the parent view" <|
            \_ ->
                (Expect.equal 1 <|
                    List.length <|
                        queryByTagname "p" darthVader
                )
        , test "should render child views" <|
            \_ ->
                (Expect.equal 1 <|
                    List.length <|
                        queryByTagname "button" darthVader
                )
        , test "should render child views of child views" <|
            \_ ->
                (Expect.equal 1 <|
                    List.length <|
                        queryByTagname "span" darthVader
                )
        , test "should work when Html.map a empty div" <|
            \_ ->
                (Expect.equal 1 <|
                    List.length <|
                        queryByClassname "force" darthVader
                )
        , test "should work when Html.map a text node" <|
            \_ ->
                (Expect.equal True <|
                    String.contains "Han shot first" <|
                        htmlToString darthVader
                )
        ]
