port module Main exposing (..)

import BasicTests
import MarkdownTest
import TeaTest
import Test
import Test.Runner.Node exposing (run, TestProgram)
import Json.Encode exposing (Value)


tests =
    Test.concat
        [ BasicTests.allTests
        , MarkdownTest.nodeTests
        , TeaTest.all
        ]


main : TestProgram
main =
    run emit tests


port emit : ( String, Value ) -> Cmd msg
