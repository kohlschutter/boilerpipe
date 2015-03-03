package com.kohlschutter.boilerpipe;

import com.kohlschutter.boilerpipe.corpora.CorporaAsserter;
import com.kohlschutter.boilerpipe.corpora.CorporaCache;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.kohlschutter.boilerpipe.jsoup.JsoupParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Tests using real world documents to look at real world accuracy.
 */
public class CorporaTests {

    CorporaCache corporaCache = new CorporaCache( getClass() );
    CorporaAsserter corporaAsserter = new CorporaAsserter( getClass() );

    @Test
    public void testAll() throws Exception {

        test( "http://www.bbc.com/news/world-europe-31669061", "testBbc1" );
        test( "http://www.cnn.com/2015/02/26/us/arizona-llamas-escape/index.html", "testCnn1" );
        test( "http://www.cnn.com/videos/us/2015/02/26/pkg-woman-loses-over-800-pounds.ktrk", "testCnn2" );
        test( "http://www.cnn.com/2015/02/27/world/mexico-knights-templar-leader-detained/index.html", "testCnn3" );
        test( "http://techcrunch.com/2015/02/27/nsa-bullk-telephony-metadata-program-reupped-until-parts-of-the-patriot-act-potentially-sunset/", "testTechcrunch1" );

    }

    private void test( String link, String key ) throws Exception {
        testContent( link, key );
        //testContentAsHTML( link, key );
    }

    private void testContent(String link, String key) throws Exception {

        String html = read( link );

        ArticleExtractor articleExtractor = ArticleExtractor.getInstance();

        //TextDocument textDocumentFromNeko = new BoilerpipeSAXInput(new InputSource(new StringReader(html))).getTextDocument();

        TextDocument textDocumentFromJsoup = new JsoupParser().parse( html );

        corporaAsserter.assertCorpora( key, articleExtractor.getText( textDocumentFromJsoup ) );

    }

    private void testContentAsHTML(String link, String key) throws Exception {

        String html = read( link );

        ArticleExtractor articleExtractor = ArticleExtractor.getInstance();

        //TextDocument textDocumentFromNeko = new BoilerpipeSAXInput(new InputSource(new StringReader(html))).getTextDocument();

        TextDocument textDocumentFromJsoup = new JsoupParser().parse( html );

        corporaAsserter.assertCorpora( key, articleExtractor.getHTML( textDocumentFromJsoup ) );

    }


    private String key( String link ) {

        return link.replaceAll( "[:/?=&%]", "_" );

    }


    private String read( String link ) throws IOException {

        String key = key( link );

        if ( corporaCache.contains( key ) ) {
            return corporaCache.read( key );
        }

        String data = fetch( link );

        corporaCache.write( key, data );

        return data;

    }


    // fetch the given link by going over the network.
    private String fetch( String link ) throws IOException {

        // TODO: should we strip any charset in the <meta> since after this we
        // always save as UTF8 ?

        URL url = new URL( link );

        Document doc = Jsoup.parse( url, 30000 );

        String content = doc.outerHtml();

        return content;

    }

}
