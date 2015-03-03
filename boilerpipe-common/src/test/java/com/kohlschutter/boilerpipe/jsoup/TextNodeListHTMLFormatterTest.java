package com.kohlschutter.boilerpipe.jsoup;

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


    private void test( String input, String expectedOutput ) throws Exception {

        TextNodeExtendedContentListener textNodeExtendedContentListener = new TextNodeExtendedContentListener();
        JsoupSAXParser jsoupSAXParser = new JsoupSAXParser( textNodeExtendedContentListener );

        jsoupSAXParser.parse( input );

        String html = TextNodeListHTMLFormatter.format( textNodeExtendedContentListener.getTextNodes() );

        assertEquals( expectedOutput, html );

    }

}