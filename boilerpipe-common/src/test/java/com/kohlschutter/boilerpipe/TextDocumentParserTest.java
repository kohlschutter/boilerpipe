package com.kohlschutter.boilerpipe;

import com.kohlschutter.boilerpipe.corpora.CorporaAsserter;
import com.kohlschutter.boilerpipe.corpora.Formatter;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.sax.BoilerpipeSAXInput;
import com.kohlschutter.boilerpipe.sax.HTMLDocument;
import com.kohlschutter.boilerpipe.sax.HTMLFetcher;
import org.junit.Test;

import java.net.URL;

import static com.kohlschutter.boilerpipe.corpora.Formatter.*;

public class TextDocumentParserTest {

    CorporaAsserter corporaAsserter = new CorporaAsserter( getClass() );

    @Test
    public void testBasicDocument1() throws Exception {

        TextDocument doc = parse( "/test1.html" );

        corporaAsserter.assertCorpora( "testBasicDocument1", doc.getTextBlocks().toString() );

    }

    @Test
    public void testBasicDocument2() throws Exception {

        TextDocument doc = parse( "/test2.html" );

        corporaAsserter.assertCorpora( "testBasicDocument2", doc.getTextBlocks().toString() );

    }

    @Test
    public void testBasicDocument3() throws Exception {

        TextDocument doc = parse( "/test3.html" );

        // I can use jsoup for this by taking the code from flushBlock in
        // boilerpipe.sax.BoilerpipeHTMLContentHandler and doing a jsoup query
        // for div,p ... in jsoup and then taking the text from the elements to
        // build a  TextBlock

        corporaAsserter.assertCorpora( "testBasicDocument3", table( doc.getTextBlocks() ) );

    }

    private TextDocument parse( String path ) throws Exception {

        URL url = getClass().getResource( path );

        final HTMLDocument htmlDoc = HTMLFetcher.fetch( url );

        return new BoilerpipeSAXInput(htmlDoc.toInputSource())
            .getTextDocument();

    }

}