/**
 * boilerpipe
 *
 * Copyright (c) 2009, 2014 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kohlschutter.boilerpipe.sax;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.xml.sax.ContentHandler;

/**
 * Extended content handler with methods for JSoup callbacks which provide
 * element and text node information.  Used so that we can have the JSoupParser
 * work with ExtendedContentHandlers which can also be used for testing the
 * JSoupParser's behavior.
 */
public interface ExtendedContentHandler extends ContentHandler {

    /**
     * Called at the start of each element.
     *
     * @param element
     */
    public void startElement( Element element );

    /**
     * Called at the end of each element.
     *
     * @param element
     */
    public void endElement( Element element );

    /**
     * Called when we have found a text node so we can keep a reference to it
     * for use with TextBlocks.
     *
     * @param textNode
     */
    public void textNode(TextNode textNode);

}