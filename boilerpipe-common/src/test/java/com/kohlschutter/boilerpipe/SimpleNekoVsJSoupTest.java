package com.kohlschutter.boilerpipe;

import com.kohlschutter.boilerpipe.document.TextBlocks;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.jsoup.JsoupParser;
import com.kohlschutter.boilerpipe.sax.BoilerpipeSAXInput;
import com.kohlschutter.boilerpipe.sax.HTMLDocument;
import com.kohlschutter.boilerpipe.sax.HTMLFetcher;
import org.junit.Test;

import java.net.URL;

import static com.kohlschutter.boilerpipe.corpora.Formatter.*;
import static org.junit.Assert.*;

/**
 *
 */
public class SimpleNekoVsJSoupTest {

    @Test
    public void testBasicDocument1() throws Exception {

        test( "/test1.html" );

    }

    @Test
    public void testBasicDocument2() throws Exception {

        test( "/test2.html" );

    }

    @Test
    public void testBasicDocument3() throws Exception {

        test( "/test3.html" );

    }

    private void test( String path ) throws Exception {

        TextDocument td0 = Parsers.parseWithNeko( path );
        TextDocument td1 = Parsers.parseWithJSoup( path );

        assertNotNull( td0 );
        assertNotNull( td1 );

        assertEquals( td0.getTitle(), td1.getTitle() );

        assertEquals( table( TextBlocks.text( td0.getTextBlocks() ) ),
                      table( TextBlocks.text( td1.getTextBlocks() ) ) );

        assertEquals( table( td0.getTextBlocks() ),
                      table( td1.getTextBlocks() ) );

    }



}
