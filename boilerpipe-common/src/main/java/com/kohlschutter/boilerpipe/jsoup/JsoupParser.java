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

import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.sax.BoilerpipeHTMLContentHandler;
import com.kohlschutter.boilerpipe.sax.ExtendedContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Parser that emulates SAX and just uses a stock BoilerpipeHTMLContentHandler
 */
public class JsoupParser {

    public TextDocument parse( InputStream inputStream, String charsetName , String baseUri ) throws IOException, SAXException {
        return parse( Jsoup.parse( inputStream, charsetName, baseUri ) );
    }

    public TextDocument parse( URL url, int timeoutMillis ) throws IOException, SAXException  {
        return parse( Jsoup.parse( url, timeoutMillis ) );
    }

    public TextDocument parse( String html ) throws SAXException {
        return parse( Jsoup.parse( html ) );
    }

    /**
     * Parse the given HTML to return a TextDocument.
     *
     * @return
     */
    public TextDocument parse( Element element ) throws SAXException {

        BoilerpipeHTMLContentHandler contentHandler = new BoilerpipeHTMLContentHandler();

        JsoupSAXParser jsoupSAXParser = new JsoupSAXParser( contentHandler );

        jsoupSAXParser.parse( element );

        return contentHandler.toTextDocument();

    }

}
