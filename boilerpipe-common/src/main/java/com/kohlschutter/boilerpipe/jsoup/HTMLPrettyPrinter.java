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
package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.sax.DefaultExtendedContentHandler;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.xml.sax.SAXException;

/**
 * Simple pretty printer for HTML which uses the parsed DOM to print elements
 * and text to enable easily understandable HTML.
 */
public class HTMLPrettyPrinter {

    public static String format( Element root ) {

        try {

            PrintingContentHandler printingContentHandler = new PrintingContentHandler();

            JsoupSAXParser jsoupSAXParser = new JsoupSAXParser( printingContentHandler );

            jsoupSAXParser.parse( root );

            return printingContentHandler.buff.toString();

        } catch (SAXException e) {
            throw new RuntimeException( e );
        }

    }

    static class PrintingContentHandler extends DefaultExtendedContentHandler {

        StringBuilder buff = new StringBuilder();

        @Override
        public void startElement(Element element) {

            buff.append( "<" );
            buff.append( element.tagName() );

            for (Attribute attribute : element.attributes().asList()) {

                buff.append( "  " );
                buff.append( attribute.getKey() );
                buff.append( "=\"" );
                buff.append( attribute.getValue() );
                buff.append( "=\"\n" );

            }

            buff.append( ">\n" );

        }

        @Override
        public void endElement(Element element) {

            buff.append( "\n</" );
            buff.append( element.tagName() );
            buff.append( "/>\n" );

        }

        @Override
        public void textNode(TextNode textNode) {
            buff.append( textNode.text() );
        }

    }

}
