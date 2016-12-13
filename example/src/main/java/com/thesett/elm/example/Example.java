/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.elm.example;

import java.io.FileNotFoundException;

import javax.script.ScriptException;

import com.thesett.elm.ElmRenderer;
import com.thesett.util.resource.ResourceUtils;

public class Example
{
    public static void main(String[] args) throws ScriptException, FileNotFoundException
    {
        String examplePath = ResourceUtils.resourceFilePath("example.js");
        ElmRenderer elmRenderer = new ElmRenderer(examplePath);

        String result = (String) elmRenderer.runModule("Main", new Object());

        System.out.println(result);
    }
}
