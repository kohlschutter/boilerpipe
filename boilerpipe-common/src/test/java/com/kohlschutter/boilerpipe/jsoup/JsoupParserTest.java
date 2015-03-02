package com.kohlschutter.boilerpipe.jsoup;

import com.google.common.io.ByteStreams;
import com.kohlschutter.boilerpipe.document.TextDocument;
import org.jsoup.Jsoup;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsoupParserTest {

    @Test
    public void testParse() throws Exception {

        JsoupParser jsoupParser = new JsoupParser();

        TextDocument textDocument = jsoupParser.parse( getClass().getResourceAsStream( "/test1.html" ), "UTF-8", "http://example.com" );

        assertEquals( "TextDocument{textBlocks=[TextBlock{isContent=false, text=hello world, labels=null, offsetBlocksStart=0, offsetBlocksEnd=0, numWords=2, numWordsInAnchorText=0, numWordsInWrappedLines=2, numWrappedLines=1, textDensity=2.0, linkDensity=0.0, containedTextElements={}, numFullTextWords=0, tagLevel=0}], title=''}",
                      textDocument.toString() );

    }

}