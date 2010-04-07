package it.sk.alfresco.clojure;

import java.io.BufferedReader;
import java.io.StringReader;

import clojure.lang.Compiler;

/**
 * Created by IntelliJ IDEA.
 * User: sku
 * Date: Apr 7, 2010
 * Time: 12:20:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class RunSwankServer
{
    public void init() throws Exception
    {
        final String startSwankScript =
                "(require 'clojure.main)\n" +
                        "(require 'swank.swank)\n" +
                        "(clojure.main/with-bindings\n" +
                        "  (swank/ignore-protocol-version \"2008-11-23\")\n" +
                        "  (swank/start-server \"nul\" :encoding \"utf-8-unix\" :port 4005))\n";


        Compiler.load(new StringReader(startSwankScript));

    }
}
