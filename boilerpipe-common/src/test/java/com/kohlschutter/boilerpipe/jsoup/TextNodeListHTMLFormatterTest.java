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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for properly reconstructing the DOM tree after we have just a list of
 * TextNodes.
 */
public class TextNodeListHTMLFormatterTest {

    @Test
    public void testBasicNode() throws Exception {

        test( "<html><body><p>hello world</p></body><html>",
              "<p>hello world</p>" );

    }

    @Test
    public void testWithTwoSiblingNodes() throws Exception {

        test( "<html><body><p>first</p><p>second</p></body><html>",
              "<p>first</p><p>second</p>" );

    }

    @Test
    public void testAnchor() throws Exception {

        test( "<html><body><p><a href='http://cnn.com'>cnn</a></p></body><html>",
              "<a href=\"http://cnn.com\">cnn</a>" );

    }

    @Test
    public void testMixedContent() throws Exception {

        test( "<html><body><p>this is <b>mixed</b> content and here <i>is</i> more.</p></body><html>",
              "<p>this is <b>mixed</b> content and here <i>is</i> more.</p>" );

    }

    @Test
    @Ignore
    public void testNestedMixedContent() throws Exception {

        test( "<html><body><p>this is <b>mixed</b> content and here <i>is</i> more.<p>this is <b>mixed</b> content and here <i>is</i> more.</p></p></body><html>",
              "<p>this is <b>mixed</b> content and here <i>is</i> more.<p>this is <b>mixed</b> content and here <i>is</i> more.</p></p>" );

    }

    @Test
    public void testDeepNestedContent() throws Exception {

        test( "<html><body><blockquote><em>hello <b>world <a href='http://cnn.com'>cnn</a></b></em></blockquote></body><html>",
              "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <blockquote>\n" +
                "   <em>hello <b>world <a href=\"http://cnn.com\">cnn</a></b></em>\n" +
                "  </blockquote>\n" +
                " </body>\n" +
                "</html>",
              "<em>hello <b>world <a href=\"http://cnn.com\">cnn</a></b></em>" );

    }

    private void test( String input, String expectedOutput ) throws Exception {
        test( input, null, expectedOutput );
    }

    private void test( String input, String expectedParsed, String expectedOutput ) throws Exception {

        Document document = Jsoup.parse( input );

        if ( expectedParsed != null ) {
            assertEquals( expectedParsed, document.outerHtml() );
        }

        TextNodeExtendedContentListener textNodeExtendedContentListener = new TextNodeExtendedContentListener();
        JsoupSAXParser jsoupSAXParser = new JsoupSAXParser( textNodeExtendedContentListener );

        jsoupSAXParser.parse( document );

        String html = TextNodeListHTMLFormatter.format( textNodeExtendedContentListener.getTextNodes() );

        assertEquals( expectedOutput, html );

    }

}