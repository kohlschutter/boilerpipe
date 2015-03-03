package com.kohlschutter.boilerpipe.jsoup;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for properly reconstructing the DOM tree after we have just a list of
 * TextNodes.
 */
public class TextNodeListHTMLFormatterTest {

    @Test
    public void testFormat() throws Exception {

        TextNodeExtendedContentListener textNodeExtendedContentListener = new TextNodeExtendedContentListener();

        JsoupSAXParser jsoupSAXParser = new JsoupSAXParser( textNodeExtendedContentListener );

        jsoupSAXParser.parse( "<html><body><p>hello world</p></body><html>" );

        String html = TextNodeListHTMLFormatter.format( textNodeExtendedContentListener.getTextNodes() );

        assertEquals( "<p>hello world</p>\n",
                      html );

    }

}