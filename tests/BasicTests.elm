module BasicTests exposing (..)

-- where

import HtmlToString exposing (..)
import ServerSide.InternalTypes exposing (..)
import ServerSide.Helpers exposing (..)
import HtmlQuery exposing (..)
import Html
import Html.Attributes
import Html.Events
import Dict
import String
import Json.Encode
import Test exposing (..)
import Expect exposing (Expectation)
import Test.Runner as Runner
import Random.Pcg
import MarkdownTest
import TeaTest


-- DATA
-- Empty things


emptyText : String
emptyText =
    ""


emptyTextDecoded : NodeType
emptyTextDecoded =
    TextTag { text = emptyText }


emptyDiv : Html.Html msg
emptyDiv =
    Html.div [] []


emptyP : Html.Html msg
emptyP =
    Html.p [] []


emptyDivAsString : String
emptyDivAsString =
    "<div></div>"


emptyDivDecoded : NodeType
emptyDivDecoded =
    NodeEntry
        { tag = "div"
        , children = []
        , descendantsCount = 0
        , facts = emptyFacts
        }


emptyDivWithClick : Html.Html String
emptyDivWithClick =
    Html.div
        [ Html.Events.onClick ("hello")
        , Html.Events.onInput (\x -> x)
        ]
        []


inputText =
    Json.Encode.object
        [ ( "target"
          , Json.Encode.object [ ( "value", Json.Encode.string "hello" ) ]
          )
        ]


stuff : String
stuff =
    emptyDivWithClick
        |> triggerEvent "input" inputText
        |> Debug.log "event"
        |> Result.withDefault ""


emptyDivWithAddedAttribute : Html.Html String
emptyDivWithAddedAttribute =
    Html.div [] []
        |> addAttribute (Html.Attributes.class "dog")


emptyDivWithAddedAttributeAsString : String
emptyDivWithAddedAttributeAsString =
    "<div class=\"dog\"></div>"


emptyDivWithAddedAttributeDecoded : NodeType
emptyDivWithAddedAttributeDecoded =
    NodeEntry
        { tag = "div"
        , children = []
        , descendantsCount = 0
        , facts =
            { emptyFacts
                | stringOthers =
                    Dict.fromList [ ( "className", "dog" ) ]
            }
        }


emptyDivWithAttribute : Html.Html msg
emptyDivWithAttribute =
    Html.div [ Html.Attributes.class "dog" ] []


emptyDivWithAttributeAsString : String
emptyDivWithAttributeAsString =
    "<div class=\"dog\"></div>"


emptyDivWithAttributeDecoded : NodeType
emptyDivWithAttributeDecoded =
    NodeEntry
        { tag = "div"
        , children = []
        , descendantsCount = 0
        , facts =
            { emptyFacts
                | stringOthers =
                    Dict.fromList [ ( "className", "dog" ) ]
            }
        }


emptyDivWithManyAttributes : Html.Html msg
emptyDivWithManyAttributes =
    --Html.div
    --    [ Html.Attributes.class "dog"
    --    , Html.Attributes.value "cat"
    --    , Html.Attributes.width 50
    --    ]
    --[]
    emptyDiv
        |> addAttribute (Html.Attributes.class "dog")
        |> addAttribute (Html.Attributes.value "cat")
        |> addAttribute (Html.Attributes.width 50)


emptyDivWithManyAttributesAsString : String
emptyDivWithManyAttributesAsString =
    String.trim """
<div class="dog" value="cat" width="50"></div>
    """


emptyDivWithManyAttributesDecoded : NodeType
emptyDivWithManyAttributesDecoded =
    NodeEntry
        { tag = "div"
        , children = []
        , descendantsCount = 0
        , facts =
            { emptyFacts
                | stringOthers =
                    Dict.fromList
                        [ ( "className", "dog" )
                        , ( "value", "cat" )
                        , ( "width", "50" )
                        ]
            }
        }


emptyDivWithStyle : Html.Html msg
emptyDivWithStyle =
    Html.div [ Html.Attributes.style [ ( "color", "red" ) ] ] []


emptyDivWithStyleAsString : String
emptyDivWithStyleAsString =
    "<div style=\"color:red\"></div>"


emptyDivWithStyleDecoded : NodeType
emptyDivWithStyleDecoded =
    NodeEntry
        { tag = "div"
        , children = []
        , descendantsCount = 0
        , facts =
            { emptyFacts
                | styles =
                    Dict.fromList [ ( "color", "red" ) ]
            }
        }



-- Non empty things!


nonEmptyText : String
nonEmptyText =
    "hello"


nonEmptyTextDecoded : NodeType
nonEmptyTextDecoded =
    TextTag { text = nonEmptyText }


oneChildDiv : Html.Html msg
oneChildDiv =
    Html.div [] [ Html.text nonEmptyText ]


oneChildDivAsString : String
oneChildDivAsString =
    "<div>" ++ nonEmptyText ++ "</div>"


oneChildDivDecoded : NodeType
oneChildDivDecoded =
    NodeEntry
        { tag = "div"
        , children = [ nonEmptyTextDecoded ]
        , descendantsCount = 1
        , facts = emptyFacts
        }


oneChildSpan : Html.Html msg
oneChildSpan =
    Html.span [] [ Html.text nonEmptyText ]


oneChildSpanAsString : String
oneChildSpanAsString =
    "<span>" ++ nonEmptyText ++ "</span>"


oneChildSpanDecoded : NodeType
oneChildSpanDecoded =
    NodeEntry
        { tag = "span"
        , children = [ nonEmptyTextDecoded ]
        , descendantsCount = 1
        , facts = emptyFacts
        }


twoChildForm : Html.Html msg
twoChildForm =
    Html.form [] [ oneChildDiv, oneChildSpan ]


twoChildFormAsString : String
twoChildFormAsString =
    "<form>" ++ oneChildDivAsString ++ oneChildSpanAsString ++ "</form>"


twoChildFormDecoded : NodeType
twoChildFormDecoded =
    let
        children =
            [ oneChildDivDecoded
            , oneChildSpanDecoded
            ]
    in
        NodeEntry
            { tag = "form"
            , children = children
            , descendantsCount = 4
            , facts = emptyFacts
            }



-- HELPERS


textTagTypeFromString : String -> NodeType
textTagTypeFromString =
    Html.text >> nodeTypeFromHtml


textFromHtml : String -> String
textFromHtml =
    Html.text >> htmlToString


assertEqualPair : ( a, a ) -> Expectation
assertEqualPair ( left, right ) =
    Expect.equal left right


countDescendents : NodeType -> Int
countDescendents nodeType =
    case nodeType of
        NodeEntry { descendantsCount } ->
            descendantsCount

        TextTag _ ->
            1

        _ ->
            0



-- TESTS


textTests : Test
textTests =
    concat
        [ test "empty strings are empty results" <|
            \_ -> assertEqualPair ( emptyText, textFromHtml emptyText )
        , test "empty strings are decoded to empty text tags" <|
            \_ -> assertEqualPair ( emptyTextDecoded, textTagTypeFromString emptyText )
        , test "non empty strings are non empty results" <|
            \_ -> assertEqualPair ( nonEmptyText, textFromHtml nonEmptyText )
        , test "non strings are decoded to non text tags" <|
            \_ -> assertEqualPair ( nonEmptyTextDecoded, textTagTypeFromString nonEmptyText )
        ]


nodeTests : Test
nodeTests =
    concat
        [ test "empty divs are empty divs as a string" <|
            \_ ->
                assertEqualPair ( emptyDivAsString, htmlToString emptyDiv )
        , test "empty divs are decoded to empty div nodes" <|
            \_ ->
                assertEqualPair ( emptyDivDecoded, nodeTypeFromHtml emptyDiv )
        , test "empty divs are empty divs as a string" <|
            \_ ->
                assertEqualPair ( emptyDivWithAddedAttributeAsString, htmlToString emptyDivWithAddedAttribute )
        , test "empty divs are decoded to empty div nodes" <|
            \_ ->
                assertEqualPair ( emptyDivWithAddedAttributeDecoded, nodeTypeFromHtml emptyDivWithAddedAttribute )
        , test "empty divs with classes get classes as a string" <|
            \_ ->
                assertEqualPair ( emptyDivWithAttributeAsString, htmlToString emptyDivWithAttribute )
        , test "empty divs with classes are decoded to empty div nodes with classes" <|
            \_ -> assertEqualPair ( emptyDivWithAttributeDecoded, nodeTypeFromHtml emptyDivWithAttribute )
        , test "empty divs with many attributes get attributes as a string" <|
            \_ -> assertEqualPair ( emptyDivWithManyAttributesAsString, htmlToString emptyDivWithManyAttributes )
        , test "empty divs with many attributes are decoded to empty div nodes with attributes" <|
            \_ -> assertEqualPair ( emptyDivWithManyAttributesDecoded, nodeTypeFromHtml emptyDivWithManyAttributes )
        , test "empty divs with styles get styles as a string" <|
            \_ -> assertEqualPair ( emptyDivWithStyleAsString, htmlToString emptyDivWithStyle )
        , test "empty divs with styles are decoded to empty div nodes with styles" <|
            \_ -> assertEqualPair ( emptyDivWithStyleDecoded, nodeTypeFromHtml emptyDivWithStyle )
        , test "divs with one non-empty text node are just a div with text" <|
            \_ -> assertEqualPair ( oneChildDivAsString, htmlToString oneChildDiv )
        , test "divs with one non-empty text node are decoded to just a div with text" <|
            \_ -> assertEqualPair ( oneChildDivDecoded, nodeTypeFromHtml oneChildDiv )
        , test "spans with one non-empty text node are just a span with text" <|
            \_ -> assertEqualPair ( oneChildSpanAsString, htmlToString oneChildSpan )
        , test "spans with one non-empty text node are decoded to just a span with text" <|
            \_ -> assertEqualPair ( oneChildSpanDecoded, nodeTypeFromHtml oneChildSpan )
        , test "forms with two non-empty text children are just a form with text" <|
            \_ -> assertEqualPair ( twoChildFormAsString, htmlToString twoChildForm )
        , test "forms with two non-empty text children are decoded to just a form with text" <|
            \_ -> assertEqualPair ( twoChildFormDecoded, nodeTypeFromHtml twoChildForm )
        ]


queryTests : Test
queryTests =
    let
        p1 =
            Html.p
                [ Html.Attributes.class "my-class other-class"
                , Html.Attributes.id "myP"
                ]
                []

        p2 =
            Html.p [ Html.Attributes.class "foo my-class" ] []

        p3 =
            Html.p [ Html.Attributes.class "foo bar moo" ] []
    in
        concat
            [ test "query by tagname returns an empty list if no matches" <|
                \_ ->
                    assertEqualPair ( [], queryByTagname "img" emptyDiv )
            , test "query by tagname finds a node" <|
                \_ ->
                    assertEqualPair
                        ( [ nodeTypeFromHtml emptyDiv ]
                        , queryByTagname "div" emptyDiv
                        )
            , test "query finds all nodes by tagname" <|
                \_ ->
                    assertEqualPair
                        ( [ nodeTypeFromHtml emptyP
                          , nodeTypeFromHtml emptyP
                          ]
                        , queryByTagname "p" (Html.div [] [ emptyP, emptyP ])
                        )
            , test "query by id returns an empty list if no matches" <|
                \_ ->
                    assertEqualPair
                        ( []
                        , queryById "myId" (Html.div [] [ emptyP, emptyP ])
                        )
            , test "query by id finds a node" <|
                \_ ->
                    assertEqualPair
                        ( [ nodeTypeFromHtml p1 ]
                        , queryById "myP" (Html.div [] [ p1 ])
                        )
            , test "query by classname returns an empty list if no matches" <|
                \_ ->
                    assertEqualPair
                        ( []
                        , queryByClassname "my-class" (Html.div [] [ emptyP, emptyP ])
                        )
            , test "query by class finds a node" <|
                \_ ->
                    assertEqualPair
                        ( [ nodeTypeFromHtml p1 ]
                        , queryByClassname "my-class" (Html.div [] [ p1, emptyP ])
                        )
            , test "query by class finds all nodes" <|
                \_ ->
                    assertEqualPair
                        ( [ nodeTypeFromHtml p1, nodeTypeFromHtml p2 ]
                        , queryByClassname "my-class" (Html.div [] [ p1, p2, p3 ])
                        )
            , test "query by attribute finds all nodes" <|
                \_ ->
                    assertEqualPair
                        ( [ emptyDivWithManyAttributesDecoded ]
                        , queryByAttribute "width" "50" (Html.div [] [ p1, emptyDivWithManyAttributes, p3 ])
                        )
            , test "query by classlist returns an empty list if no matches" <|
                \_ ->
                    assertEqualPair
                        ( []
                        , queryByClassList [ "foo", "nope" ] (Html.div [] [ p1, p2, p3 ])
                        )
            , test "query by classlist finds all nodes" <|
                \_ ->
                    assertEqualPair
                        ( [ nodeTypeFromHtml p3 ]
                        , queryByClassList [ "foo", "moo" ] (Html.div [] [ p1, p2, p3 ])
                        )
            ]


allTests : Test
allTests =
    concat
        [ textTests
        , nodeTests
        , queryTests
        , MarkdownTest.nodeTests
        , TeaTest.all
        ]



-- seed =
--     Random.Pcg.initialSeed 227852860
--
--
-- main : Program Never
-- main =
--     Runner.run <| Runner.fromTest 1 seed allTests
