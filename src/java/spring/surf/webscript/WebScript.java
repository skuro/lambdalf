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

package spring.surf.webscript;

/**
 * The main interface a WebScript must implement.
 *
 * Alpha: subject to change
 * @author Carlo Sciolla
 * @author Andreas Steffan
 * @since 1.0
 */
public interface WebScript {
    /**
     * Runs the WebScript and stores the result by updating and returning the model
     *
     * @param inputStream A stream to the clojure web script sources
     * @param writer The output writer
     * @param model The model where to put results
     * @return The updated model
     */
    // TODO better documentation and type safety. Or just migrate everything to pure Clojure
    Object run(Object inputStream, Object writer, Object model);
}
