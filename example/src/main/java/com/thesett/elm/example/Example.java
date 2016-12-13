/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.elm.example;

import java.io.FileNotFoundException;

import javax.script.ScriptException;

import com.thesett.elm.ElmRenderer;

public class Example
{
    public static void main(String[] args) throws ScriptException, FileNotFoundException
    {
        ElmRenderer elmRenderer = new ElmRenderer("example.js");

        String result = (String) elmRenderer.runModule("Main", new Object());

        System.out.println(result);
    }
}
