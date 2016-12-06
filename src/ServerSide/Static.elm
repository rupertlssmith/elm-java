module ServerSide.Static exposing (..)

import Html exposing (Html)
import Json.Encode exposing (Value)


{-
   Defines the type of static programs (which have Never for the msg type).
   There are two static program types defines, one which produces Html Never,
   and one which produces Values.
-}


type alias HtmlProgram =
    Program Value (Html Never) Never


type alias JsonProgram =
    Program Value Value Never
