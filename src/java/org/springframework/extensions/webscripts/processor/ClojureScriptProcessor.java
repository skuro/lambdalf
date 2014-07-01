/**
 * Copyright (c) 2011 Carlo Sciolla
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.springframework.extensions.webscripts.processor;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import clojure.lang.RT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.surf.core.scripts.ScriptException;
import org.springframework.extensions.webscripts.ScriptContent;

import spring.surf.webscript.WebScript;

/**
 * Loads Clojure script controllers from an {@link InputStream} and memoizes
 * the compiled {@link WebScript} instance
 *
 * @author Carlo Sciolla &lt;skuro@skuro.tk&gt;
 */
public class ClojureScriptProcessor extends AbstractScriptProcessor {

    static {
        // needed as clojure.lang.Compiler needs RT to be fully loaded beforehand
        RT.list();
    }

    private static final Log log = LogFactory.getLog(ClojureScriptProcessor.class);

    private Map<String, WebScript> compiledWebScripts =
            Collections.synchronizedMap(new HashMap<String,WebScript>());

    /**
     * {@inheritDoc}
     * @return "clj"
     */
    public String getExtension() {
        return "clj";
    }

    /**
     * {@inheritDoc}
     * @return "clojure"
     */
    public String getName() {
        return "clojure";
    }

    /**
     * Executes the Clojure script
     *
     * @param is    the input stream
     * @param out   the writer.  This can be null if no output is required.
     * @param model the context model for the script
     * @return WebScript  a new instance of the requested Clojure backed web script
     */
    @SuppressWarnings(value = "unchecked")
    protected WebScript compileClojureScript(InputStream is, Writer out, Map<String, Object> model) {
        log.debug("Executing Clojure script");
        log.debug("This line is to get rid of an IDEA warning: " + out);

        this.addProcessorModelExtensions(model);

        try {
            return (WebScript) clojure.lang.Compiler.load(new InputStreamReader(is));
        } catch (Exception exception) {
            throw new ScriptException("Error executing Clojure script", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ScriptContent findScript(String path) {
        // TODO: maybe check path against (ns)?
        return getScriptLoader().getScript(path);
    }

    /**
     * {@inheritDoc}
     */
    public Object executeScript(String path, Map<String, Object> model) {
        ScriptContent scriptContent = findScript(path);
        if (scriptContent == null) {
            throw new ScriptException("Unable to locate: " + path);
        }

        return executeScript(scriptContent, model);
    }

    /**
     * {@inheritDoc}
     */
    public Object executeScript(ScriptContent scriptContent, Map<String, Object> model) {
        String path  = scriptContent.getPath();
        WebScript webscript = this.compiledWebScripts.get(path);
        if (webscript == null) {
            if (log.isDebugEnabled()) {
                log.debug("Compiling new Clojure webscript at path " + path);
            }
            webscript = compileClojureScript(scriptContent.getInputStream(), null, model);
        }
        if (webscript == null) {
            throw new ScriptException("Cannot compile Clojure web script at path " + path);
        }

        synchronized (this) {
            if (log.isDebugEnabled()) {
                log.debug("Caching Clojure webscript at path " + path);
            }
            this.compiledWebScripts.put(path, webscript);
        }

        return webscript.run(scriptContent.getInputStream(), null, model);
    }

    /**
     * {@inheritDoc}
     */
    public Object unwrapValue(Object value) {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        init();
        this.compiledWebScripts = Collections.synchronizedMap(new HashMap<String,WebScript>());
    }
}
